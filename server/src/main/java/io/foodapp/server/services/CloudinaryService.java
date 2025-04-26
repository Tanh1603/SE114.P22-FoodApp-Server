package io.foodapp.server.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Environment environment;
    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", environment.getProperty("CLOUDINARY_FOLDER"), "resource_type", "auto"),
                null).get("secure_url").toString();
    }

    public List<String> uploadMultipleFile(List<MultipartFile> files) throws IOException {
        List<CompletableFuture<String>> futures = new ArrayList<>();
        for (MultipartFile file : files) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return uploadFile(file);
                } catch (IOException e) {
                    throw new RuntimeException("Error upload file");
                }
            });
            futures.add(future);
        }
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        allOf.join();

        List<String> imageUrls = new ArrayList<>();
        for (CompletableFuture<String> future : futures) {
            try {
                imageUrls.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("error");
            }
        }
        return imageUrls;
    }

    public void deleteFile(String imageUrl) throws IOException {
        String publicId = imageUrl
                .replaceFirst("^.*?/upload/", "") // bỏ trước upload/
                .replaceFirst("^v\\d+/", "") // bỏ version ví dụ v123456/
                .replaceFirst("\\.[^.]+$", ""); // bỏ phần mở rộng .png, .jpg...
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
    }

    public void deleteMultipleFile(List<String> imageUrls) throws IOException {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (String file : imageUrls) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    deleteFile(file);
                } catch (IOException e) {
                    throw new RuntimeException("Error upload file");
                }
            });
            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        allOf.join();
    }
}

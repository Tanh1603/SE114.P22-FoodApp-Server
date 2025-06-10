package io.foodapp.server.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.foodapp.server.utils.ImageInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CloudinaryService {

    private final Environment environment;
    private final Cloudinary cloudinary;

    public void deleteImage(String publicId) throws IOException {
        if (publicId == null || publicId.isEmpty()) {
            return;
        }
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
    }

    public void deleteMultipleImage(List<String> publicIds) throws Exception {
        if (publicIds == null || publicIds.isEmpty()) {
            return;
        }
        cloudinary.api().deleteResources(publicIds, ObjectUtils.asMap("invalidate", true));
    }

    public ImageInfo uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty() || file.getSize() == 0) {
            return null;
        }

        var res = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", environment.getProperty("CLOUDINARY_FOLDER"), "resource_type", "auto"),
                null);

        return new ImageInfo(
                res.get("public_id").toString(),
                res.get("url").toString()
        );
    }

    public List<ImageInfo> uploadMultipleImage(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            return null;
        }

        List<MultipartFile> validImages = files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .toList();

        if (validImages.isEmpty()) {
            return null;
        }

        return validImages.parallelStream()
                .map(file -> {
                    try {
                        return uploadImage(file);
                    } catch (IOException e) {
                        throw new RuntimeException("Error uploading image", e);
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

}

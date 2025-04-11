package io.foodapp.server.services;

import java.io.IOException;
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

    public void deleteFile(String imageUrl) throws IOException {
        String publicId = imageUrl
                .replaceFirst("^.*?/upload/", "") // bỏ trước upload/
                .replaceFirst("^v\\d+/", "") // bỏ version ví dụ v123456/
                .replaceFirst("\\.[^.]+$", ""); // bỏ phần mở rộng .png, .jpg...
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
    }

}

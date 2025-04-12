package io.foodapp.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.foodapp.server.services.CloudinaryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final CloudinaryService cloudinaryService;

    // Endpoint to test Cloudinary upload
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadImage(@RequestParam("avatar") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }
            String imageUrl = cloudinaryService.uploadFile(file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteImage(@RequestParam("imageUrl") String imageUrl) {
        try {
            cloudinaryService.deleteFile(imageUrl);
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}

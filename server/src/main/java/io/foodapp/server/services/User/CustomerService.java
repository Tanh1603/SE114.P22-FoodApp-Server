package io.foodapp.server.services.User;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.UpdateRequest;

import io.foodapp.server.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CloudinaryService cloudinaryService;

    public UserRecord getCustomerDetails(String customerId) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        try {

            return firebaseAuth.getUser(customerId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching customer details: " + e.getMessage());
        }
    }

    public List<UserRecord> listAllUsers() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        try {
            List<UserRecord> users = new ArrayList<>();
            ListUsersPage page = firebaseAuth.listUsers(null);
            while (page != null) {
                for (ExportedUserRecord user : page.getValues()) {
                    users.add(user);
                }
                page = page.getNextPage();
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException("Error listing users: " + e.getMessage(), e);
        }
    }

    public UserRecord updatePhotoUrl(String customerId, MultipartFile image) {
        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (image == null || image.isEmpty()) {
                throw new IllegalArgumentException("Photo URL cannot be null or empty");
            }
            String photoUrl = cloudinaryService.uploadImage(image).getUrl();

            UpdateRequest request = new UpdateRequest(customerId)
                    .setPhotoUrl(photoUrl);

            return firebaseAuth.updateUser(request);
        } catch (Exception e) {
            log.error("Error updating photo URL for customer {}: {}", customerId, e.getMessage());
            throw new RuntimeException("Error updating photo URL: " + e.getMessage(), e);
        }
    }

}

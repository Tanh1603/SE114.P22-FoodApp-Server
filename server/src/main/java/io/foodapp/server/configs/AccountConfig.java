package io.foodapp.server.configs;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class AccountConfig implements CommandLineRunner {

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Value("${STAFF_EMAIL}")
    private String staffEmail;

    @Value("${STAFF_PASSWORD}")
    private String staffPassword;

    @Override
    public void run(String... args) throws Exception {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        createAccountIfNotExists(auth, adminEmail, adminPassword, "admin");
        createAccountIfNotExists(auth, staffEmail, staffPassword, "staff");

    }
        private void createAccountIfNotExists(FirebaseAuth auth, String email, String password, String role) throws FirebaseAuthException
        {
            try {
                auth.getUserByEmail(email);
            } catch (FirebaseAuthException e) {
                if (e.getAuthErrorCode() != null && e.getAuthErrorCode().name().equals("USER_NOT_FOUND")) {
                    UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                            .setEmail(email)
                            .setPassword(password);

                    String uid = auth.createUser(request).getUid();
                    Map<String, Object> claims = Map.of("role", role);
                    auth.setCustomUserClaims(uid, claims);

                    System.out.println("Created user: " + email + " with role: " + role);
                } else {
                    throw e;
                }
            }
        }
}

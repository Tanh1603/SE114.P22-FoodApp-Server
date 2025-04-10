package io.foodapp.server.configs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {
    // private static final String FIREBASE_PROJECT_ID =
    // EnvConfig.get("FIREBASE_PROJECT_ID");
    // private static final String FIREBASE_SERVICE_ACCOUNT_BASE64 =
    // EnvConfig.get("FIREBASE_SERVICE_ACCOUNT_BASE64");

    @Value("${FIREBASE_PROJECT_ID}")
    private String FIREBASE_PROJECT_ID;

    @Value("${FIREBASE_SERVICE_ACCOUNT_BASE64}")
    private String FIREBASE_SERVICE_ACCOUNT_BASE64;

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {

        byte[] decodedKey = Base64.getDecoder().decode(FIREBASE_SERVICE_ACCOUNT_BASE64);
        ByteArrayInputStream serviceAccount = new ByteArrayInputStream(decodedKey);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId(FIREBASE_PROJECT_ID)
                .build();
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }
}

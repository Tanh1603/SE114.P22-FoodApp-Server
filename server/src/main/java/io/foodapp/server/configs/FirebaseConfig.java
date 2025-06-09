package io.foodapp.server.configs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {

    private final Environment environment;

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {

        byte[] decodedKey = Base64.getDecoder().decode(environment.getProperty("FIREBASE_SERVICE_ACCOUNT_BASE64"));
        ByteArrayInputStream serviceAccount = new ByteArrayInputStream(decodedKey);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId(environment.getProperty("FIREBASE_PROJECT_ID"))
                .build();
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance(initializeFirebase());
    }

}

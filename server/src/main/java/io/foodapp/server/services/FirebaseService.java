package io.foodapp.server.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {
    public UserRecord createUser(String email, String password) throws Exception {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);
        return FirebaseAuth.getInstance().createUser(request);
    }

    public UserRecord getUserByEmail(String email) throws Exception {
        return FirebaseAuth.getInstance().getUserByEmail(email);
    }

    public FirebaseToken verifyIdToken(String idToken) throws Exception {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    public FirebaseToken authenticate(String idToken) throws Exception {
        return verifyIdToken(idToken);
    }
}

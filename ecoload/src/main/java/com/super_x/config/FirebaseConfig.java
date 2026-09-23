package com.super_x.config;

import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseConfig {

    static {
        getFirebaseConfig();
    }

    public static void getFirebaseConfig() {

        try {

            InputStream serviceAccount =
                    FirebaseConfig.class
                            .getClassLoader()
                            .getResourceAsStream("ecoload.json");

            if (serviceAccount == null) {
                System.out.println("ecoload.json not found in resources");
                return;
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(
                            GoogleCredentials.fromStream(serviceAccount)
                    )
                    .build();

            FirebaseApp.initializeApp(options);

            System.out.println("Firebase Config Initialized Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Firestore getFireStore() {
        return FirestoreClient.getFirestore();
    }
}
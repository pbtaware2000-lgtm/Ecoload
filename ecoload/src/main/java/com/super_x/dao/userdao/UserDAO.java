package com.super_x.dao.userdao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.super_x.config.FirebaseConfig;
import com.super_x.model.usermodel.UserModel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserDAO {

    private final Firestore db;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public UserDAO() {
        db = FirebaseConfig.getFireStore();
    }


    // =========================================================
    // SAVE USER
    // =========================================================

    public boolean saveUser(UserModel user) {

        if (user == null) {
            System.out.println("User is null.");
            return false;
        }

        if (user.getEmail() == null ||
                user.getEmail().trim().isEmpty()) {

            System.out.println("Email cannot be empty.");
            return false;
        }

        try {

            // Email is currently used as document ID
            String documentId =
                    user.getEmail()
                            .trim()
                            .toLowerCase();

            Map<String, Object> userData =
                    new HashMap<>();

            userData.put(
                    "username",
                    user.getUsername());

            userData.put(
                    "phone",
                    user.getPhone());

            userData.put(
                    "email",
                    user.getEmail());

            userData.put(
                    "gstNumber",
                    user.getGstNumber());

            userData.put(
                    "businessType",
                    user.getBusinessType());

            userData.put(
                    "businessLicenseNumber",
                    user.getBusinessLicenseNumber());

            userData.put(
                    "address",
                    user.getAddress());

            userData.put(
                    "city",
                    user.getCity());

            userData.put(
                    "state",
                    user.getState());

            userData.put(
                    "pinCode",
                    user.getPinCode());

            userData.put(
                    "businessDocumenturl",
                    user.getBusinessDocumenturl());

            userData.put(
                    "role",
                    user.getRole());

            userData.put(
                    "status",
                    user.getStatus());


            // =================================================
            // FIRESTORE
            // =================================================

            ApiFuture<WriteResult> future =
                    db.collection("users")
                            .document(documentId)
                            .set(userData);

            future.get();


            System.out.println(
                    "================================");

            System.out.println(
                    "USER SAVED SUCCESSFULLY");

            System.out.println(
                    "Firestore Collection : users");

            System.out.println(
                    "Document ID          : "
                            + documentId);

            System.out.println(
                    "================================");


            return true;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                    "Firestore operation interrupted.");

            e.printStackTrace();

            return false;

        } catch (ExecutionException e) {

            System.out.println(
                    "Error saving user to Firestore.");

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // GET USER BY EMAIL
    // =========================================================

    public UserModel getUserByEmail(String email) {

        if (email == null ||
                email.trim().isEmpty()) {

            return null;
        }

        try {

            String documentId =
                    email.trim().toLowerCase();

            ApiFuture<DocumentSnapshot> future =
                    db.collection("users")
                            .document(documentId)
                            .get();

            DocumentSnapshot document =
                    future.get();

            if (!document.exists()) {

                System.out.println(
                        "User not found: " + email);

                return null;
            }

            UserModel user =
                    document.toObject(UserModel.class);

            System.out.println(
                    "User retrieved successfully: "
                            + email);

            return user;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            e.printStackTrace();

            return null;

        } catch (ExecutionException e) {

            System.out.println(
                    "Error retrieving user.");

            e.printStackTrace();

            return null;
        }
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    public boolean updateUser(UserModel user) {

        if (user == null ||
                user.getEmail() == null ||
                user.getEmail().trim().isEmpty()) {

            return false;
        }

        try {

            String documentId =
                    user.getEmail()
                            .trim()
                            .toLowerCase();

            Map<String, Object> userData =
                    new HashMap<>();

            userData.put(
                    "username",
                    user.getUsername());

            userData.put(
                    "phone",
                    user.getPhone());

            userData.put(
                    "email",
                    user.getEmail());

            userData.put(
                    "gstNumber",
                    user.getGstNumber());

            userData.put(
                    "businessType",
                    user.getBusinessType());

            userData.put(
                    "businessLicenseNumber",
                    user.getBusinessLicenseNumber());

            userData.put(
                    "address",
                    user.getAddress());

            userData.put(
                    "city",
                    user.getCity());

            userData.put(
                    "state",
                    user.getState());

            userData.put(
                    "pinCode",
                    user.getPinCode());

            userData.put(
                    "businessDocumenturl",
                    user.getBusinessDocumenturl());

            userData.put(
                    "role",
                    user.getRole());

            userData.put(
                    "status",
                    user.getStatus());


            ApiFuture<WriteResult> future =
                    db.collection("users")
                            .document(documentId)
                            .set(userData);

            future.get();

            System.out.println(
                    "User updated successfully.");

            return true;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            e.printStackTrace();

            return false;

        } catch (ExecutionException e) {

            System.out.println(
                    "Error updating user.");

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    public boolean deleteUser(String email) {

        if (email == null ||
                email.trim().isEmpty()) {

            return false;
        }

        try {

            String documentId =
                    email.trim().toLowerCase();

            ApiFuture<WriteResult> future =
                    db.collection("users")
                            .document(documentId)
                            .delete();

            future.get();

            System.out.println(
                    "User deleted successfully.");

            return true;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            e.printStackTrace();

            return false;

        } catch (ExecutionException e) {

            System.out.println(
                    "Error deleting user.");

            e.printStackTrace();

            return false;
        }
    }
}
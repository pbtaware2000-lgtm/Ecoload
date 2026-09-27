package com.super_x.controller.usercontroller;

import com.super_x.dao.userdao.UserAuthDAO;
import com.super_x.dao.userdao.UserDAO;
import com.super_x.model.usermodel.UserModel;

public class UserLoginController {

    private final UserAuthDAO userAuthDAO;
    private final UserDAO userDAO;

    public UserLoginController() {

        userAuthDAO = new UserAuthDAO();
        userDAO = new UserDAO();
    }

    // =========================================================
    // LOGIN USER
    // =========================================================

    public UserModel loginUser(
            String email,
            String password) {

        // =====================================================
        // 1. VALIDATE INPUT
        // =====================================================

        if (email == null ||
                email.trim().isEmpty()) {

            System.out.println(
                    "Email cannot be empty.");

            return null;
        }

        if (password == null ||
                password.isEmpty()) {

            System.out.println(
                    "Password cannot be empty.");

            return null;
        }


        // =====================================================
        // 2. FIREBASE AUTHENTICATION
        // =====================================================

        long authStart = System.currentTimeMillis();

        System.out.println("========== USER LOGIN ==========");
        System.out.println("Starting Firebase Authentication...");

        boolean loginSuccessful = userAuthDAO.login(email, password);

        long authEnd = System.currentTimeMillis();

        System.out.println(
                "Firebase Authentication Time: "
                        + (authEnd - authStart)
                        + " ms");

        if (!loginSuccessful) {

            System.out.println(
                    "Firebase Authentication failed.");

            return null;
        }


        // =====================================================
        // 3. GET USER PROFILE FROM FIRESTORE
        // =====================================================

        long firestoreStart = System.currentTimeMillis();

        System.out.println(
                "Starting Firestore User Fetch...");

        UserModel user = userDAO.getUserByEmail(email);

        long firestoreEnd = System.currentTimeMillis();

        System.out.println(
                "Firestore User Fetch Time: "
                        + (firestoreEnd - firestoreStart)
                        + " ms"); 


        // =====================================================
        // 4. CHECK ACCOUNT STATUS
        // =====================================================

        if (user.getStatus() == null ||
                !user.getStatus()
                        .equalsIgnoreCase("ACTIVE")) {

            System.out.println(
                    "User account is not active.");

            return null;
        }


        // =====================================================
        // 5. LOGIN SUCCESS
        // =====================================================

        System.out.println(
                "================================");

        System.out.println(
                "LOGIN SUCCESSFUL");

        System.out.println(
                "Email: " +
                        user.getEmail());

        System.out.println(
                "Username: " +
                        user.getUsername());

        System.out.println(
                "Role: " +
                        user.getRole());

        System.out.println(
                "================================");


        return user;
    }
}
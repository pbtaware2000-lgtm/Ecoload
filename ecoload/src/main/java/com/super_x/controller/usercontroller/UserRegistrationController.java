package com.super_x.controller.usercontroller;

import com.super_x.dao.userdao.UserAuthDAO;
import com.super_x.dao.userdao.UserDAO;
import com.super_x.model.usermodel.UserModel;

public class UserRegistrationController {

    private final UserDAO userDAO;
    private final UserAuthDAO userAuthDAO;

    public UserRegistrationController() {

        userDAO = new UserDAO();
        userAuthDAO = new UserAuthDAO();
    }

    public UserModel createUser(
            String username,
            String phone,
            String email,
            String gstNumber,
            String businessType,
            String businessLicenseNumber,
            String address,
            String city,
            String state,
            String pinCode,
            String password) {

        // =====================================================
        // 1. CREATE FIREBASE AUTH ACCOUNT
        // =====================================================

        boolean accountCreated =
                userAuthDAO.createAccount(
                        email,
                        password);

        if (!accountCreated) {

            System.out.println(
                    "Firebase Authentication failed.");

            return null;
        }

        // =====================================================
        // 2. CREATE USER MODEL
        // =====================================================

        UserModel user = new UserModel();

        user.setUsername(username);
        user.setPhone(phone);
        user.setEmail(email);
        user.setGstNumber(gstNumber);
        user.setBusinessType(businessType);
        user.setBusinessLicenseNumber(
                businessLicenseNumber);
        user.setAddress(address);
        user.setCity(city);
        user.setState(state);
        user.setPinCode(pinCode);

        // =====================================================
        // 3. DEFAULT VALUES
        // =====================================================

        user.setRole("USER");
        user.setStatus("ACTIVE");

        return user;
    }

    // =========================================================
    // SAVE USER TO FIRESTORE
    // =========================================================

    public boolean saveUser(UserModel user) {

        return userDAO.saveUser(user);
    }
}
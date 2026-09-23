package com.super_x.controller.drivercontroller;

import com.super_x.dao.driverdao.DriverAuthDAO;
import com.super_x.dao.driverdao.DriverDAO;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.usermodel.UserModel;

public class DriverRegistrationController {

    private final DriverDAO driverDAO;
    private final DriverAuthDAO driverAuthDAO;

    public DriverRegistrationController(DriverDAO driverDAO) {

        this.driverDAO = driverDAO;
        this.driverAuthDAO = new DriverAuthDAO();
    }

    public boolean registerDriver(
            DriverModel driver,
            String password,
            String confirmPassword) throws Exception {

        // =====================================================
        // 1. VALIDATE PASSWORD
        // =====================================================

        if (password == null || password.isEmpty()) {
            throw new Exception("Password is required.");
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            throw new Exception("Please confirm your password.");
        }

        if (!password.equals(confirmPassword)) {
            throw new Exception("Passwords do not match.");
        }

        // =====================================================
        // 2. CHECK DUPLICATE DRIVER PROFILE
        // =====================================================

        DriverModel existing =
                driverDAO.getDriverByEmail(
                        driver.getEmail()
                );

        if (existing != null) {
            return false;
        }

        // =====================================================
        // 3. CREATE FIREBASE AUTHENTICATION ACCOUNT
        // =====================================================

        String firebaseUid =
                driverAuthDAO.registerDriver(
                        driver.getEmail(),
                        password
                );

        // =====================================================
        // 4. SET DRIVER INFORMATION
        // =====================================================

        driver.setRole("DRIVER");

        // Driver waits for admin approval
        driver.setStatus("PENDING");

        String now =
                java.time.Instant.now().toString();

        driver.setCreatedAt(now);
        driver.setUpdatedAt(now);

        // =====================================================
        // 5. SAVE DRIVER PROFILE TO FIRESTORE
        // =====================================================

        driverDAO.saveDriver(driver);

        // firebaseUid is available here if you later
        // add a uid field to DriverModel.
        System.out.println("Firebase Driver UID: " + firebaseUid);

        return true;
    }
}
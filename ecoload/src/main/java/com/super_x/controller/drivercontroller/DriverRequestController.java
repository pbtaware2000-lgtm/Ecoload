package com.super_x.controller.drivercontroller;

import com.super_x.dao.driverdao.DriverDAO;
import com.super_x.dao.driverdao.DriverRequestDAO;
import com.super_x.model.drivermodel.DriverRequestModel;

public class DriverRequestController {

    private final DriverRequestDAO requestDAO;
    private final DriverDAO driverDAO;

    public DriverRequestController(
            DriverRequestDAO requestDAO,
            DriverDAO driverDAO) {

        this.requestDAO = requestDAO;
        this.driverDAO = driverDAO;
    }


    // =====================================================
    // DRIVER SUBMITS REQUEST
    // =====================================================

    public void submitRequest(String email)
            throws Exception {

        // Check whether request already exists
        DriverRequestModel existing =
                requestDAO.getRequestByEmail(email);

        if (existing != null) {

            throw new Exception(
                    "A registration request already exists for this driver."
            );
        }


        // Create new request
        DriverRequestModel request =
                new DriverRequestModel();

        request.setDriverEmail(email);

        request.setStatus("PENDING");

        request.setSubmittedAt(
                java.time.Instant.now().toString()
        );


        // Save request
        requestDAO.createRequest(request);
    }


    // =====================================================
    // ADMIN APPROVES DRIVER
    // =====================================================

    public void approveRequest(
            String email,
            String reviewedBy)
            throws Exception {

        // 1. Update request
        requestDAO.updateStatus(
                email,
                "APPROVED",
                reviewedBy
        );


        // 2. Update driver
        driverDAO.updateDriverStatus(
                email,
                "APPROVED"
        );
    }


    // =====================================================
    // ADMIN REJECTS DRIVER
    // =====================================================

    public void rejectRequest(
            String email,
            String reviewedBy,
            String reason)
            throws Exception {

        // 1. Update request
        requestDAO.rejectRequest(
                email,
                reviewedBy,
                reason
        );


        // 2. Update driver
        driverDAO.updateDriverStatus(
                email,
                "REJECTED"
        );
    }
}
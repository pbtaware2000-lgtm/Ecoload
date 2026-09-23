package com.super_x.controller.drivercontroller;

import com.super_x.dao.driverdao.VehicleDAO;
import com.super_x.model.drivermodel.VehicleModel;

public class VehicleRegistrationController {

    private final VehicleDAO vehicleDAO;

    public VehicleRegistrationController(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    public boolean registerVehicle(VehicleModel vehicle)
            throws Exception {

        // =====================================================
        // 1. VALIDATE VEHICLE OBJECT
        // =====================================================

        if (vehicle == null) {
            System.out.println(
                    "Vehicle data cannot be null."
            );
            return false;
        }

        // =====================================================
        // 2. VALIDATE DRIVER EMAIL
        // =====================================================

        if (vehicle.getDriverEmail() == null ||
                vehicle.getDriverEmail()
                        .trim()
                        .isEmpty()) {

            System.out.println(
                    "Driver email cannot be empty."
            );
            return false;
        }

        // =====================================================
        // 3. VALIDATE VEHICLE CAPACITY
        // =====================================================

        if (vehicle.getVehicleCapacity() <= 0) {

            System.out.println(
                    "Vehicle capacity must be greater than 0."
            );

            return false;
        }

        // =====================================================
        // 4. CHECK EXISTING VEHICLE
        // =====================================================

        VehicleModel existing =
                vehicleDAO.getVehicleByDriverEmail(
                        vehicle.getDriverEmail()
                );

        if (existing != null) {

            System.out.println(
                    "Vehicle already registered for this driver."
            );

            return false;
        }

        // =====================================================
        // 5. SAVE VEHICLE
        // =====================================================

        vehicleDAO.saveVehicle(vehicle);

        System.out.println(
                "Vehicle registered successfully."
        );

        System.out.println(
                "Driver Email: "
                        + vehicle.getDriverEmail()
        );

        System.out.println(
                "Vehicle Type: "
                        + vehicle.getVehicleType()
        );

        System.out.println(
                "Vehicle Capacity: "
                        + vehicle.getVehicleCapacity()
                        + " Ton"
        );

        return true;
    }
}
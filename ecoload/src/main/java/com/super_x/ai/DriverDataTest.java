package com.super_x.ai;

import java.util.List;

import com.super_x.config.FirebaseConfig;
import com.super_x.dao.driverdao.DriverDAO;
import com.super_x.dao.driverdao.VehicleDAO;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.drivermodel.VehicleModel;

public class DriverDataTest {

    public static void main(String[] args) {

        try {

            // =====================================================
            // FIRESTORE
            // =====================================================

            var firestore =
                    FirebaseConfig.getFireStore();


            // =====================================================
            // DAO OBJECTS
            // =====================================================

            DriverDAO driverDAO =
                    new DriverDAO(firestore);

            VehicleDAO vehicleDAO =
                    new VehicleDAO(firestore);


            // =====================================================
            // GET ALL DRIVERS
            // =====================================================

            List<DriverModel> drivers =
                    driverDAO.getAllDrivers();


            System.out.println();

            System.out.println(
                    "===== ECOLOAD DRIVER + VEHICLE DATA ====="
            );

            System.out.println(
                    "Total Drivers: " + drivers.size()
            );

            System.out.println();


            // =====================================================
            // PROCESS APPROVED DRIVERS
            // =====================================================

            for (DriverModel driver : drivers) {

                // Only APPROVED drivers
                if (!"APPROVED".equalsIgnoreCase(
                        driver.getStatus())) {

                    continue;
                }


                // =================================================
                // GET VEHICLE USING DRIVER EMAIL
                // =================================================

                VehicleModel vehicle =
                        vehicleDAO.getVehicleByDriverEmail(
                                driver.getEmail()
                        );


                // =================================================
                // DRIVER INFORMATION
                // =================================================

                System.out.println(
                        "Driver Name: " +
                        driver.getUsername()
                );

                System.out.println(
                        "Email: " +
                        driver.getEmail()
                );

                System.out.println(
                        "Status: " +
                        driver.getStatus()
                );


                // =================================================
                // VEHICLE INFORMATION
                // =================================================

                if (vehicle != null) {

                    System.out.println(
                            "Vehicle Name: " +
                            vehicle.getVehicleName()
                    );

                    System.out.println(
                            "Vehicle Number: " +
                            vehicle.getVehiclePlateNumber()
                    );

                    System.out.println(
                            "Vehicle Type: " +
                            vehicle.getVehicleType()
                    );

                    System.out.println(
                            "Vehicle Capacity: " +
                            vehicle.getVehicleCapacity()
                    );

                    System.out.println(
                            "Fuel Type: " +
                            vehicle.getFuelType()
                    );

                } else {

                    System.out.println(
                            "Vehicle: Not Registered"
                    );
                }


                System.out.println(
                        "----------------------------------------"
                );
            }


            System.out.println();

            System.out.println(
                    "=========================================="
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to load driver and vehicle data."
            );

            e.printStackTrace();
        }
    }
}
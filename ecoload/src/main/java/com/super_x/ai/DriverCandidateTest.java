package com.super_x.ai;

import java.util.ArrayList;
import java.util.List;

import com.super_x.config.FirebaseConfig;
import com.super_x.dao.driverdao.DriverDAO;
import com.super_x.dao.driverdao.VehicleDAO;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.drivermodel.VehicleModel;

public class DriverCandidateTest {

    public static void main(String[] args) {

        try {

            // Get Firestore
            var firestore =
                    FirebaseConfig.getFireStore();

            // DAO
            DriverDAO driverDAO =
                    new DriverDAO(firestore);

            VehicleDAO vehicleDAO =
                    new VehicleDAO(firestore);

            // Get all drivers
            List<DriverModel> drivers =
                    driverDAO.getAllDrivers();

            // Candidate list
            List<DriverCandidate> candidates =
                    new ArrayList<>();

            System.out.println();
            System.out.println(
                    "===== ECOLOAD AI DRIVER CANDIDATES ====="
            );

            // Process drivers
            for (DriverModel driver : drivers) {

                // Only approved drivers
                if (!"APPROVED".equalsIgnoreCase(
                        driver.getStatus())) {

                    continue;
                }

                // Get vehicle
                VehicleModel vehicle =
                        vehicleDAO.getVehicleByDriverEmail(
                                driver.getEmail()
                        );

                // Skip driver without vehicle
                if (vehicle == null) {

                    System.out.println(
                            "Skipping " +
                            driver.getUsername() +
                            " - Vehicle not registered."
                    );

                    continue;
                }

                // Create candidate
                DriverCandidate candidate =
                        new DriverCandidate(
                                driver.getUsername(),
                                driver.getEmail(),
                                driver.getStatus(),
                                vehicle.getVehicleName(),
                                vehicle.getVehicleType(),
                                vehicle.getVehicleCapacity(),
                                vehicle.getFuelType()
                        );

                candidates.add(candidate);
            }

            // Display candidates
            System.out.println();

            System.out.println(
                    "Total AI Candidates: " +
                    candidates.size()
            );

            System.out.println();

            for (DriverCandidate candidate :
                    candidates) {

                System.out.println(
                        "Driver Name: " +
                        candidate.getDriverName()
                );

                System.out.println(
                        "Email: " +
                        candidate.getDriverEmail()
                );

                System.out.println(
                        "Vehicle: " +
                        candidate.getVehicleName()
                );

                System.out.println(
                        "Vehicle Type: " +
                        candidate.getVehicleType()
                );

                System.out.println(
                        "Capacity: " +
                        candidate.getVehicleCapacity() +
                        " Ton"
                );

                System.out.println(
                        "Fuel Type: " +
                        candidate.getFuelType()
                );

                System.out.println(
                        "----------------------------------------"
                );
            }

            System.out.println(
                    "=========================================="
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to create AI driver candidates."
            );

            e.printStackTrace();
        }
    }
}
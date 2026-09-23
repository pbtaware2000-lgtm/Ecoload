package com.super_x.ai;

import java.util.List;

import com.super_x.config.FirebaseConfig;
import com.super_x.dao.driverdao.DriverDAO;
import com.super_x.dao.driverdao.VehicleDAO;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.drivermodel.VehicleModel;
import com.super_x.model.usermodel.Load;

public class AIRecommendationTest {

    public static void main(String[] args) {

        try {

            // =====================================================
            // FIRESTORE
            // =====================================================

            var firestore =
                    FirebaseConfig.getFireStore();


            // =====================================================
            // DAO
            // =====================================================

            DriverDAO driverDAO =
                    new DriverDAO(firestore);

            VehicleDAO vehicleDAO =
                    new VehicleDAO(firestore);


            // =====================================================
            // TEST LOAD
            // =====================================================

            Load load = new Load();

            load.setLoadId("AI-TEST-001");
            load.setPickupLocation("Pune");
            load.setDestination("Mumbai");
            load.setLoadType("General");
            load.setWeight(8.0);
            load.setWeightUnit("Ton");
            load.setTruckType("Truck");
            load.setOfferPrice(15000);


            // =====================================================
            // GET APPROVED DRIVERS
            // =====================================================

            List<DriverModel> drivers =
                    driverDAO.getAllDrivers();


            StringBuilder candidates =
                    new StringBuilder();


            int candidateNumber = 1;


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


                // Skip if vehicle not registered
                if (vehicle == null) {
                    continue;
                }


                // Add candidate information
                candidates.append(
                        "\nDriver ")
                        .append(candidateNumber)
                        .append(":\n");

                candidates.append(
                        "Name: ")
                        .append(driver.getUsername())
                        .append("\n");

                candidates.append(
                        "Email: ")
                        .append(driver.getEmail())
                        .append("\n");

                candidates.append(
                        "Status: ")
                        .append(driver.getStatus())
                        .append("\n");

                candidates.append(
                        "Vehicle: ")
                        .append(vehicle.getVehicleName())
                        .append("\n");

                candidates.append(
                        "Vehicle Type: ")
                        .append(vehicle.getVehicleType())
                        .append("\n");

                candidates.append(
                        "Vehicle Capacity: ")
                        .append(vehicle.getVehicleCapacity())
                        .append(" Ton\n");

                candidates.append(
                        "Fuel Type: ")
                        .append(vehicle.getFuelType())
                        .append("\n");

                candidates.append(
                        "------------------------------\n");


                candidateNumber++;
            }


            // =====================================================
            // BUILD GROQ PROMPT
            // =====================================================

            String prompt =
                    """
                    You are the AI driver recommendation
                    assistant for EcoLoad.

                    Analyze the load and approved driver
                    candidates.

                    LOAD INFORMATION:
                    Load ID: %s
                    Pickup: %s
                    Destination: %s
                    Load Type: %s
                    Weight: %.2f %s
                    Required Truck Type: %s
                    Offer Price: %.2f

                    APPROVED DRIVER CANDIDATES:
                    %s

                    RECOMMENDATION RULES:

                    1. Vehicle capacity must be greater than
                       or equal to the load weight.

                    2. Prefer the vehicle type that best
                       matches the required truck type.

                    3. Only APPROVED drivers can be recommended.

                    4. Do not recommend a driver whose vehicle
                       cannot carry the load.

                    5. Do not invent missing information.

                    Give the TOP 3 suitable drivers.

                    For each recommendation provide:
                    - Rank
                    - Driver Name
                    - Driver Email
                    - Vehicle
                    - Vehicle Capacity
                    - Reason

                    If fewer than 3 drivers are suitable,
                    return only the suitable drivers.
                    """.formatted(
                            load.getLoadId(),
                            load.getPickupLocation(),
                            load.getDestination(),
                            load.getLoadType(),
                            load.getWeight(),
                            load.getWeightUnit(),
                            load.getTruckType(),
                            load.getOfferPrice(),
                            candidates.toString()
                    );


            // =====================================================
            // CALL GROQ AI
            // =====================================================

            GrokAIService grokAIService =
                    new GrokAIService();

            String response =
                    grokAIService.askGrok(prompt);


            // =====================================================
            // DISPLAY RESULT
            // =====================================================

            System.out.println();

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "       ECOLOAD AI RECOMMENDATION"
            );

            System.out.println(
                    "=========================================="
            );

            System.out.println(response);

            System.out.println(
                    "=========================================="
            );


        } catch (Exception e) {

            System.err.println(
                    "EcoLoad AI recommendation failed."
            );

            e.printStackTrace();
        }
    }
}
package com.super_x.ai;

import java.util.List;

import com.google.cloud.firestore.Firestore;
import com.super_x.config.FirebaseConfig;
import com.super_x.dao.driverdao.DriverDAO;
import com.super_x.dao.driverdao.VehicleDAO;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.drivermodel.VehicleModel;
import com.super_x.model.usermodel.Load;

public class AIRecommendationService {

    private final DriverDAO driverDAO;
    private final VehicleDAO vehicleDAO;
    private final GrokAIService grokAIService;

    public AIRecommendationService() {

        Firestore firestore =
                FirebaseConfig.getFireStore();

        this.driverDAO =
                new DriverDAO(firestore);

        this.vehicleDAO =
                new VehicleDAO(firestore);

        this.grokAIService =
                new GrokAIService();
    }

    // =========================================================
    // GET AI DRIVER RECOMMENDATION
    // =========================================================

    public String recommendDrivers(Load load)
            throws Exception {

        if (load == null) {

            throw new IllegalArgumentException(
                    "Load cannot be null."
            );
        }

        // =====================================================
        // NORMALIZE LOAD WEIGHT TO TON
        // =====================================================

        double loadWeightTon =
                convertToTons(
                        load.getWeight(),
                        load.getWeightUnit()
                );

        // =====================================================
        // GET ALL DRIVERS
        // =====================================================

        List<DriverModel> drivers =
                driverDAO.getAllDrivers();

        StringBuilder candidates =
                new StringBuilder();

        int candidateCount = 0;

        // =====================================================
        // BUILD APPROVED DRIVER + VEHICLE DATA
        // =====================================================

        for (DriverModel driver : drivers) {

            // Only approved drivers
            if (!"APPROVED".equalsIgnoreCase(
                    driver.getStatus())) {

                continue;
            }

            // Get vehicle using driver email
            VehicleModel vehicle =
                    vehicleDAO.getVehicleByDriverEmail(
                            driver.getEmail()
                    );

            // Driver without vehicle
            if (vehicle == null) {

                continue;
            }

            candidateCount++;

            candidates
                    .append("\nDriver ")
                    .append(candidateCount)
                    .append(":\n");

            candidates
                    .append("Name: ")
                    .append(driver.getUsername())
                    .append("\n");

            candidates
                    .append("Email: ")
                    .append(driver.getEmail())
                    .append("\n");

            candidates
                    .append("Status: ")
                    .append(driver.getStatus())
                    .append("\n");

            candidates
                    .append("Vehicle Name: ")
                    .append(vehicle.getVehicleName())
                    .append("\n");

            candidates
                    .append("Vehicle Number: ")
                    .append(vehicle.getVehiclePlateNumber())
                    .append("\n");

            candidates
                    .append("Vehicle Type: ")
                    .append(vehicle.getVehicleType())
                    .append("\n");

            candidates
                    .append("Vehicle Capacity: ")
                    .append(vehicle.getVehicleCapacity())
                    .append(" Ton\n");

            candidates
                    .append("Fuel Type: ")
                    .append(vehicle.getFuelType())
                    .append("\n");

            candidates
                    .append("--------------------------------\n");
        }

        // =====================================================
        // NO DRIVER AVAILABLE
        // =====================================================

        if (candidateCount == 0) {

            return "No approved drivers with registered vehicles are available.";
        }

        // =====================================================
        // BUILD AI PROMPT
        // =====================================================

        String prompt = """
                You are EcoLoad AI, an intelligent
                transport driver recommendation system.

                Analyze the load and approved driver
                candidates provided below.

                ================================
                LOAD INFORMATION
                ================================

                Load ID: %s
                Pickup Location: %s
                Destination: %s
                Load Type: %s
                Load Weight: %.2f Ton
                Required Truck Type: %s
                Offer Price: %.2f
                Pickup Date: %s
                Pickup Time: %s
                Delivery Date: %s
                Delivery Time: %s

                ================================
                APPROVED DRIVER CANDIDATES
                ================================

                %s

                ================================
                RECOMMENDATION RULES
                ================================

                1. Only APPROVED drivers can be recommended.

                2. The vehicle capacity must be greater than
                   or equal to the load weight.

                3. Prefer vehicles whose type best matches
                   the required truck type.

                4. Do not recommend a vehicle that cannot
                   carry the load.

                5. Do not invent driver or vehicle information.

                6. Consider vehicle capacity, vehicle type,
                   fuel type and overall suitability.

                7. Return maximum 3 suitable drivers.

                8. If fewer than 3 drivers are suitable,
                   return only the suitable drivers.

                ================================
                RESPONSE FORMAT
                ================================

                Rank 1:
                Driver Name:
                Driver Email:
                Vehicle:
                Vehicle Type:
                Vehicle Capacity:
                Reason:

                Rank 2:
                Driver Name:
                Driver Email:
                Vehicle:
                Vehicle Type:
                Vehicle Capacity:
                Reason:

                Rank 3:
                Driver Name:
                Driver Email:
                Vehicle:
                Vehicle Type:
                Vehicle Capacity:
                Reason:

                If no driver is suitable, clearly state:
                "No suitable driver found."
                """.formatted(
                        load.getLoadId(),
                        load.getPickupLocation(),
                        load.getDestination(),
                        load.getLoadType(),
                        loadWeightTon,
                        load.getTruckType(),
                        load.getOfferPrice(),
                        load.getPickupDate(),
                        load.getPickupTime(),
                        load.getDeliveryDate(),
                        load.getDeliveryTime(),
                        candidates.toString()
                );

        // =====================================================
        // CALL GROQ AI
        // =====================================================

        return grokAIService.askGrok(prompt);
    }

    // =========================================================
    // WEIGHT CONVERSION
    // =========================================================

    private double convertToTons(
            double weight,
            String weightUnit) {

        if (weightUnit == null ||
                weightUnit.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Weight unit cannot be empty."
            );
        }

        String unit =
                weightUnit.trim().toLowerCase();

        switch (unit) {

            case "kg":
            case "kgs":
            case "kilogram":
            case "kilograms":

                return weight / 1000.0;

            case "ton":
            case "tons":
            case "tonne":
            case "tonnes":

                return weight;

            default:

                throw new IllegalArgumentException(
                        "Unsupported weight unit: "
                                + weightUnit
                );
        }
    }
}

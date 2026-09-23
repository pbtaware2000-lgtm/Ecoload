package com.super_x.ai;

import com.super_x.dao.userdao.LoadDao;
import com.super_x.model.drivermodel.VehicleModel;
import com.super_x.model.usermodel.Load;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReturnLoadRecommendationService {

    private final LoadDao loadDao;

    public ReturnLoadRecommendationService() {
        this.loadDao = new LoadDao();
    }

    // =========================================================
    // FIND RETURN LOADS
    // Returns ALL suitable return loads
    // =========================================================

    public List<Load> findReturnLoads(
            String currentLocation,
            String previousPickupLocation,
            VehicleModel vehicle) {

        List<Load> candidates = new ArrayList<>();

        try {

            if (currentLocation == null ||
                    currentLocation.trim().isEmpty()) {

                return candidates;
            }

            if (vehicle == null) {

                return candidates;
            }

            String current =
                    currentLocation.trim();

            String previousPickup =
                    previousPickupLocation == null
                            ? ""
                            : previousPickupLocation.trim();

            double vehicleCapacity =
                    vehicle.getVehicleCapacity();

            System.out.println();
            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "      ECOLOAD AI RETURN LOAD"
            );

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "Current Location: "
                            + current
            );

            System.out.println(
                    "Previous Pickup: "
                            + previousPickup
            );

            System.out.println(
                    "Vehicle Capacity: "
                            + vehicleCapacity
                            + " Ton"
            );

            // =====================================================
            // FETCH ALL LOADS
            // =====================================================

            List<Load> allLoads =
                    loadDao.fetchAllLoads();

            // =====================================================
            // FIND ALL SUITABLE RETURN LOADS
            // =====================================================

            for (Load load : allLoads) {

                if (load == null) {
                    continue;
                }

                // -------------------------------------------------
                // ONLY PENDING LOADS
                // -------------------------------------------------

                if (load.getStatus() == null ||
                        !load.getStatus()
                                .equalsIgnoreCase("PENDING")) {

                    continue;
                }

                // -------------------------------------------------
                // LOAD MUST START FROM CURRENT LOCATION
                // -------------------------------------------------

                if (load.getPickupLocation() == null ||
                        !load.getPickupLocation()
                                .trim()
                                .equalsIgnoreCase(current)) {

                    continue;
                }

                // -------------------------------------------------
                // DRIVER SHOULD NOT PICK ASSIGNED LOAD
                // -------------------------------------------------

                if (load.getDriverId() != null &&
                        !load.getDriverId()
                                .trim()
                                .isEmpty()) {

                    continue;
                }

                // -------------------------------------------------
                // CAPACITY CHECK
                // -------------------------------------------------

                if (load.getWeight() >
                        vehicleCapacity) {

                    continue;
                }

                candidates.add(load);
            }

            System.out.println(
                    "Return Load Candidates: "
                            + candidates.size()
            );

            // =====================================================
            // NO RETURN LOAD
            // =====================================================

            if (candidates.isEmpty()) {

                System.out.println(
                        "No suitable return load found."
                );

                System.out.println(
                        "======================================"
                );

                return candidates;
            }

            // =====================================================
            // RANK BY OFFER PRICE
            // Highest offer first
            // =====================================================

            candidates.sort(
                    Comparator
                            .comparingDouble(
                                    Load::getOfferPrice
                            )
                            .reversed()
            );

            // =====================================================
            // PRINT ALL RECOMMENDED RETURN LOADS
            // =====================================================

            System.out.println();
            System.out.println(
                    "===== AI RETURN LOAD RECOMMENDATIONS ====="
            );

            int rank = 1;

            for (Load load : candidates) {

                System.out.println();
                System.out.println(
                        "Rank "
                                + rank
                                + ":"
                );

                System.out.println(
                        "Load ID: "
                                + load.getLoadId()
                );

                System.out.println(
                        "Route: "
                                + load.getPickupLocation()
                                + " -> "
                                + load.getDestination()
                );

                System.out.println(
                        "Load Type: "
                                + load.getLoadType()
                );

                System.out.println(
                        "Weight: "
                                + load.getWeight()
                                + " "
                                + load.getWeightUnit()
                );

                System.out.println(
                        "Offer Price: ₹"
                                + load.getOfferPrice()
                );

                rank++;
            }

            System.out.println();
            System.out.println(
                    "======================================"
            );

            return candidates;

        } catch (Exception e) {

            System.err.println(
                    "Return load recommendation failed."
            );

            e.printStackTrace();

            return candidates;
        }
    }

    // =========================================================
    // BACKWARD COMPATIBILITY METHOD
    // Returns BEST ONE RETURN LOAD
    // =========================================================

    public Load findBestReturnLoad(
            String currentLocation,
            String previousPickupLocation,
            VehicleModel vehicle) {

        List<Load> candidates =
                findReturnLoads(
                        currentLocation,
                        previousPickupLocation,
                        vehicle
                );

        if (candidates.isEmpty()) {

            return null;
        }

        return candidates.get(0);
    }

    // =========================================================
    // CAPACITY PARSER
    // =========================================================

    private double parseCapacity(
            String capacity) {

        if (capacity == null ||
                capacity.trim().isEmpty()) {

            return 0;
        }

        try {

            String value =
                    capacity
                            .trim()
                            .toLowerCase()
                            .replace("tons", "")
                            .replace("ton", "")
                            .trim();

            return Double.parseDouble(value);

        } catch (Exception e) {

            System.err.println(
                    "Invalid vehicle capacity: "
                            + capacity
            );

            return 0;
        }
    }
}
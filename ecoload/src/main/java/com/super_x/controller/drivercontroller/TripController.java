package com.super_x.controller.drivercontroller;

import com.super_x.dao.driverdao.TripDAO;
import com.super_x.model.drivermodel.Trip;
import com.super_x.model.drivermodel.TripStatus;
import com.super_x.model.drivermodel.ActiveTripManager;

public class TripController {

    private final TripDAO tripDAO =
            new TripDAO();

    // =========================================================
    // CREATE OR ADD LOAD TO TRIP
    // =========================================================

    public void createOrAddTrip(
            String loadId,
            String userId,
            String driverId,
            String driverName,
            String pickupLocation,
            String destination,
            double totalCapacity,
            double loadWeight
    ) throws Exception {

        // =====================================================
        // FIND EXISTING ACTIVE TRIP
        // =====================================================

        Trip existingTrip =
                tripDAO.getActiveTripByDriverId(
                        driverId
                );

        // =====================================================
        // EXISTING ACTIVE TRIP FOUND
        // =====================================================

        if (existingTrip != null) {

            double availableCapacity =
                    existingTrip.getTotalCapacity()
                            - existingTrip.getUsedCapacity();

            System.out.println(
                    "Existing Active Trip Found: "
                            + existingTrip.getTripId()
            );

            System.out.println(
                    "Truck Capacity: "
                            + existingTrip.getTotalCapacity()
                            + " Ton"
            );

            System.out.println(
                    "Current Used Capacity: "
                            + existingTrip.getUsedCapacity()
                            + " Ton"
            );

            System.out.println(
                    "Available Capacity: "
                            + availableCapacity
                            + " Ton"
            );

            // =================================================
            // CAPACITY CHECK
            // =================================================

            if (loadWeight > availableCapacity) {

                throw new Exception(
                        "Load cannot be accepted. "
                                + "Required: "
                                + loadWeight
                                + " Ton, Available: "
                                + availableCapacity
                                + " Ton"
                );
            }

            // =================================================
            // ADD LOAD TO EXISTING TRIP
            // =================================================

            tripDAO.addLoadToExistingTrip(
                    existingTrip,
                    loadId,
                    loadWeight,
                    pickupLocation
            );

            return;
        }

        // =====================================================
        // NO ACTIVE TRIP → CREATE NEW TRIP
        // =====================================================

        Trip trip = new Trip();

        trip.setTripId(
                "TRIP-"
                        + System.currentTimeMillis()
        );

        trip.setLoadId(loadId);

        trip.setLoadIds(
                new java.util.ArrayList<>(
                        java.util.List.of(loadId)
                )
        );

        trip.setUserId(userId);
        trip.setDriverId(driverId);
        trip.setDriverName(driverName);

        trip.setPickupLocation(
                pickupLocation
        );

        trip.setDestination(
                destination
        );

        trip.setTotalCapacity(
                totalCapacity
        );

        trip.setUsedCapacity(
                loadWeight
        );

        trip.setDistanceKm(0);

        trip.setEta(
                "Calculating..."
        );

        trip.setStatus(
                TripStatus.PICKUP.firestoreValue()
        );

        tripDAO.saveTrip(trip);
        ActiveTripManager.getInstance().setActiveTrip(trip);
    }
}

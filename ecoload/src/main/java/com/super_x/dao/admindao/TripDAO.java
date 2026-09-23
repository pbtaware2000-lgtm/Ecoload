package com.super_x.dao.admindao;

import com.super_x.config.FirebaseConfig;
import com.super_x.model.adminmodel.Trip;
import com.super_x.model.drivermodel.TripStatus;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.Timestamp;

import java.util.ArrayList;
import java.time.Instant;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class TripDAO {

    private final Firestore db;

    public TripDAO() {
        db = FirebaseConfig.getFireStore();
    }


    // =========================================================
    // GET ALL TRIPS
    // =========================================================

    public List<Trip> getAllTrips() {

        List<Trip> trips = new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection("trips")
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Trip trip =
                        document.toObject(Trip.class);

                if (trip != null) {

                    if (trip.getTripId() == null ||
                            trip.getTripId().isEmpty()) {

                        trip.setTripId(
                                document.getId()
                        );
                    }

                    trips.add(trip);

                    System.out.println(
                            "Trip fetched: "
                                    + trip.getTripId()
                                    + " | Load: "
                                    + trip.getLoadId()
                                    + " | Driver: "
                                    + trip.getDriverName()
                                    + " | "
                                    + trip.getPickupLocation()
                                    + " -> "
                                    + trip.getDestination()
                                    + " | Status: "
                                    + trip.getStatus()
                    );
                }
            }

            System.out.println(
                    "Total trips fetched: "
                            + trips.size()
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to fetch trips from Firebase."
            );

            e.printStackTrace();
        }

        return trips;
    }

    /**
     * Returns the admin analytics status totals from the real {@code trips}
     * collection.  The optional start time is compared with Firestore's
     * server-managed document update time so this works for both current and
     * legacy trip documents without requiring another date field.
     */
    public TripStatusDistribution getStatusDistribution(Instant startTime) {

        int active = 0;
        int inTransit = 0;
        int delivered = 0;
        int unanswered = 0;

        try {
            QuerySnapshot snapshot = db.collection("trips").get().get();

            for (DocumentSnapshot document : snapshot.getDocuments()) {
                Timestamp updateTime = document.getUpdateTime();
                if (startTime != null && (updateTime == null || Instant.ofEpochSecond(
                        updateTime.getSeconds(), updateTime.getNanos()).isBefore(startTime))) {
                    continue;
                }

                switch (statusBucket(document.getString("status"))) {
                    case ACTIVE -> active++;
                    case IN_TRANSIT -> inTransit++;
                    case DELIVERED -> delivered++;
                    case UNANSWERED -> unanswered++;
                }
            }
        } catch (Exception error) {
            System.err.println("Failed to fetch trip status distribution from Firebase: "
                    + error.getMessage());
            return TripStatusDistribution.empty();
        }

        return new TripStatusDistribution(active, inTransit, delivered, unanswered);
    }

    private StatusBucket statusBucket(String status) {
        if (status == null || status.isBlank()) {
            return StatusBucket.UNANSWERED;
        }

        String normalized = status.trim().toUpperCase().replace(' ', '_');
        // Keep unknown and cancelled values visible without pretending they
        // are part of an in-progress or delivered trip lifecycle.
        if ("CANCELLED".equals(normalized) || !isKnownTripStatus(normalized)) {
            return StatusBucket.UNANSWERED;
        }

        return switch (TripStatus.fromFirestore(status)) {
            case IN_TRANSIT -> StatusBucket.IN_TRANSIT;
            case DELIVERED -> StatusBucket.DELIVERED;
            // PICKUP, DISPATCH and ARRIVED are still active journeys.
            case PICKUP, DISPATCH, ARRIVED -> StatusBucket.ACTIVE;
        };
    }

    private boolean isKnownTripStatus(String status) {
        return switch (status) {
            case "ACTIVE", "ACCEPTED", "PICKUP", "PICKUP_COMPLETED", "DISPATCH",
                    "IN_TRANSIT", "DELIVERY", "ARRIVED", "DELIVERED", "COMPLETED" -> true;
            default -> false;
        };
    }

    private enum StatusBucket {
        ACTIVE, IN_TRANSIT, DELIVERED, UNANSWERED
    }

    public static final class TripStatusDistribution {
        private final int active;
        private final int inTransit;
        private final int delivered;
        private final int unanswered;

        public TripStatusDistribution(int active, int inTransit, int delivered, int unanswered) {
            this.active = active;
            this.inTransit = inTransit;
            this.delivered = delivered;
            this.unanswered = unanswered;
        }

        public static TripStatusDistribution empty() {
            return new TripStatusDistribution(0, 0, 0, 0);
        }

        public int getActive() { return active; }
        public int getInTransit() { return inTransit; }
        public int getDelivered() { return delivered; }
        public int getUnanswered() { return unanswered; }
        public int getTotal() { return active + inTransit + delivered + unanswered; }
    }


    // =========================================================
    // GET ACTIVE TRIPS
    // =========================================================

    public List<Trip> getActiveTrips() {

        List<Trip> trips = new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection("trips")
                            .whereEqualTo(
                                    "status",
                                    "ACTIVE"
                            )
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Trip trip =
                        document.toObject(Trip.class);

                if (trip != null) {

                    if (trip.getTripId() == null ||
                            trip.getTripId().isEmpty()) {

                        trip.setTripId(
                                document.getId()
                        );
                    }

                    trips.add(trip);
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Failed to fetch active trips."
            );

            e.printStackTrace();
        }

        return trips;
    }


    // =========================================================
    // GET COMPLETED TRIPS
    // =========================================================

    public List<Trip> getCompletedTrips() {

        List<Trip> trips = new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection("trips")
                            .whereEqualTo(
                                    "status",
                                    "COMPLETED"
                            )
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Trip trip =
                        document.toObject(Trip.class);

                if (trip != null) {

                    if (trip.getTripId() == null ||
                            trip.getTripId().isEmpty()) {

                        trip.setTripId(
                                document.getId()
                        );
                    }

                    trips.add(trip);
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Failed to fetch completed trips."
            );

            e.printStackTrace();
        }

        return trips;
    }
    // =========================================================
// GET IN-TRANSIT TRIP COUNT
// =========================================================

public int getInTransitTripsCount() {

    int count = 0;

    try {

        QuerySnapshot snapshot =
                db.collection("trips")
                        .get()
                        .get();

        for (DocumentSnapshot document :
                snapshot.getDocuments()) {

            String status =
                    document.getString("status");

            if (status == null || status.isBlank()) {
                continue;
            }

            try {

                TripStatus tripStatus =
                        TripStatus.fromFirestore(status);

                if (tripStatus == TripStatus.IN_TRANSIT) {
                    count++;
                }

            } catch (Exception ignored) {
                // Ignore invalid/unknown status values
            }
        }

    } catch (Exception e) {

        System.err.println(
                "Failed to fetch in-transit trip count."
        );

        e.printStackTrace();
    }

    return count;
}


// =========================================================
// GET UNIQUE IN-TRANSIT ROUTE COUNT
// =========================================================

public int getInTransitRoutesCount() {

    Set<String> routes =
            new HashSet<>();

    try {

        QuerySnapshot snapshot =
                db.collection("trips")
                        .get()
                        .get();

        for (DocumentSnapshot document :
                snapshot.getDocuments()) {

            String status =
                    document.getString("status");

            if (status == null || status.isBlank()) {
                continue;
            }

            try {

                TripStatus tripStatus =
                        TripStatus.fromFirestore(status);

                if (tripStatus == TripStatus.IN_TRANSIT) {

                    String pickup =
                            document.getString(
                                    "pickupLocation"
                            );

                    String destination =
                            document.getString(
                                    "destination"
                            );

                    if (pickup != null &&
                            destination != null &&
                            !pickup.isBlank() &&
                            !destination.isBlank()) {

                        String route =
                                pickup.trim()
                                        + " → "
                                        + destination.trim();

                        routes.add(route);
                    }
                }

            } catch (Exception ignored) {
                // Ignore invalid/unknown status values
            }
        }

    } catch (Exception e) {

        System.err.println(
                "Failed to fetch in-transit route count."
        );

        e.printStackTrace();
    }

    return routes.size();
}
}

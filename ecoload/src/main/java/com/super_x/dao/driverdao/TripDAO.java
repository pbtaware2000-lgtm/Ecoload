package com.super_x.dao.driverdao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteBatch;
import com.super_x.config.FirebaseConfig;
import com.super_x.model.drivermodel.Trip;
import com.super_x.model.drivermodel.TripStatus;

public class TripDAO {

    private final Firestore db =
            FirebaseConfig.getFireStore();

    // =========================================================
    // SAVE NEW TRIP
    // =========================================================

    public void saveTrip(
            Trip trip
    ) throws Exception {

        db.collection("trips")
                .document(trip.getTripId())
                .set(trip)
                .get();

        System.out.println(
                "Trip created successfully: "
                        + trip.getTripId()
        );
    }

    // =========================================================
    // FIND ACTIVE TRIP FOR DRIVER
    // =========================================================
    /**
     * Returns the driver's latest successfully finished trip.  Both legacy
     * COMPLETED and current DELIVERED statuses represent a completed journey.
     */
    public Trip getLatestSuccessfulTripByDriverId(
        String driverId) throws Exception {

    if (driverId == null || driverId.isBlank()) return null;

    Trip latestTrip = null;
    LocalDateTime latestActivityAt = null;
    for (QueryDocumentSnapshot document : db.collection("trips")
            .whereEqualTo("driverId", driverId.trim()).get().get().getDocuments()) {
        Trip trip = document.toObject(Trip.class);
        if (trip == null || TripStatus.fromFirestore(trip.getStatus()) != TripStatus.DELIVERED) continue;
        // completedTime is the app's delivery timestamp.  Older delivered
        // records can lack it, so use Firestore's server-managed update time
        // rather than unspecified query iteration order.
        LocalDateTime activityAt = parseCompletedTime(trip.getCompletedTime());
        if (activityAt == null) {
            activityAt = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(document.getUpdateTime().getSeconds(), document.getUpdateTime().getNanos()),
                    ZoneId.systemDefault());
        }
        if (latestActivityAt == null || activityAt.isAfter(latestActivityAt)) {
            latestTrip = trip;
            latestActivityAt = activityAt;
        }
    }
    return latestTrip;
}

    private LocalDateTime parseCompletedTime(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDateTime.parse(value.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException ignored) {
            try {
                return LocalDateTime.parse(value.trim());
            } catch (DateTimeParseException ignoredAgain) {
                return null;
            }
        }
    }

    /** Finds the user's currently accepted or progressing trip in `trips`. */
    public Trip getActiveTripByUserId(String userId) throws Exception {
        Trip latestTrip = null;
        QueryDocumentSnapshot latestDocument = null;
        for (QueryDocumentSnapshot document : db.collection("trips")
                .whereEqualTo("userId", userId).get().get().getDocuments()) {
            Trip trip = document.toObject(Trip.class);
            if (trip != null && isActiveStatus(trip.getStatus())) {
                // A user can have historical completed records. Select the
                // most recently updated active record, never query order.
                if (latestDocument == null
                        || document.getUpdateTime().compareTo(latestDocument.getUpdateTime()) > 0) {
                    latestTrip = trip;
                    latestDocument = document;
                }
            }
        }
        return latestTrip;
    }

    /** One-shot user-scoped trip snapshot for read-only user analytics. */
    public List<Trip> getTripsByUserId(String userId) {
        List<Trip> trips = new ArrayList<>();
        if (userId == null || userId.isBlank()) return trips;
        try {
            for (QueryDocumentSnapshot document : db.collection("trips")
                    .whereEqualTo("userId", userId).get().get().getDocuments()) {
                Trip trip = document.toObject(Trip.class);
                if (trip != null) trips.add(trip);
            }
        } catch (Exception error) {
            System.err.println("Unable to load user trips for analytics: " + error.getMessage());
        }
        return trips;
    }

    /**
     * Resolves a user's trips from both supported relationships: the trip's
     * userId and its loadId/loadIds references. This includes legacy trip
     * documents where userId was not saved, without exposing another user's
     * load because every relationship is checked against the supplied IDs.
     */
    public List<Trip> getTripsForUserLoads(String userId, Set<String> userLoadIds) throws Exception {
        Map<String, Trip> matches = new LinkedHashMap<>();
        if (userId != null && !userId.isBlank()) {
            for (QueryDocumentSnapshot document : db.collection("trips")
                    .whereEqualTo("userId", userId.trim()).get().get().getDocuments()) {
                addTrip(matches, document.toObject(Trip.class));
            }
        }
        if (userLoadIds == null || userLoadIds.isEmpty()) return new ArrayList<>(matches.values());

        for (String loadId : userLoadIds) {
            if (loadId == null || loadId.isBlank()) continue;
            for (QueryDocumentSnapshot document : db.collection("trips")
                    .whereEqualTo("loadId", loadId).get().get().getDocuments()) {
                addTrip(matches, document.toObject(Trip.class));
            }
            for (QueryDocumentSnapshot document : db.collection("trips")
                    .whereArrayContains("loadIds", loadId).get().get().getDocuments()) {
                addTrip(matches, document.toObject(Trip.class));
            }
        }
        return new ArrayList<>(matches.values());
    }

    private void addTrip(Map<String, Trip> matches, Trip trip) {
        if (trip == null) return;
        String key = trip.getTripId();
        if (key == null || key.isBlank()) key = "document-" + matches.size();
        matches.putIfAbsent(key, trip);
    }

    /**
     * Activates the trip that already contains this load.  It deliberately
     * keeps a trip that has progressed beyond PICKUP unchanged, making a
     * repeated Razorpay verification safe.
     */
    public Trip activateTripForLoad(String loadId) throws Exception {
        List<Trip> matches = new ArrayList<>();

        for (QueryDocumentSnapshot document : db.collection("trips")
                .whereArrayContains("loadIds", loadId).get().get().getDocuments()) {
            matches.add(document.toObject(Trip.class));
        }
        for (QueryDocumentSnapshot document : db.collection("trips")
                .whereEqualTo("loadId", loadId).get().get().getDocuments()) {
            Trip trip = document.toObject(Trip.class);
            if (trip != null && matches.stream().noneMatch(t ->
                    t != null && java.util.Objects.equals(t.getTripId(), trip.getTripId()))) {
                matches.add(trip);
            }
        }

        if (matches.isEmpty()) {
            throw new IllegalStateException("No trip is associated with load: " + loadId);
        }

        Trip trip = matches.get(0);
        TripStatus status = TripStatus.fromFirestore(trip.getStatus());
        if (trip.getStatus() == null || trip.getStatus().isBlank()
                || "ACCEPTED".equalsIgnoreCase(trip.getStatus())
                || "ACTIVE".equalsIgnoreCase(trip.getStatus())) {
            db.collection("trips").document(trip.getTripId())
                    .update("status", TripStatus.PICKUP.firestoreValue()).get();
            trip.setStatus(TripStatus.PICKUP.firestoreValue());
        } else if (status == TripStatus.PICKUP) {
            trip.setStatus(TripStatus.PICKUP.firestoreValue());
        }
        return trip;
    }

    private boolean isActiveStatus(String status) {
        if (status == null) return false;
        String value = status.trim().toUpperCase().replace(' ', '_');
        return "ACCEPTED".equals(value) || "ACTIVE".equals(value)
                || "PICKUP".equals(value) || "PICKUP_COMPLETED".equals(value)
                || "DISPATCH".equals(value) || "IN_TRANSIT".equals(value)
                || "DELIVERY".equals(value) || "ARRIVED".equals(value)
                || "DELIVERED".equals(value) || "COMPLETED".equals(value);
    }
    // =========================================================
// FIND CURRENT ACTIVE / IN-PROGRESS TRIP FOR DRIVER
// =========================================================

// =========================================================
// FIND CURRENT ACTIVE / IN-PROGRESS TRIP FOR DRIVER
// =========================================================

public Trip getActiveTripByDriverId(String driverId)
        throws Exception {

    System.out.println("=================================");
    System.out.println("SEARCHING CURRENT TRIP");
    System.out.println("Driver ID: " + driverId);

    List<Trip> trips = new ArrayList<>();

    for (QueryDocumentSnapshot document :
            db.collection("trips")
                    .whereEqualTo("driverId", driverId)
                    .get()
                    .get()
                    .getDocuments()) {

        Trip trip = document.toObject(Trip.class);

        System.out.println(
                "FOUND TRIP: "
                + trip.getTripId()
                + " | STATUS: "
                + trip.getStatus()
        );

        String status = trip.getStatus();

        if (status == null) {
            continue;
        }

        status = status.trim().toUpperCase();

        if (isDriverActiveStatus(status)) {

            trips.add(trip);

            System.out.println(
                    "CURRENT TRIP SELECTED: "
                    + trip.getTripId()
            );
        }
    }

    if (trips.isEmpty()) {

        System.out.println(
                "NO CURRENT TRIP FOUND FOR: "
                + driverId
        );

        return null;
    }

    Trip latestTrip = trips.get(0);

    for (Trip trip : trips) {

        if (trip.getTripId() != null
                && latestTrip.getTripId() != null
                && trip.getTripId().compareTo(
                        latestTrip.getTripId()) > 0) {

            latestTrip = trip;
        }
    }

    System.out.println(
            "RETURNING TRIP: "
            + latestTrip.getTripId()
            + " | STATUS: "
            + latestTrip.getStatus()
    );

    System.out.println("=================================");

    return latestTrip;
}
    // =========================================================
    // ADD LOAD TO EXISTING ACTIVE TRIP
    // =========================================================

    public void addLoadToExistingTrip(
            Trip trip,
            String loadId,
            double loadWeight,
            String pickupLocation
    ) throws Exception {

        List<String> loadIds =
                trip.getLoadIds();

        if (loadIds == null) {

            loadIds =
                    new ArrayList<>();
        }

        // Preserve old primary loadId
        if (trip.getLoadId() != null
                && !trip.getLoadId().trim().isEmpty()
                && !loadIds.contains(
                        trip.getLoadId()
                )) {

            loadIds.add(
                    trip.getLoadId()
            );
        }

        // Duplicate check
        if (loadIds.contains(loadId)) {

            System.out.println(
                    "Load already exists in trip: "
                            + loadId
            );

            return;
        }

        double availableCapacity =
                trip.getTotalCapacity()
                        - trip.getUsedCapacity();

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

        double newUsedCapacity =
                trip.getUsedCapacity()
                        + loadWeight;

        loadIds.add(loadId);

        trip.setLoadIds(
                loadIds
        );

        trip.setUsedCapacity(
                newUsedCapacity
        );

        db.collection("trips")
                .document(trip.getTripId())
                .update(
                        "loadIds",
                        loadIds,
                        "usedCapacity",
                        newUsedCapacity
                )
                .get();

        System.out.println(
                "================================="
        );

        System.out.println(
                "LOAD ADDED TO EXISTING TRIP"
        );

        System.out.println(
                "Trip ID: "
                        + trip.getTripId()
        );

        System.out.println(
                "All Load IDs in Trip: "
                        + loadIds
        );

        System.out.println(
                "New Load ID: "
                        + loadId
        );

        System.out.println(
                "Truck Capacity: "
                        + trip.getTotalCapacity()
                        + " Ton"
        );

        System.out.println(
                "Used Capacity: "
                        + newUsedCapacity
                        + " Ton"
        );

        System.out.println(
                "Available Capacity: "
                        + (
                                trip.getTotalCapacity()
                                        - newUsedCapacity
                        )
                        + " Ton"
        );

        System.out.println(
                "================================="
        );
    }


    // =========================================================
// FIND COMPLETED TRIP BY LOAD ID
// =========================================================


public Trip getCompletedTripByLoadId(
        String loadId
) throws Exception {

    List<Trip> trips = new ArrayList<>();

    for (QueryDocumentSnapshot document :
            db.collection("trips")
                    .whereArrayContains(
                            "loadIds",
                            loadId
                    )
                    .get()
                    .get()
                    .getDocuments()) {

        Trip trip = document.toObject(Trip.class);
        if (trip != null && TripStatus.fromFirestore(trip.getStatus()) == TripStatus.DELIVERED) {
            trips.add(trip);
        }
    }

    if (trips.isEmpty()) {
        return null;
    }

    return trips.get(0);
}

    public Trip getCompletedTripByUserId(
        String userId
) throws Exception {

    List<Trip> trips = new ArrayList<>();

    for (QueryDocumentSnapshot document :
            db.collection("trips")
                    .whereEqualTo("userId", userId)
                    .get()
                    .get()
                    .getDocuments()) {

        Trip trip = document.toObject(Trip.class);
        if (trip != null && TripStatus.fromFirestore(trip.getStatus()) == TripStatus.DELIVERED) {
            trips.add(trip);
        }
    }

    if (trips.isEmpty()) {
        return null;
    }

    Trip latestTrip = trips.get(0);

    for (Trip trip : trips) {

        if (trip.getCompletedTime() != null
                && latestTrip.getCompletedTime() != null
                && trip.getCompletedTime()
                        .compareTo(
                                latestTrip.getCompletedTime()
                        ) > 0) {

            latestTrip = trip;
        }
    }

    return latestTrip;
}

    // =========================================================
    // UPDATE TRIP STATUS
    // =========================================================

    public void updateTripStatus(
            String tripId,
            String status,
            String time
    ) throws Exception {

        String canonicalStatus = "COMPLETED".equalsIgnoreCase(status)
                ? "COMPLETED"
                : TripStatus.fromFirestore(status).firestoreValue();

        if (TripStatus.IN_TRANSIT.firestoreValue().equals(canonicalStatus)) {

            db.collection("trips")
                    .document(tripId)
                    .update(
                            "status",
                            canonicalStatus,
                            "startTime",
                            time
                    )
                    .get();

        } else if ("COMPLETED".equals(canonicalStatus)) {

            db.collection("trips")
                    .document(tripId)
                    .update(
                            "status",
                            canonicalStatus,
                            "completedTime",
                            time
                    )
                    .get();

        } else {

            db.collection("trips")
                    .document(tripId)
                    .update(
                            "status",
                            canonicalStatus
                    )
                    .get();
        }

        System.out.println(
                "Trip status updated: "
                        + tripId
                        + " -> "
                        + status
        );
    }

    // =========================================================
// CANCEL TRIP
// =========================================================

public void cancelTrip(
        String tripId,
        String cancelledTime
) throws Exception {

    db.collection("trips")
            .document(tripId)
            .update(
                    "status",
                    "CANCELLED",
                    "cancelledTime",
                    cancelledTime
            )
            .get();

    System.out.println(
            "Trip cancelled: "
                    + tripId
    );
}

    /** Atomically cancels the existing trip and its exact load document. */
    public void cancelTripAndLoad(String tripId, String loadId, String cancelledTime) throws Exception {
        WriteBatch batch = db.batch();
        batch.update(db.collection("trips").document(tripId),
                "status", "CANCELLED", "cancelledTime", cancelledTime);
        batch.update(db.collection("loads").document(loadId), "status", "CANCELLED");
        batch.commit().get();
    }

    /** Returns only an active trip containing this exact load and assigned to this driver. */
    public Trip getActiveTripByLoadAndDriver(String loadId, String driverId) throws Exception {
        if (loadId == null || loadId.isBlank() || driverId == null || driverId.isBlank()) return null;

        for (QueryDocumentSnapshot document : db.collection("trips")
                .whereEqualTo("driverId", driverId).get().get().getDocuments()) {
            Trip trip = document.toObject(Trip.class);
            if (trip == null || !isDriverActiveStatus(trip.getStatus())) continue;
            boolean containsLoad = loadId.equals(trip.getLoadId())
                    || (trip.getLoadIds() != null && trip.getLoadIds().contains(loadId));
            if (containsLoad) return trip;
        }
        return null;
    }

    private boolean isDriverActiveStatus(String status) {
        if (!isActiveStatus(status)) return false;
        TripStatus progress = TripStatus.fromFirestore(status);
        return progress != TripStatus.DELIVERED;
    }
}

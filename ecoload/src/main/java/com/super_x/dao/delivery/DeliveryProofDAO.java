package com.super_x.dao.delivery;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.super_x.config.FirebaseConfig;
import com.super_x.model.delivery.DeliveryProof;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Firestore access for the driver-submitted proof of delivery. */
public final class DeliveryProofDAO {
    private static final DateTimeFormatter PROJECT_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Firestore db = FirebaseConfig.getFireStore();

    public DeliveryRelationship validateDriverAssignment(String tripId, String loadId, String driverId) throws Exception {
        if (blank(tripId) || blank(loadId) || blank(driverId)) throw new IllegalArgumentException("Trip, load, and driver are required.");
        DocumentSnapshot trip = db.collection("trips").document(tripId).get().get();
        DocumentSnapshot load = db.collection("loads").document(loadId).get().get();
        verifyAssignment(trip, load, tripId, loadId, driverId);
        if (db.collection("deliveryProofs").document(proofId(tripId, loadId)).get().get().exists()) {
            throw new IllegalStateException("Delivery proof has already been submitted.");
        }
        return new DeliveryRelationship(load.getString("userId"), trip.getString("driverId"));
    }

    public void saveProof(String tripId, String loadId, String driverId, String userId, String receiverName,
                          String photoUrl, String signatureUrl) throws Exception {
        if (blank(receiverName) || blank(photoUrl) || blank(signatureUrl) || blank(userId)) {
            throw new IllegalArgumentException("Complete delivery proof details are required.");
        }
        DocumentReference proofRef = db.collection("deliveryProofs").document(proofId(tripId, loadId));
        DocumentReference tripRef = db.collection("trips").document(tripId);
        DocumentReference loadRef = db.collection("loads").document(loadId);
        String completedTime = LocalDateTime.now().format(PROJECT_TIME);

        db.runTransaction(transaction -> {
            DocumentSnapshot existing = transaction.get(proofRef).get();
            if (existing.exists()) throw new IllegalStateException("Delivery proof has already been submitted.");
            DocumentSnapshot trip = transaction.get(tripRef).get();
            DocumentSnapshot load = transaction.get(loadRef).get();
            verifyAssignment(trip, load, tripId, loadId, driverId);
            if (!userId.equals(load.getString("userId"))) throw new SecurityException("The load user does not match the delivery proof.");

            Map<String, Object> proof = new LinkedHashMap<>();
            proof.put("tripId", tripId); proof.put("loadId", loadId); proof.put("driverId", driverId); proof.put("userId", userId);
            proof.put("receiverName", receiverName.trim()); proof.put("deliveryPhotoUrl", photoUrl); proof.put("signatureUrl", signatureUrl);
            proof.put("deliveryStatus", "DELIVERED"); proof.put("deliveredAt", FieldValue.serverTimestamp());
            transaction.set(proofRef, proof);
            transaction.update(loadRef, "status", "DELIVERED");

            List<String> tripLoadIds = trip.get("loadIds") instanceof List<?> ids
                    ? ids.stream().filter(String.class::isInstance).map(String.class::cast).toList()
                    : List.of(loadId);
            boolean everyLoadHasProof = true;
            for (String id : tripLoadIds) {
                if (!id.equals(loadId) && !transaction.get(db.collection("deliveryProofs").document(proofId(tripId, id))).get().exists()) {
                    everyLoadHasProof = false;
                    break;
                }
            }
            if (everyLoadHasProof) transaction.update(tripRef, "status", "DELIVERED", "completedTime", completedTime);
            return null;
        }).get();
    }

    public DeliveryProof getProofForUser(String tripId, String loadId, String userId) throws Exception {
        if (blank(tripId) || blank(loadId) || blank(userId)) return null;
        DocumentSnapshot proof = db.collection("deliveryProofs").document(proofId(tripId, loadId)).get().get();
        if (!proof.exists() || !userId.equals(proof.getString("userId"))) return null;
        return new DeliveryProof(proof.getString("tripId"), proof.getString("loadId"), proof.getString("driverId"),
                proof.getString("userId"), proof.getString("receiverName"), proof.getString("deliveryPhotoUrl"),
                proof.getString("signatureUrl"), date(proof.get("deliveredAt")), proof.getString("deliveryStatus"));
    }

    private static void verifyAssignment(DocumentSnapshot trip, DocumentSnapshot load, String tripId, String loadId, String driverId) {
        if (!trip.exists() || !load.exists()) throw new IllegalStateException("The assigned trip or load no longer exists.");
        if (!driverId.equals(trip.getString("driverId")) || !driverId.equals(load.getString("driverId"))) {
            throw new SecurityException("You are not assigned to this trip.");
        }
        Object loadIds = trip.get("loadIds");
        boolean containsLoad = loadId.equals(trip.getString("loadId")) || (loadIds instanceof List<?> ids && ids.contains(loadId));
        if (!containsLoad) throw new SecurityException("This load is not part of the selected trip.");
    }

    private static LocalDateTime date(Object value) {
        if (value instanceof Timestamp timestamp) return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()), ZoneId.systemDefault());
        if (value instanceof java.util.Date date) return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return null;
    }
    private static String proofId(String tripId, String loadId) { return safe(tripId) + "_" + safe(loadId); }
    private static String safe(String value) { return value.replace('/', '_'); }
    private static boolean blank(String value) { return value == null || value.isBlank(); }

    public record DeliveryRelationship(String userId, String driverId) { }
}

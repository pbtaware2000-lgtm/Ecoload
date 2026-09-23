package com.super_x.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.super_x.config.FirebaseConfig;
import com.super_x.model.drivermodel.Trip;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Settles one already-verified Razorpay payment.  A driver is never looked up
 * by name or amount: the only eligible recipient is loads/{loadId}.driverId,
 * confirmed against the trip which contains the same load.
 */
public final class WalletPaymentDao {
    private final Firestore db = FirebaseConfig.getFireStore();

    public Trip settleVerifiedPayment(String loadId, String paymentLinkId, String paymentId,
                                     double verifiedAmount, String completedAt) throws Exception {
        if (blank(loadId) || blank(paymentLinkId) || blank(paymentId) || verifiedAmount <= 0) {
            throw new IllegalArgumentException("Verified payment details are incomplete.");
        }

        DocumentReference loadRef = db.collection("loads").document(loadId);
        DocumentSnapshot load = loadRef.get().get();
        if (!load.exists()) throw new IllegalStateException("Load does not exist: " + loadId);

        String driverId = load.getString("driverId");
        String userId = load.getString("userId");
        String recordedLinkId = load.getString("razorpayPaymentLinkId");
        if (blank(driverId) || blank(userId)) {
            throw new IllegalStateException("The paid load does not have an assigned driver and user.");
        }
        if (!blank(recordedLinkId) && !recordedLinkId.equals(paymentLinkId)) {
            throw new IllegalStateException("Verified payment link does not belong to load: " + loadId);
        }

        DocumentReference tripRef = findExactTrip(loadId, driverId);
        DocumentReference markerRef = db.collection("paymentProcessing").document(paymentId);
        DocumentReference userWalletRef = db.collection("wallets").document("user_" + safeId(userId));
        DocumentReference driverWalletRef = db.collection("wallets").document("driver_" + safeId(driverId));
        DocumentReference userTransactionRef = db.collection("walletTransactions")
                .document(safeId(paymentId) + "_USER_DEBIT");
        DocumentReference driverTransactionRef = db.collection("walletTransactions")
                .document(safeId(paymentId) + "_DRIVER_CREDIT");

        db.runTransaction(transaction -> {
            DocumentSnapshot currentLoad = transaction.get(loadRef).get();
            DocumentSnapshot currentTrip = transaction.get(tripRef).get();
            DocumentSnapshot marker = transaction.get(markerRef).get();
            if (marker.exists()) return null; // The payment was settled by an earlier callback.
            if (!currentLoad.exists() || !currentTrip.exists()) {
                throw new IllegalStateException("Load or its matching trip no longer exists.");
            }

            String transactionDriverId = currentLoad.getString("driverId");
            String tripDriverId = currentTrip.getString("driverId");
            String transactionLinkId = currentLoad.getString("razorpayPaymentLinkId");
            if (!driverId.equals(transactionDriverId) || !driverId.equals(tripDriverId)
                    || (!blank(transactionLinkId) && !paymentLinkId.equals(transactionLinkId))) {
                throw new IllegalStateException("Payment, load, and trip driver relationship does not match.");
            }

            DocumentSnapshot userWallet = transaction.get(userWalletRef).get();
            DocumentSnapshot driverWallet = transaction.get(driverWalletRef).get();
            double userBalance = number(userWallet.get("balance"));
            double driverBalance = number(driverWallet.get("balance"));
            double driverEarnings = number(driverWallet.get("totalEarnings"));
            String route = value(currentLoad.getString("pickupLocation")) + " -> "
                    + value(currentLoad.getString("destination"));

            Map<String, Object> paymentFields = new LinkedHashMap<>();
            paymentFields.put("status", "PICKUP");
            paymentFields.put("paymentStatus", "PAYMENT_COMPLETED");
            paymentFields.put("razorpayPaymentLinkId", paymentLinkId);
            paymentFields.put("razorpayPaymentId", paymentId);
            paymentFields.put("paymentCompletedAt", completedAt);
            transaction.update(loadRef, paymentFields);

            String tripStatus = currentTrip.getString("status");
            if (blank(tripStatus) || "ACCEPTED".equalsIgnoreCase(tripStatus)
                    || "ACTIVE".equalsIgnoreCase(tripStatus)) {
                transaction.update(tripRef, "status", "PICKUP");
            }

            transaction.set(userWalletRef, wallet(userId, "USER", userBalance - verifiedAmount,
                    number(userWallet.get("totalEarnings")), completedAt), SetOptions.merge());
            transaction.set(driverWalletRef, wallet(driverId, "DRIVER", driverBalance + verifiedAmount,
                    driverEarnings + verifiedAmount, completedAt), SetOptions.merge());
            transaction.set(userTransactionRef, ledger("DEBIT", userId, driverId, loadId, tripRef.getId(),
                    paymentId, paymentLinkId, verifiedAmount, route, completedAt));
            transaction.set(driverTransactionRef, ledger("CREDIT", userId, driverId, loadId, tripRef.getId(),
                    paymentId, paymentLinkId, verifiedAmount, route, completedAt));

            Map<String, Object> markerData = new LinkedHashMap<>();
            markerData.put("paymentId", paymentId);
            markerData.put("paymentLinkId", paymentLinkId);
            markerData.put("loadId", loadId);
            markerData.put("tripId", tripRef.getId());
            markerData.put("userId", userId);
            markerData.put("driverId", driverId);
            markerData.put("amount", verifiedAmount);
            markerData.put("status", "COMPLETED");
            markerData.put("completedAt", completedAt);
            transaction.set(markerRef, markerData);
            return null;
        }).get();

        DocumentSnapshot settledTrip = tripRef.get().get();
        return settledTrip.toObject(Trip.class);
    }

    private DocumentReference findExactTrip(String loadId, String driverId) throws Exception {
        Map<String, DocumentReference> matches = new LinkedHashMap<>();
        List<QueryDocumentSnapshot> bySingleLoad = db.collection("trips")
                .whereEqualTo("loadId", loadId).get().get().getDocuments();
        for (QueryDocumentSnapshot trip : bySingleLoad) {
            if (driverId.equals(trip.getString("driverId"))) {
                matches.put(trip.getId(), trip.getReference());
            }
        }
        List<QueryDocumentSnapshot> byLoadList = db.collection("trips")
                .whereArrayContains("loadIds", loadId).get().get().getDocuments();
        for (QueryDocumentSnapshot trip : byLoadList) {
            if (driverId.equals(trip.getString("driverId"))) {
                matches.put(trip.getId(), trip.getReference());
            }
        }
        if (matches.size() != 1) {
            throw new IllegalStateException(matches.isEmpty()
                    ? "No trip for load " + loadId + " has the assigned driver."
                    : "More than one trip matches the paid load; wallet credit was stopped.");
        }
        return matches.values().iterator().next();
    }

    private Map<String, Object> wallet(String ownerId, String ownerType, double balance,
                                       double totalEarnings, String updatedAt) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("ownerId", ownerId); data.put("ownerType", ownerType);
        data.put("balance", balance); data.put("totalEarnings", totalEarnings);
        data.put("updatedAt", updatedAt);
        return data;
    }

    private Map<String, Object> ledger(String type, String userId, String driverId, String loadId,
                                       String tripId, String paymentId, String paymentLinkId,
                                       double amount, String route, String completedAt) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", type); data.put("userId", userId); data.put("driverId", driverId);
        data.put("loadId", loadId); data.put("tripId", tripId); data.put("paymentId", paymentId);
        data.put("razorpayPaymentLinkId", paymentLinkId); data.put("amount", amount);
        data.put("status", "COMPLETED"); data.put("description", "Trip payment: " + route);
        data.put("timestamp", completedAt);
        return data;
    }

    private static boolean blank(String value) { return value == null || value.isBlank(); }
    private static String value(String value) { return value == null ? "" : value; }
    private static String safeId(String value) { return value.replace('/', '_'); }
    private static double number(Object value) { return value instanceof Number number ? number.doubleValue() : 0d; }
}

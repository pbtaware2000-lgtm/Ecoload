package com.super_x.dao.userdao;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.super_x.config.FirebaseConfig;
import com.super_x.model.usermodel.Load;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoadDao {

    private final Firestore db =
            FirebaseConfig.getFireStore();

    // =========================================================
    // ADD LOAD
    // =========================================================

    public Load addLoad(Load load) {

        try {

            String date =
                    new SimpleDateFormat("yyyyMMdd")
                            .format(new Date());

            String loadId =
                    "LD-"
                    + date
                    + "-"
                    + System.currentTimeMillis();

            load.setLoadId(loadId);

            if (load.getStatus() == null
                    || load.getStatus().trim().isEmpty()) {

                load.setStatus("PENDING");
            }

            db.collection("loads")
                    .document(loadId)
                    .create(load)
                    .get();

            System.out.println(
                    "Load Data Inserted"
            );

            System.out.println(
                    "Generated Load ID: "
                    + loadId
            );

            // Return saved Load
            return load;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // FETCH ALL LOADS
    // =========================================================

    public List<Load> fetchAllLoads() {

        try {

            List<Load> loads =
                    new ArrayList<>();

            for (QueryDocumentSnapshot document :
                    db.collection("loads")
                            .get()
                            .get()
                            .getDocuments()) {

                loads.add(
                        document.toObject(
                                Load.class
                        )
                );
            }

            return loads;

        } catch (Exception e) {

            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    // =========================================================
    // FETCH COMPLETED LOADS FOR USER
    // =========================================================

    public List<Load> getCompletedLoadsByUserId(
            String userId) {

        try {

            List<Load> completedLoads =
                    new ArrayList<>();

            for (QueryDocumentSnapshot document :
                    db.collection("loads")
                            .whereEqualTo(
                                    "userId",
                                    userId
                            )
                            .whereEqualTo(
                                    "status",
                                    "COMPLETED"
                            )
                            .get()
                            .get()
                            .getDocuments()) {

                completedLoads.add(
                        document.toObject(
                                Load.class
                        )
                );
            }

            return completedLoads;

        } catch (Exception e) {

            e.printStackTrace();

            return new ArrayList<>();
        }
    }
    // =========================================================
// FETCH ALL LOADS FOR USER
// Used to show user's posted loads and their current status
// =========================================================

public List<Load> getLoadsByUserId(String userId) {

    try {

        List<Load> userLoads =
                new ArrayList<>();

        for (QueryDocumentSnapshot document :
                db.collection("loads")
                        .whereEqualTo(
                                "userId",
                                userId
                        )
                        .get()
                        .get()
                        .getDocuments()) {

            userLoads.add(
                    document.toObject(
                            Load.class
                    )
            );
        }

        return userLoads;

    } catch (Exception e) {

        e.printStackTrace();

        return new ArrayList<>();
    }
}

    // =========================================================
    // FETCH LOAD BY ID
    // =========================================================

    public Load getLoadById(
            String loadId) {

        try {

            var snapshot =
                    db.collection("loads")
                            .document(loadId)
                            .get()
                            .get();

            if (!snapshot.exists()) {

                return null;
            }

            return snapshot.toObject(
                    Load.class
            );

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // UPDATE LOAD STATUS + DRIVER
    // =========================================================

    public void updateLoadStatus(
            String loadId,
            String status,
            String driverId,
            String driverName,
            String acceptedAt) throws Exception {

        db.collection("loads")
                .document(loadId)
                .update(
                        "status",
                        status,
                        "driverId",
                        driverId,
                        "driverName",
                        driverName,
                        "acceptedAt",
                        acceptedAt
                )
                .get();

        System.out.println(
                "Load status updated: "
                + loadId
                + " -> "
                + status
        );
    }

    // =========================================================
    // UPDATE LOAD STATUS ONLY
    // Used when completing the trip
    // =========================================================

    public void updateLoadStatusOnly(
            String loadId,
            String status) throws Exception {

        db.collection("loads")
                .document(loadId)
                .update(
                        "status",
                        status
                )
                .get();

        System.out.println(
                "Load status updated: "
                + loadId
                + " -> "
                + status
        );
    }

    /**
     * Records the exact Razorpay link against the existing load before it is
     * opened.  The document ID remains the primary relationship key.
     */
    public void recordRazorpayPaymentLink(String loadId, String paymentLinkId) throws Exception {
        db.collection("loads").document(loadId)
                .update("razorpayPaymentLinkId", paymentLinkId)
                .get();
    }

    /**
     * Idempotently promotes only the load associated with a verified Razorpay
     * link.  Payment state is kept separate from driver trip progress; the
     * existing load status becomes PICKUP, never IN TRANSIT.
     */
    public void completeRazorpayPayment(
            String loadId, String paymentLinkId, String paymentId, String completedAt) throws Exception {

        DocumentReference loadRef = db.collection("loads").document(loadId);
        db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(loadRef).get();
            if (!snapshot.exists()) {
                throw new IllegalStateException("Load does not exist: " + loadId);
            }

            String recordedLinkId = snapshot.getString("razorpayPaymentLinkId");
            if (recordedLinkId != null && !recordedLinkId.isBlank()
                    && !recordedLinkId.equals(paymentLinkId)) {
                throw new IllegalStateException("Verified payment link does not belong to load: " + loadId);
            }

            transaction.update(loadRef,
                    "status", "PICKUP",
                    "paymentStatus", "PAYMENT_COMPLETED",
                    "razorpayPaymentLinkId", paymentLinkId,
                    "razorpayPaymentId", paymentId,
                    "paymentCompletedAt", completedAt);
            return null;
        }).get();
    }

    // =========================================================
    // RESET LOAD AFTER TRIP CANCELLATION
    // =========================================================

    public void resetLoadAfterTripCancellation(
            String loadId) throws Exception {

        db.collection("loads")
                .document(loadId)
                .update(
                        "status",
                        "PENDING",
                        "driverId",
                        "",
                        "driverName",
                        "",
                        "acceptedAt",
                        ""
                )
                .get();

        System.out.println(
                "Load reset after trip cancellation: "
                + loadId
        );
    }
}

package com.super_x.dao.driverdao;

import com.google.cloud.firestore.*;
import com.super_x.model.drivermodel.DriverRequestModel;

import java.util.ArrayList;
import java.util.List;

public class DriverRequestDAO {

    private final Firestore firestore;

    public DriverRequestDAO(Firestore firestore) {
        this.firestore = firestore;
    }

    public void createRequest(
            DriverRequestModel request) throws Exception {

        firestore.collection("driverRequests")
                .document(request.getDriverEmail())
                .set(request)
                .get();
    }

    public DriverRequestModel getRequestByEmail(
            String email) throws Exception {

        DocumentSnapshot document =
                firestore.collection("driverRequests")
                        .document(email)
                        .get()
                        .get();

        if (!document.exists()) {
            return null;
        }

        return document.toObject(DriverRequestModel.class);
    }

    public List<DriverRequestModel> getPendingRequests()
            throws Exception {

        QuerySnapshot snapshot =
                firestore.collection("driverRequests")
                        .whereEqualTo("status", "PENDING")
                        .get()
                        .get();

        List<DriverRequestModel> requests = new ArrayList<>();

        for (DocumentSnapshot document : snapshot.getDocuments()) {

            DriverRequestModel request =
                    document.toObject(DriverRequestModel.class);

            requests.add(request);
        }

        return requests;
    }

    public void updateStatus(String email,
                             String status,
                             String reviewedBy)
            throws Exception {

        firestore.collection("driverRequests")
                .document(email)
                .update(
                        "status", status,
                        "reviewedBy", reviewedBy,
                        "reviewedAt",
                        java.time.Instant.now().toString()
                )
                .get();
    }

    public void rejectRequest(String email,
                               String reviewedBy,
                               String reason)
            throws Exception {

        firestore.collection("driverRequests")
                .document(email)
                .update(
                        "status", "REJECTED",
                        "reviewedBy", reviewedBy,
                        "reviewedAt",
                        java.time.Instant.now().toString(),
                        "rejectionReason", reason
                )
                .get();
    }
}
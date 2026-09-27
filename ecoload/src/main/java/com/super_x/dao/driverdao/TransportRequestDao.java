package com.super_x.dao.driverdao;



import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.super_x.dao.driverdao.TransportRequestDao;
import com.super_x.model.drivermodel.TransportRequest;

import java.util.ArrayList;
import java.util.List;
import com.google.cloud.firestore.Firestore;
import com.super_x.config.FirebaseConfig;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransportRequestDao {

    private Firestore db = FirebaseConfig.getFireStore();

    public void addTransportRequest(TransportRequest request) {

        try {

            String date =
                    new SimpleDateFormat("yyyyMMdd")
                            .format(new Date());

            String requestId =
                    "REQ-" + date + "-" + System.currentTimeMillis();

            request.setRequestId(requestId);

            request.setStatus("PENDING");

            request.setCreatedAt(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(new Date())
            );

            db.collection("transportRequests")
                    .document(requestId)
                    .create(request);

            System.out.println(
                    "Transport Request Created"
            );

            System.out.println(
                    "Request ID: " + requestId
            );

            System.out.println(
                    "Load ID: " + request.getLoadId()
            );

            System.out.println(
                    "Driver: " + request.getDriverName()
            );

            System.out.println(
                    "Status: " + request.getStatus()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public List<TransportRequest> fetchAllTransportRequests() {

    try {

        List<TransportRequest> requests =
                new ArrayList<>();

        for (QueryDocumentSnapshot document :
                db.collection("transportRequests")
                        .get()
                        .get()
                        .getDocuments()) {

            requests.add(
                    document.toObject(TransportRequest.class)
            );
        }

        return requests;

    } catch (Exception e) {

        e.printStackTrace();

        return new ArrayList<>();
    }
}
}
package com.super_x.dao.admindao;

import com.super_x.config.FirebaseConfig;
import com.super_x.model.adminmodel.Load;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoadDAO {

    private final Firestore db;

    public LoadDAO() {
        db = FirebaseConfig.getFireStore();
    }


    // =========================================================
    // GET ALL LOADS
    // =========================================================

    public List<Load> getAllLoads() {

        List<Load> loads = new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection("loads")
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Load load =
                        document.toObject(Load.class);

                if (load != null) {

                    // Firebase Document ID
                    if (load.getLoadId() == null ||
                            load.getLoadId().isEmpty()) {

                        load.setLoadId(
                                document.getId()
                        );
                    }

                    loads.add(load);

                    System.out.println(
                            "Load fetched: "
                                    + load.getLoadId()
                                    + " | "
                                    + load.getPickupLocation()
                                    + " -> "
                                    + load.getDestination()
                                    + " | Driver: "
                                    + load.getDriverName()
                                    + " | Status: "
                                    + load.getStatus()
                    );
                }
            }

            System.out.println(
                    "Total loads fetched: "
                            + loads.size()
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to fetch loads from Firebase."
            );

            e.printStackTrace();
        }

        return loads;
    }


    // =========================================================
    // GET ACTIVE LOADS
    // =========================================================

    public List<Load> getActiveLoads() {

        List<Load> loads = new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection("loads")
                            .whereEqualTo(
                                    "status",
                                    "ACTIVE"
                            )
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Load load =
                        document.toObject(Load.class);

                if (load != null) {

                    if (load.getLoadId() == null ||
                            load.getLoadId().isEmpty()) {

                        load.setLoadId(
                                document.getId()
                        );
                    }

                    loads.add(load);
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Failed to fetch active loads."
            );

            e.printStackTrace();
        }

        return loads;
    }


    // =========================================================
    // GET PENDING LOADS
    // =========================================================

    public List<Load> getPendingLoads() {

        List<Load> loads = new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection("loads")
                            .whereEqualTo(
                                    "status",
                                    "PENDING"
                            )
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Load load =
                        document.toObject(Load.class);

                if (load != null) {

                    if (load.getLoadId() == null ||
                            load.getLoadId().isEmpty()) {

                        load.setLoadId(
                                document.getId()
                        );
                    }

                    loads.add(load);
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Failed to fetch pending loads."
            );

            e.printStackTrace();
        }

        return loads;
    }
}
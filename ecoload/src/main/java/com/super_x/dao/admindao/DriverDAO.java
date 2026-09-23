package com.super_x.dao.admindao;

import com.super_x.config.FirebaseConfig;
import com.super_x.model.adminmodel.Driver;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DriverDAO {

    private final Firestore db;

    public DriverDAO() {
        db = FirebaseConfig.getFireStore();
    }

    // =========================================================
    // GET ALL DRIVERS
    // =========================================================

    public List<Driver> getAllDrivers() {

        List<Driver> drivers = new ArrayList<>();

        try {

            QuerySnapshot snapshot = db
                    .collection("drivers")
                    .get()
                    .get();

            for (DocumentSnapshot document : snapshot.getDocuments()) {

                Driver driver = document.toObject(Driver.class);

                if (driver != null) {

                    driver.setId(document.getId());

                    drivers.add(driver);

                    System.out.println(
                            "Driver fetched: "
                                    + driver.getUsername()
                                    + " | Email: "
                                    + driver.getEmail()
                                    + " | Phone: "
                                    + driver.getPhone()
                                    + " | License: "
                                    + driver.getDrivingLicenseNumber()
                                    + " | License URL: "
                                    + driver.getLicenseurl()
                                    + " | Status: "
                                    + driver.getStatus()
                    );
                }
            }

            System.out.println(
                    "Total drivers fetched: "
                            + drivers.size()
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to fetch drivers from Firebase."
            );

            e.printStackTrace();
        }

        return drivers;
    }


    // =========================================================
    // GET ONLY PENDING DRIVERS
    // =========================================================

    public List<Driver> getPendingDrivers() {

        List<Driver> drivers = new ArrayList<>();

        try {

            QuerySnapshot snapshot = db
                    .collection("drivers")
                    .whereEqualTo("status", "PENDING")
                    .get()
                    .get();

            for (DocumentSnapshot document : snapshot.getDocuments()) {

                Driver driver = document.toObject(Driver.class);

                if (driver != null) {

                    driver.setId(document.getId());

                    drivers.add(driver);

                    System.out.println(
                            "Pending Driver: "
                                    + driver.getUsername()
                                    + " | Email: "
                                    + driver.getEmail()
                                    + " | License: "
                                    + driver.getDrivingLicenseNumber()
                                    + " | License URL: "
                                    + driver.getLicenseurl()
                    );
                }
            }

            System.out.println(
                    "Total pending drivers: "
                            + drivers.size()
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to fetch pending drivers."
            );

            e.printStackTrace();
        }

        return drivers;
    }


    // =========================================================
    // GET VEHICLE DOCUMENT
    //
    // First:
    // vehicles/{driverEmail}
    //
    // If not found:
    // search vehicles collection by driverEmail/email
    // =========================================================

    public DocumentSnapshot getDriverVehicleDocument(
            String driverEmail) {

        if (driverEmail == null
                || driverEmail.trim().isEmpty()) {

            System.err.println(
                    "Driver email is empty."
            );

            return null;
        }

        String email = driverEmail.trim();

        try {

            // =================================================
            // METHOD 1
            // Document ID = email
            // =================================================

            DocumentSnapshot directDocument = db
                    .collection("vehicles")
                    .document(email)
                    .get()
                    .get();

            if (directDocument.exists()) {

                System.out.println(
                        "Vehicle document found using document ID: "
                                + email
                );

                printVehicleDocument(directDocument);

                return directDocument;
            }

            // =================================================
            // METHOD 2
            // Search using driverEmail
            // =================================================

            QuerySnapshot emailSnapshot = db
                    .collection("vehicles")
                    .whereEqualTo(
                            "driverEmail",
                            email
                    )
                    .limit(1)
                    .get()
                    .get();

            if (!emailSnapshot.isEmpty()) {

                DocumentSnapshot document =
                        emailSnapshot.getDocuments().get(0);

                System.out.println(
                        "Vehicle document found using driverEmail."
                );

                printVehicleDocument(document);

                return document;
            }

            // =================================================
            // METHOD 3
            // Search using email
            // =================================================

            QuerySnapshot secondSnapshot = db
                    .collection("vehicles")
                    .whereEqualTo(
                            "email",
                            email
                    )
                    .limit(1)
                    .get()
                    .get();

            if (!secondSnapshot.isEmpty()) {

                DocumentSnapshot document =
                        secondSnapshot.getDocuments().get(0);

                System.out.println(
                        "Vehicle document found using email field."
                );

                printVehicleDocument(document);

                return document;
            }

            System.out.println(
                    "No vehicle document found for: "
                            + email
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to fetch vehicle document for: "
                            + email
            );

            e.printStackTrace();
        }

        return null;
    }


    // =========================================================
    // PRINT VEHICLE DOCUMENT
    // =========================================================

    private void printVehicleDocument(
            DocumentSnapshot document) {

        System.out.println(
                "--------------------------------"
        );

        System.out.println(
                "Vehicle Document ID: "
                        + document.getId()
        );

        System.out.println(
                "Driver Email: "
                        + getString(
                                document,
                                "driverEmail"
                        )
        );

        System.out.println(
                "Email: "
                        + getString(
                                document,
                                "email"
                        )
        );

        System.out.println(
                "RC URL: "
                        + getString(
                                document,
                                "registrationCertificateUrl"
                        )
        );

        System.out.println(
                "Insurance URL: "
                        + getString(
                                document,
                                "vehicleInsuranceUrl"
                        )
        );

        System.out.println(
                "--------------------------------"
        );
    }


    // =========================================================
    // GET REGISTRATION CERTIFICATE URL
    // =========================================================

    public String getRegistrationCertificateUrl(
            String driverEmail) {

        DocumentSnapshot document =
                getDriverVehicleDocument(driverEmail);

        if (document == null
                || !document.exists()) {

            return null;
        }

        return getString(
                document,
                "registrationCertificateUrl"
        );
    }


    // =========================================================
    // GET VEHICLE INSURANCE URL
    // =========================================================

    public String getVehicleInsuranceUrl(
            String driverEmail) {

        DocumentSnapshot document =
                getDriverVehicleDocument(driverEmail);

        if (document == null
                || !document.exists()) {

            return null;
        }

        return getString(
                document,
                "vehicleInsuranceUrl"
        );
    }


    // =========================================================
    // SAFE FIRESTORE STRING
    // =========================================================

    private String getString(
            DocumentSnapshot document,
            String field) {

        try {

            Object value =
                    document.get(field);

            if (value == null) {
                return null;
            }

            String result =
                    String.valueOf(value).trim();

            if (result.isEmpty()) {
                return null;
            }

            return result;

        } catch (Exception e) {

            System.err.println(
                    "Unable to read Firestore field: "
                            + field
            );

            return null;
        }
    }


    // =========================================================
    // APPROVE DRIVER
    // =========================================================

    public boolean approveDriver(
            String driverId) {

        if (driverId == null
                || driverId.trim().isEmpty()) {

            return false;
        }

        try {

            db.collection("drivers")
                    .document(driverId)
                    .update(
                            "status",
                            "APPROVED"
                    )
                    .get();

            System.out.println(
                    "Driver approved successfully: "
                            + driverId
            );

            return true;

        } catch (Exception e) {

            System.err.println(
                    "Failed to approve driver: "
                            + driverId
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // REJECT DRIVER
    // =========================================================

    public boolean rejectDriver(
            String driverId) {

        if (driverId == null
                || driverId.trim().isEmpty()) {

            return false;
        }

        try {

            db.collection("drivers")
                    .document(driverId)
                    .update(
                            "status",
                            "REJECTED"
                    )
                    .get();

            System.out.println(
                    "Driver rejected successfully: "
                            + driverId
            );

            return true;

        } catch (Exception e) {

            System.err.println(
                    "Failed to reject driver: "
                            + driverId
            );

            e.printStackTrace();

            return false;
        }
    }
}
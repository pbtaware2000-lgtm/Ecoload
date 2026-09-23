package com.super_x.dao.driverdao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.api.core.ApiFuture;
//import com.google.cloud.firestore.WriteResult;
import com.super_x.model.drivermodel.VehicleModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.cloud.firestore.SetOptions;
import com.super_x.model.drivermodel.VehicleHealthProfile;

public class VehicleDAO {

    private final Firestore firestore;

    public VehicleDAO(Firestore firestore) {
        this.firestore = firestore;
    }

    public void saveVehicle(VehicleModel vehicle) throws Exception {

        firestore.collection("vehicles")
                .document(vehicle.getDriverEmail())
                .set(vehicle)
                .get();
    }

    public VehicleModel getVehicleByDriverEmail(
            String driverEmail) throws Exception {

        ApiFuture<DocumentSnapshot> future =
                firestore.collection("vehicles")
                        .document(driverEmail)
                        .get();

        DocumentSnapshot document = future.get();

        if (!document.exists()) {
            return null;
        }

        return document.toObject(VehicleModel.class);
    }

    /** Saves only the optional health profile; registration fields are never overwritten. */
    public void saveVehicleHealth(String driverEmail, VehicleHealthProfile health) throws Exception {
        if (driverEmail == null || driverEmail.isBlank()) {
            throw new IllegalArgumentException("Driver email is required.");
        }
        firestore.collection("vehicles").document(driverEmail)
                .set(Map.of("vehicleHealth", health), SetOptions.merge()).get();
    }

    /** Returns registered vehicles for read-only recommendation features. */
    public List<VehicleModel> getAllVehicles() throws Exception {
        List<VehicleModel> vehicles = new ArrayList<>();
        for (var document : firestore.collection("vehicles").get().get().getDocuments()) {
            VehicleModel vehicle = document.toObject(VehicleModel.class);
            if (vehicle != null) {
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }
}

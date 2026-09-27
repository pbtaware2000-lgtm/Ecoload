package com.super_x.controller.drivercontroller;

import java.util.List;
import com.super_x.dao.driverdao.TransportRequestDao;
import com.super_x.model.drivermodel.TransportRequest;

public class TransportRequestController {

    TransportRequestDao transportRequestDao =
            new TransportRequestDao();

    public void createTransportRequest(
            String loadId,
            String driverId,
            String driverName,
            String phone,
            String vehicleNumber,
            String vehicleType
    ) {

        TransportRequest request =
                new TransportRequest();

        request.setLoadId(loadId);
        request.setDriverId(driverId);
        request.setDriverName(driverName);
        request.setPhone(phone);
        request.setVehicleNumber(vehicleNumber);
        request.setVehicleType(vehicleType);

        transportRequestDao.addTransportRequest(request);
    }
    public List<TransportRequest> getAllTransportRequests() {

    return transportRequestDao.fetchAllTransportRequests();
}
}

package com.super_x.model.drivermodel;



public class TransportRequest {

    private String requestId;
    private String loadId;

    private String driverId;
    private String driverName;
    private String phone;
    private String vehicleNumber;
    private String vehicleType;

    private String status;
    private String createdAt;


    // =========================================================
    // EMPTY CONSTRUCTOR
    // Required for Firebase / Firestore
    // =========================================================

    public TransportRequest() {
    }


    // =========================================================
    // REQUEST ID
    // =========================================================

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    // =========================================================
    // LOAD ID
    // =========================================================

    public String getLoadId() {
        return loadId;
    }

    public void setLoadId(String loadId) {
        this.loadId = loadId;
    }


    // =========================================================
    // DRIVER ID
    // =========================================================

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }


    // =========================================================
    // DRIVER NAME
    // =========================================================

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }


    // =========================================================
    // PHONE
    // =========================================================

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    // =========================================================
    // VEHICLE NUMBER
    // =========================================================

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }


    // =========================================================
    // VEHICLE TYPE
    // =========================================================

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }


    // =========================================================
    // STATUS
    // =========================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    // =========================================================
    // CREATED AT
    // =========================================================

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    // =========================================================
    // TO STRING
    // =========================================================

   
}
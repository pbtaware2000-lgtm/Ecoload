package com.super_x.model.adminmodel;


public class Trip {

    private String tripId;
    private String loadId;

    private String driverId;
    private String driverName;

    private String userId;

    private String pickupLocation;
    private String destination;

    private String status;

    private Object startTime;
    private Object completedTime;


    // =========================================================
    // FIRESTORE REQUIRED CONSTRUCTOR
    // =========================================================

    public Trip() {
    }


    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }


    public String getLoadId() {
        return loadId;
    }

    public void setLoadId(String loadId) {
        this.loadId = loadId;
    }


    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }


    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }


    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Object getStartTime() {
        return startTime;
    }

    public void setStartTime(Object startTime) {
        this.startTime = startTime;
    }


    public Object getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Object completedTime) {
        this.completedTime = completedTime;
    }
}
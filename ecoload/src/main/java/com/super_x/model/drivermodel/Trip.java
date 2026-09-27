package com.super_x.model.drivermodel;

import java.util.ArrayList;
import java.util.List;

public class Trip {

    private String tripId;

    // Existing single load ID - keep for compatibility
    private String loadId;

    // Multiple loads in same trip
    private List<String> loadIds = new ArrayList<>();

    private String userId;
    private String driverId;
    private String driverName;

    private double distanceKm;
    private String eta;

    private double totalCapacity;
    private double usedCapacity;

    private String pickupLocation;
    private String destination;

    private String status;

    private String startTime;
    private String completedTime;
    private String cancelledTime;

    public Trip() {
    }

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

    public List<String> getLoadIds() {
        return loadIds;
    }

    public void setLoadIds(List<String> loadIds) {
        this.loadIds = loadIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public double getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(double totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public double getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(double usedCapacity) {
        this.usedCapacity = usedCapacity;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }

    public String getCancelledTime() {
        return cancelledTime;
    }

    public void setCancelledTime(String cancelledTime) {
        this.cancelledTime = cancelledTime;
    }
}

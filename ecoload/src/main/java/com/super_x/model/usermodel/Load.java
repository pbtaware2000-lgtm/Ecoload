package com.super_x.model.usermodel;



public class Load {

    private String receiverName;
    private String loadId;
    private String userId;
    private String transporterName;

    private String pickupLocation;
    private String destination;

    private String loadType;

    private double weight;
    private String weightUnit;

    private String truckType;
    private double offerPrice;

    private String pickupDate;
    private String pickupTime;

    private String deliveryDate;
    private String deliveryTime;

    private String status;

    private String driverId;
    private String driverName;

    private String createdAt;
    private String acceptedAt;
    private String paymentCompletedAt;


    // =========================================================
    // EMPTY CONSTRUCTOR
    // Required for Firebase / Firestore
    // =========================================================

    public Load() {
    }


    // =========================================================
    // GETTERS & SETTERS
    // =========================================================

    public String getLoadId() {
        return loadId;
    }

    public void setLoadId(String loadId) {
        this.loadId = loadId;
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


    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }


    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }


    public String getTruckType() {
        return truckType;
    }

    public void setTruckType(String truckType) {
        this.truckType = truckType;
    }


    public double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(double offerPrice) {
        this.offerPrice = offerPrice;
    }


    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }


    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }


    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }


    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(String acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public String getPaymentCompletedAt() {
        return paymentCompletedAt;
    }

    public void setPaymentCompletedAt(String paymentCompletedAt) {
        this.paymentCompletedAt = paymentCompletedAt;
    }

    public String getReceiverName() {
    return receiverName;
    }

    public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
    }

    public String getTransporterName() {
    return transporterName;
    }

    public void setTransporterName(String transporterName) {
    this.transporterName = transporterName;
    }

    



   
}

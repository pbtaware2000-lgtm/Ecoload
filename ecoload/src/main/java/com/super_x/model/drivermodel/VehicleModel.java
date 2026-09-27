package com.super_x.model.drivermodel;

public class VehicleModel {

    private String driverEmail;

    private String vehicleName;
    private String vehiclePlateNumber;
    private double vehicleCapacity;
    private String vehicleType;
    private String fuelType;
    private String vehicleColour;
    private int manufacturingYear;

    private String registrationCertificateUrl;
    private String vehicleInsuranceUrl;

    /* Optional, non-duplicated maintenance data stored with this vehicle. */
    private VehicleHealthProfile vehicleHealth;

    private String createdAt;
    private String updatedAt;

    public VehicleModel() {
    }

    public VehicleModel(String driverEmail,
                        String vehicleName,
                        String vehiclePlateNumber,
                        double vehicleCapacity,
                        String vehicleType,
                        String fuelType,
                        String vehicleColour,
                        int manufacturingYear,
                        String registrationCertificateUrl,
                        String vehicleInsuranceUrl,
                        String createdAt,
                        String updatedAt) {

        this.driverEmail = driverEmail;
        this.vehicleName = vehicleName;
        this.vehiclePlateNumber = vehiclePlateNumber;
        this.vehicleCapacity = vehicleCapacity;
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.vehicleColour = vehicleColour;
        this.manufacturingYear = manufacturingYear;
        this.registrationCertificateUrl = registrationCertificateUrl;
        this.vehicleInsuranceUrl = vehicleInsuranceUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public double getVehicleCapacity() {
        return vehicleCapacity;
    }

    public void setVehicleCapacity(double vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getVehicleColour() {
        return vehicleColour;
    }

    public void setVehicleColour(String vehicleColour) {
        this.vehicleColour = vehicleColour;
    }

    public int getManufacturingYear() {
        return manufacturingYear;
    }

    public void setManufacturingYear(int manufacturingYear) {
        this.manufacturingYear = manufacturingYear;
    }

    public String getRegistrationCertificateUrl() {
        return registrationCertificateUrl;
    }

    public void setRegistrationCertificateUrl(String registrationCertificateUrl) {
        this.registrationCertificateUrl = registrationCertificateUrl;
    }

    public String getVehicleInsuranceUrl() {
        return vehicleInsuranceUrl;
    }

    public void setVehicleInsuranceUrl(String vehicleInsuranceUrl) {
        this.vehicleInsuranceUrl = vehicleInsuranceUrl;
    }

    public VehicleHealthProfile getVehicleHealth() {
        return vehicleHealth;
    }

    public void setVehicleHealth(VehicleHealthProfile vehicleHealth) {
        this.vehicleHealth = vehicleHealth;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

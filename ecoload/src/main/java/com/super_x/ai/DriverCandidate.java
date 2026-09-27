package com.super_x.ai;

public class DriverCandidate {

    private String driverName;
    private String driverEmail;
    private String driverStatus;

    private String vehicleName;
    private String vehicleType;
    private double vehicleCapacity;
    private String fuelType;

    public DriverCandidate(
            String driverName,
            String driverEmail,
            String driverStatus,
            String vehicleName,
            String vehicleType,
            double vehicleCapacity,
            String fuelType) {

        this.driverName = driverName;
        this.driverEmail = driverEmail;
        this.driverStatus = driverStatus;
        this.vehicleName = vehicleName;
        this.vehicleType = vehicleType;
        this.vehicleCapacity = vehicleCapacity;
        this.fuelType = fuelType;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public double getVehicleCapacity() {
        return vehicleCapacity;
    }

    public String getFuelType() {
        return fuelType;
    }
}
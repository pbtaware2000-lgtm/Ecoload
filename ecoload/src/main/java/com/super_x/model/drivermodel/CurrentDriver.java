package com.super_x.model.drivermodel;

public class CurrentDriver {

    private static CurrentDriver instance;

    private DriverModel driver;

    private CurrentDriver() {
    }

    public static CurrentDriver getInstance() {

        if (instance == null) {
            instance = new CurrentDriver();
        }

        return instance;
    }

    public DriverModel getDriver() {
        return driver;
    }

    public void setDriver(DriverModel driver) {
        this.driver = driver;
    }

    public void clearDriver() {
        driver = null;
    }
}

package com.super_x.model.adminmodel;

public class DriverDocuments {

    private String driverEmail;

    // Driving Licence
    private String licenseUrl;

    // Vehicle Registration Certificate
    private String registrationCertificateUrl;

    // Vehicle Insurance
    private String vehicleInsuranceUrl;


    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public DriverDocuments() {
    }


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public DriverDocuments(
            String driverEmail,
            String licenseUrl,
            String registrationCertificateUrl,
            String vehicleInsuranceUrl) {

        this.driverEmail = driverEmail;
        this.licenseUrl = licenseUrl;
        this.registrationCertificateUrl = registrationCertificateUrl;
        this.vehicleInsuranceUrl = vehicleInsuranceUrl;
    }


    // =========================================================
    // DRIVER EMAIL
    // =========================================================

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }


    // =========================================================
    // DRIVING LICENCE URL
    // =========================================================

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }


    // =========================================================
    // REGISTRATION CERTIFICATE URL
    // =========================================================

    public String getRegistrationCertificateUrl() {
        return registrationCertificateUrl;
    }

    public void setRegistrationCertificateUrl(
            String registrationCertificateUrl) {

        this.registrationCertificateUrl =
                registrationCertificateUrl;
    }


    // =========================================================
    // VEHICLE INSURANCE URL
    // =========================================================

    public String getVehicleInsuranceUrl() {
        return vehicleInsuranceUrl;
    }

    public void setVehicleInsuranceUrl(
            String vehicleInsuranceUrl) {

        this.vehicleInsuranceUrl =
                vehicleInsuranceUrl;
    }
}

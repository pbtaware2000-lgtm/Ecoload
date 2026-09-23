package com.super_x.model.drivermodel;

public class DriverModel {

    private String username;
    private String phone;
    private String email;
    private String drivingLicenseNumber;
    private String licenseurl;
    private String role;
    private String status;
    private String createdAt;
    private String updatedAt;

    public DriverModel(){
        
    }

    public DriverModel(String username, String phone, String email, String drivingLicenseNumber, 
        String licenseurl, String role, String status,String createdAt, String updatedAt){
        this.username=username;
        this.phone=phone;
        this.email=email;
        this.drivingLicenseNumber=drivingLicenseNumber;
        this.licenseurl=licenseurl;
        this.role=role;
        this.status=status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public String getLicenseurl() {
        return licenseurl;
    }

    public void setLicenseurl(String licenseurl) {
        this.licenseurl = licenseurl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}

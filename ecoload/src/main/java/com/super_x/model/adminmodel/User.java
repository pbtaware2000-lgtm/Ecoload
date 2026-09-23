package com.super_x.model.adminmodel;

public class User {

    // Firebase Document ID
    private String id;

    private String address;
    private String businessDocumenturl;
    private String businessLicenseNumber;
    private String businessType;
    private String city;
    private String email;
    private String gstNumber;
    private String phone;
    private String pinCode;
    private String role;
    private String state;
    private String status;
    private String username;

    // Firebase requires empty constructor
    public User() {
    }

    // =========================
    // ID
    // =========================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // =========================
    // ADDRESS
    // =========================

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // =========================
    // BUSINESS DOCUMENT
    // =========================

    public String getBusinessDocumenturl() {
        return businessDocumenturl;
    }

    public void setBusinessDocumenturl(String businessDocumenturl) {
        this.businessDocumenturl = businessDocumenturl;
    }

    // =========================
    // BUSINESS LICENSE
    // =========================

    public String getBusinessLicenseNumber() {
        return businessLicenseNumber;
    }

    public void setBusinessLicenseNumber(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }

    // =========================
    // BUSINESS TYPE
    // =========================

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    // =========================
    // CITY
    // =========================

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // =========================
    // EMAIL
    // =========================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // =========================
    // GST NUMBER
    // =========================

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    // =========================
    // PHONE
    // =========================

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // =========================
    // PIN CODE
    // =========================

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    // =========================
    // ROLE
    // =========================

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // =========================
    // STATE
    // =========================

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // =========================
    // STATUS
    // =========================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // =========================
    // USERNAME
    // =========================

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
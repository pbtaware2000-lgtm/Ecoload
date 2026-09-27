package com.super_x.model.usermodel;

public class UserModel {

    private String username;
    private String phone;
    private String email;
    private String gstNumber;
    private String businessType;
    private String businessLicenseNumber;
    private String address;
    private String city;
    private String state;
    private String pinCode;
    private String businessDocumenturl;
    private String role;
    private String status;

    public UserModel(){
    }

    public UserModel(String username, String phone, String email, String gstNumber, String businessType, String businessLicenseNumber,
    String address, String city, String state, String pinCode, String businessDocumenturl, String role, String status){
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.gstNumber = gstNumber;
        this.businessType = businessType;
        this.businessLicenseNumber = businessLicenseNumber;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.businessDocumenturl = businessDocumenturl;
        this.role = role;
        this.status = status;
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

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessLicenseNumber() {
        return businessLicenseNumber;
    }

    public void setBusinessLicenseNumber(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getBusinessDocumenturl() {
        return businessDocumenturl;
    }

    public void setBusinessDocumenturl(String businessDocumenturl) {
        this.businessDocumenturl = businessDocumenturl;
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

package com.super_x.model.drivermodel;

public class DriverRequestModel {

    private String driverEmail;
    private String status;

    private String submittedAt;
    private String reviewedAt;
    private String reviewedBy;

    private String rejectionReason;

    public DriverRequestModel() {
    }

    public DriverRequestModel(String driverEmail,String status,String submittedAt,
                              String reviewedAt,
                              String reviewedBy,
                              String rejectionReason) {

        this.driverEmail = driverEmail;
        this.status = status;
        this.submittedAt = submittedAt;
        this.reviewedAt = reviewedAt;
        this.reviewedBy = reviewedBy;
        this.rejectionReason = rejectionReason;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(String reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
package com.super_x.model.reviewmodel;

public class ReviewModel {

    private String reviewerMail;
    private String reviewerRole;

    private String targetMail;
    private String targetType;

    private String tripId;

    private int rating;
    private String comment;

    private String createdAt;
    private String status;

    public ReviewModel() {
    }

    public ReviewModel(
            String reviewerMail,
            String reviewerRole,
            String targetMail,
            String targetType,
            String tripId,
            int rating,
            String comment,
            String createdAt,
            String status
    ) {
        this.reviewerMail = reviewerMail;
        this.reviewerRole = reviewerRole;
        this.targetMail = targetMail;
        this.targetType = targetType;
        this.tripId = tripId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.status = status;
    }

    public String getReviewerMail() {
        return reviewerMail;
    }

    public void setReviewerMail(String reviewerMail) {
        this.reviewerMail = reviewerMail;
    }

    public String getReviewerRole() {
        return reviewerRole;
    }

    public void setReviewerRole(String reviewerRole) {
        this.reviewerRole = reviewerRole;
    }

    public String getTargetMail() {
        return targetMail;
    }

    public void setTargetMail(String targetMail) {
        this.targetMail = targetMail;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
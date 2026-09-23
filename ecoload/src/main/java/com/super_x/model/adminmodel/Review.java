package com.super_x.model.adminmodel;

public class Review {

    private String id;
    private String reviewerMail;
    private String reviewerRole;
    private int rating;
    private String comment;
    private String createdAt;
    private String status;
    private String targetMail;
    private String targetType;
    private String tripId;

    public Review() {
    }

    public Review(
            String id,
            String reviewerMail,
            String reviewerRole,
            int rating,
            String comment,
            String createdAt,
            String status,
            String targetMail,
            String targetType,
            String tripId
    ) {
        this.id = id;
        this.reviewerMail = reviewerMail;
        this.reviewerRole = reviewerRole;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.status = status;
        this.targetMail = targetMail;
        this.targetType = targetType;
        this.tripId = tripId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
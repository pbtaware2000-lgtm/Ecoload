package com.super_x.model.drivermodel;

import java.util.ArrayList;
import java.util.List;

public class SupportTicket {

    private String id;

    private String driverEmail;
    private String driverName;
    private String role;

    private String issueType;
    private String priority;
    private String subject;
    private String description;

    private String status;

    private String createdAt;
    private String updatedAt;

    private String resolution;
    private String attachment;
    private String assignedTo;
    private String adminResponse;

    private int rating;
    private String feedback;

    private List<TimelineEvent> timeline;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public SupportTicket() {
        this.timeline = new ArrayList<>();
        this.rating = 0;
    }

    // =========================================================
    // PARAMETERIZED CONSTRUCTOR
    // =========================================================

    public SupportTicket(
            String id,
            String driverEmail,
            String driverName,
            String role,
            String issueType,
            String priority,
            String subject,
            String description,
            String status,
            String createdAt,
            String updatedAt,
            String resolution,
            String attachment) {

        this.id = id;
        this.driverEmail = driverEmail;
        this.driverName = driverName;
        this.role = role;

        this.issueType = issueType;
        this.priority = priority;
        this.subject = subject;
        this.description = description;

        this.status = status;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        this.resolution = resolution;
        this.attachment = attachment;

        this.rating = 0;
        this.feedback = null;

        this.timeline = new ArrayList<>();
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public String getId() {
        return id;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getRole() {
        return role;
    }

    public String getIssueType() {
        return issueType;
    }

    public String getPriority() {
        return priority;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getResolution() {
        return resolution;
    }

    public String getAttachment() {
        return attachment;
    }

    public int getRating() {
        return rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public List<TimelineEvent> getTimeline() {
        return timeline;
    }

    // =========================================================
    // SETTERS
    // =========================================================

    public void setId(String id) {
        this.id = id;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setTimeline(List<TimelineEvent> timeline) {
        this.timeline =
                timeline != null
                        ? timeline
                        : new ArrayList<>();
    }

    // =========================================================
    // ADD TIMELINE EVENT
    // =========================================================

    public void addTimelineEvent(TimelineEvent event) {

        if (this.timeline == null) {
            this.timeline = new ArrayList<>();
        }

        if (event != null) {
            this.timeline.add(event);
        }
    }
}

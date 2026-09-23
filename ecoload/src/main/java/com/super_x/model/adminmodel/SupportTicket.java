package com.super_x.model.adminmodel;

import java.util.ArrayList;
import java.util.List;

public class SupportTicket {

    // ============================================================
    // BASIC TICKET FIELDS
    // ============================================================

    private String ticketId;
    private String subject;
    private String issueType;
    private String description;
    private String priority;
    private String status;
    private String resolution;

    // Existing Firebase field
    private String role;
    private String userEmail;
    private String driverName;
    private String driverEmail;

    private String created;
    private String updatedAt;

    private int rating;

    private String attachment;
    private String feedback;

    private List<TimelineItem> timeline;

    // ============================================================
    // ADMIN / USER DETAIL FIELDS
    // ============================================================

    /*
     * These fields are required by SupportPage.
     *
     * They are kept separate from the existing Firebase fields
     * so your existing SupportController/Firebase structure
     * does not break.
     */

    private String userName;
    private String userType;
    private String phone;
    private String shipmentId;
    private String tripId;
    private String vehicleNo;
    private String assignedTo;
    private String adminResponse;

    // ============================================================
    // CONSTRUCTORS
    // ============================================================

    public SupportTicket() {

        /*
         * Important for Firebase deserialization.
         */

        this.timeline = new ArrayList<>();
        this.assignedTo = "Unassigned";
    }

    public SupportTicket(
            String ticketId,
            String subject,
            String issueType,
            String description,
            String priority,
            String status,
            String resolution,
            String role,
            String userEmail,
            String created,
            String updatedAt,
            int rating,
            String attachment,
            String feedback,
            List<TimelineItem> timeline
    ) {

        this.ticketId = ticketId;
        this.subject = subject;
        this.issueType = issueType;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.resolution = resolution;
        this.role = role;
        this.userEmail = userEmail;
        this.created = created;
        this.updatedAt = updatedAt;
        this.rating = rating;
        this.attachment = attachment;
        this.feedback = feedback;
        this.timeline = timeline;

        this.assignedTo = "Unassigned";
    }

    // ============================================================
    // TICKET ID
    // ============================================================

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    // ============================================================
    // SUBJECT
    // ============================================================

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    // ============================================================
    // ISSUE TYPE
    // ============================================================

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    // ============================================================
    // DESCRIPTION
    // ============================================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // ============================================================
    // PRIORITY
    // ============================================================

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    // ============================================================
    // STATUS
    // ============================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ============================================================
    // RESOLUTION
    // ============================================================

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    // ============================================================
    // ROLE
    // ============================================================

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // ============================================================
    // USER EMAIL
    // ============================================================

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    // ============================================================
    // DRIVER REPORTER DETAILS
    // ============================================================

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    // ============================================================
    // CREATED
    // ============================================================

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    // ============================================================
    // UPDATED AT
    // ============================================================

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ============================================================
    // RATING
    // ============================================================

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    // ============================================================
    // ATTACHMENT
    // ============================================================

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    // ============================================================
    // FEEDBACK
    // ============================================================

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    // ============================================================
    // TIMELINE
    // ============================================================

    public List<TimelineItem> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<TimelineItem> timeline) {
        this.timeline = timeline;
    }

    // ============================================================
    // USER NAME
    // ============================================================

    public String getUserName() {
        return getReportedBy();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** Returns the correct reporter identity for either supported ticket role. */
    public String getReportedBy() {
        if (isDriverReporter()) {
            return firstPresent(driverName, driverEmail, userName, userEmail);
        }
        return firstPresent(userName, userEmail, driverName, driverEmail);
    }

    private boolean isDriverReporter() {
        return "DRIVER".equalsIgnoreCase(role)
                || "DRIVER".equalsIgnoreCase(userType);
    }

    private String firstPresent(String... values) {
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                return value;
            }
        }
        return "";
    }

    // ============================================================
    // USER TYPE
    // ============================================================

    public String getUserType() {

        /*
         * SupportPage expects getUserType().
         *
         * Your original model has role.
         * Therefore role is used as fallback.
         */

        if (userType != null && !userType.trim().isEmpty()) {
            return userType;
        }

        return role;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    // ============================================================
    // PHONE
    // ============================================================

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // ============================================================
    // SHIPMENT ID
    // ============================================================

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    // ============================================================
    // TRIP ID
    // ============================================================

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    // ============================================================
    // VEHICLE NUMBER
    // ============================================================

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    // ============================================================
    // ASSIGNED TO
    // ============================================================

    public String getAssignedTo() {

        if (assignedTo == null || assignedTo.trim().isEmpty()) {
            return "Unassigned";
        }

        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {

        if (assignedTo == null || assignedTo.trim().isEmpty()) {
            this.assignedTo = "Unassigned";
        } else {
            this.assignedTo = assignedTo;
        }
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    // ============================================================
    // CREATED TIME
    // ============================================================

    /*
     * SupportPage calls getCreatedTime().
     *
     * Your original SupportTicket already has "created".
     *
     * Therefore we simply return created.
     */

    public String getCreatedTime() {
        return created;
    }

    public void setCreatedTime(String createdTime) {
        this.created = createdTime;
    }

    // ============================================================
    // TIMELINE ITEM
    // ============================================================

    public static class TimelineItem {

        private boolean completed;
        private String description;
        private String time;
        private String title;

        // --------------------------------------------------------
        // DEFAULT CONSTRUCTOR
        // --------------------------------------------------------

        public TimelineItem() {
        }

        // --------------------------------------------------------
        // CONSTRUCTOR
        // --------------------------------------------------------

        public TimelineItem(
                boolean completed,
                String description,
                String time,
                String title
        ) {

            this.completed = completed;
            this.description = description;
            this.time = time;
            this.title = title;
        }

        // --------------------------------------------------------
        // COMPLETED
        // --------------------------------------------------------

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        // --------------------------------------------------------
        // DESCRIPTION
        // --------------------------------------------------------

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        // --------------------------------------------------------
        // TIME
        // --------------------------------------------------------

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        // --------------------------------------------------------
        // TITLE
        // --------------------------------------------------------

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

package com.super_x.controller.drivercontroller;

import com.super_x.dao.driverdao.SupportDAO;
import com.super_x.model.drivermodel.SupportTicket;
import com.super_x.model.drivermodel.TimelineEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SupportController {

    private final SupportDAO supportDAO;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(
                    "dd MMM yyyy • hh:mm a");

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SupportController(
            SupportDAO supportDAO) {

        this.supportDAO = supportDAO;
    }

    // =========================================================
    // CREATE DRIVER SUPPORT TICKET
    // =========================================================

    public SupportTicket createTicket(
            String driverEmail,
            String driverName,
            String issueType,
            String priority,
            String subject,
            String description,
            String attachment) {

        // =====================================================
        // VALIDATION
        // =====================================================

        if (driverEmail == null ||
                driverEmail.isBlank()) {

            throw new IllegalArgumentException(
                    "Driver email not found.");
        }

        if (subject == null ||
                subject.isBlank()) {

            throw new IllegalArgumentException(
                    "Subject is required.");
        }

        if (description == null ||
                description.isBlank()) {

            throw new IllegalArgumentException(
                    "Description is required.");
        }

        if (issueType == null ||
                issueType.isBlank()) {

            issueType = "General";
        }

        if (priority == null ||
                priority.isBlank()) {

            priority = "Medium";
        }

        // =====================================================
        // CURRENT TIME
        // =====================================================

        String now =
                LocalDateTime
                        .now()
                        .format(FORMATTER);

        // =====================================================
        // GENERATE TICKET ID
        // =====================================================

        String ticketId =
                "TK-" +
                        UUID.randomUUID()
                                .toString()
                                .replace(
                                        "-",
                                        "")
                                .substring(
                                        0,
                                        12)
                                .toUpperCase();

        // =====================================================
        // CREATE TICKET
        // =====================================================

        SupportTicket ticket =
                new SupportTicket();

        ticket.setId(ticketId);

        // VERY IMPORTANT
        ticket.setDriverEmail(
                driverEmail);

        ticket.setDriverName(driverName);

        ticket.setRole(
                "DRIVER");

        ticket.setIssueType(
                issueType);

        ticket.setPriority(
                priority);

        ticket.setSubject(
                subject);

        ticket.setDescription(
                description);

        ticket.setStatus(
                "Open");

        ticket.setCreatedAt(
                now);

        ticket.setUpdatedAt(
                now);

        ticket.setAttachment(
                attachment);

        ticket.setResolution(
                "Your support request has been received.");

        ticket.setRating(0);

        ticket.setFeedback("");

        // =====================================================
        // TIMELINE
        // =====================================================

        List<TimelineEvent> timeline =
                new ArrayList<>();

        timeline.add(
                new TimelineEvent(
                        "Ticket Submitted",
                        "You submitted the support ticket.",
                        now,
                        true));

        timeline.add(
                new TimelineEvent(
                        "Ticket Assigned",
                        "Your ticket has been assigned to the EcoLoad support team.",
                        "",
                        false));

        timeline.add(
                new TimelineEvent(
                        "Under Review",
                        "Support team will review your issue.",
                        "",
                        false));

        timeline.add(
                new TimelineEvent(
                        "Resolution Pending",
                        "Waiting for support team action.",
                        "",
                        false));

        ticket.setTimeline(
                timeline);

        // =====================================================
        // SAVE
        // =====================================================

        boolean saved =
                supportDAO.createTicket(
                        ticket);

        if (!saved) {

            throw new RuntimeException(
                    "Failed to save support ticket.");
        }

        return ticket;
    }

    // =========================================================
    // GET MY DRIVER TICKETS
    // =========================================================

    public List<SupportTicket> getDriverTickets(
            String driverEmail) {

        if (driverEmail == null ||
                driverEmail.isBlank()) {

            throw new IllegalArgumentException(
                    "Driver email not found.");
        }

        return supportDAO.getTicketsByDriver(
                driverEmail);
    }

    // =========================================================
    // GET ONE TICKET
    // =========================================================

    public SupportTicket getTicket(
            String ticketId) {

        if (ticketId == null ||
                ticketId.isBlank()) {

            return null;
        }

        return supportDAO.getTicketById(
                ticketId);
    }

    // =========================================================
    // SUBMIT FEEDBACK
    // =========================================================

    public boolean submitFeedback(
            String ticketId,
            int rating,
            String feedback) {

        if (ticketId == null ||
                ticketId.isBlank()) {

            throw new IllegalArgumentException(
                    "Ticket ID is required.");
        }

        if (rating < 1 ||
                rating > 5) {

            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5.");
        }

        return supportDAO.updateFeedback(
                ticketId,
                rating,
                feedback);
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    public boolean updateStatus(
            String ticketId,
            String status,
            String resolution) {

        if (ticketId == null ||
                ticketId.isBlank()) {

            throw new IllegalArgumentException(
                    "Ticket ID is required.");
        }

        if (status == null ||
                status.isBlank()) {

            throw new IllegalArgumentException(
                    "Status is required.");
        }

        return supportDAO.updateStatus(
                ticketId,
                status,
                resolution);
    }

    // =========================================================
    // UPDATE TIMELINE
    // =========================================================

    public boolean updateTimeline(
            String ticketId,
            List<TimelineEvent> timeline) {

        if (ticketId == null ||
                ticketId.isBlank()) {

            throw new IllegalArgumentException(
                    "Ticket ID is required.");
        }

        return supportDAO.updateTimeline(
                ticketId,
                timeline);
    }
}
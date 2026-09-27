package com.super_x.controller.usercontroller;

import com.super_x.dao.userdao.SupportDAO;
import com.super_x.model.usermodel.SupportTicket;
import com.super_x.model.usermodel.TimelineEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SupportController {

    private final SupportDAO supportDAO;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(
                    "dd MMM yyyy • hh:mm a");

    public SupportController(
            SupportDAO supportDAO) {

        this.supportDAO = supportDAO;
    }

    // =========================================================
    // CREATE TICKET
    // =========================================================

    public SupportTicket createTicket(
            String userEmail,
            String issueType,
            String priority,
            String subject,
            String description,
            String attachment) {

        if (userEmail == null ||
                userEmail.isBlank()) {

            throw new IllegalArgumentException(
                    "User email not found.");
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

        String now =
                LocalDateTime.now()
                        .format(FORMATTER);

        SupportTicket ticket =
                new SupportTicket();

        ticket.setUserEmail(userEmail);
        ticket.setRole("USER");

        ticket.setIssueType(issueType);
        ticket.setPriority(priority);
        ticket.setSubject(subject);
        ticket.setDescription(description);

        ticket.setStatus("Open");

        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);

        ticket.setAttachment(attachment);

        ticket.setResolution(
                "Your support request has been received.");

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
                        "Your ticket has been assigned "
                                + "to the EcoLoad support team.",
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

        ticket.setTimeline(timeline);

        boolean saved =
                supportDAO.createTicket(ticket);

        if (!saved) {

            throw new RuntimeException(
                    "Failed to save support ticket.");
        }

        return ticket;
    }

    // =========================================================
    // GET USER TICKETS
    // =========================================================

    public List<SupportTicket> getUserTickets(
            String userEmail) {

        return supportDAO.getTicketsByUser(
                userEmail);
    }

    // =========================================================
    // GET ONE TICKET
    // =========================================================

    public SupportTicket getTicket(
            String ticketId) {

        return supportDAO.getTicketById(
                ticketId);
    }

    // =========================================================
    // FEEDBACK
    // =========================================================

    public boolean submitFeedback(
            String ticketId,
            int rating,
            String feedback) {

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
}
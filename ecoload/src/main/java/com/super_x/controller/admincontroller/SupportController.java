package com.super_x.controller.admincontroller;

import com.super_x.dao.admindao.SupportTicketDAO;
import com.super_x.model.adminmodel.SupportTicket;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SupportController {

    private final SupportTicketDAO supportTicketDAO;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public SupportController() {

        supportTicketDAO =
                new SupportTicketDAO();
    }

    // ============================================================
    // GET ALL SUPPORT TICKETS
    // ============================================================

    public List<SupportTicket> getAllTickets() {

        return supportTicketDAO.getAllTickets();
    }

    public boolean updateTicketFromAdmin(
            SupportTicket ticket) {

        if (ticket == null || ticket.getTicketId() == null || ticket.getTicketId().isBlank()) {
            return false;
        }

        String updatedAt = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd MMM yyyy • hh:mm a"));
        boolean updated = supportTicketDAO.updateAdminTicket(
                ticket.getTicketId(),
                ticket.getStatus(),
                ticket.getAssignedTo(),
                ticket.getAdminResponse(),
                updatedAt);
        if (updated) {
            ticket.setUpdatedAt(updatedAt);
        }
        return updated;
    }

    // ============================================================
    // GET SINGLE SUPPORT TICKET
    // ============================================================

    public SupportTicket getTicketById(
            String ticketId
    ) {

        if (ticketId == null ||
                ticketId.trim().isEmpty()) {

            return null;
        }

        return supportTicketDAO.getTicketById(
                ticketId
        );
    }

    // ============================================================
    // COUNT OPEN TICKETS
    // ============================================================

    public long countOpen(
            List<SupportTicket> tickets
    ) {

        return tickets.stream()
                .filter(ticket ->
                        "Open".equalsIgnoreCase(
                                ticket.getStatus()
                        )
                )
                .count();
    }

    // ============================================================
    // COUNT IN PROGRESS TICKETS
    // ============================================================

    public long countInProgress(
            List<SupportTicket> tickets
    ) {

        return tickets.stream()
                .filter(ticket ->
                        "In Progress".equalsIgnoreCase(
                                ticket.getStatus()
                        )
                )
                .count();
    }

    // ============================================================
    // COUNT RESOLVED TICKETS
    // ============================================================

    public long countResolved(
            List<SupportTicket> tickets
    ) {

        return tickets.stream()
                .filter(ticket ->
                        "Resolved".equalsIgnoreCase(
                                ticket.getStatus()
                        )
                )
                .count();
    }
}

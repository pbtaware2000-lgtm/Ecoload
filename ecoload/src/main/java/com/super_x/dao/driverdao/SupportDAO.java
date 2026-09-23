package com.super_x.dao.driverdao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import com.super_x.model.drivermodel.SupportTicket;
import com.super_x.model.drivermodel.TimelineEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupportDAO {

    private final Firestore firestore;

    private static final String COLLECTION =
            "supportTickets";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SupportDAO(Firestore firestore) {
        this.firestore = firestore;
    }

    // =========================================================
    // CREATE DRIVER TICKET
    // =========================================================

    public boolean createTicket(SupportTicket ticket) {

        try {

            if (ticket == null) {
                System.err.println(
                        "Cannot create null support ticket.");
                return false;
            }

            if (ticket.getId() == null ||
                    ticket.getId().isBlank()) {

                System.err.println(
                        "Ticket ID is missing.");
                return false;
            }

            DocumentReference document =
                    firestore.collection(COLLECTION)
                            .document(ticket.getId());

            Map<String, Object> data =
                    ticketToMap(ticket);

            document.set(data).get();

            System.out.println(
                    "====================================");

            System.out.println(
                    "DRIVER SUPPORT TICKET CREATED");

            System.out.println(
                    "Ticket ID: " +
                            ticket.getId());

            System.out.println(
                    "Driver Email: " +
                            ticket.getDriverEmail());

            System.out.println(
                    "Role: " +
                            ticket.getRole());

            System.out.println(
                    "====================================");

            return true;

        } catch (Exception e) {

            System.err.println(
                    "Error creating driver support ticket.");

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET ONE TICKET
    // =========================================================

    public SupportTicket getTicketById(
            String ticketId) {

        try {

            if (ticketId == null ||
                    ticketId.isBlank()) {

                return null;
            }

            DocumentSnapshot snapshot =
                    firestore.collection(COLLECTION)
                            .document(ticketId)
                            .get()
                            .get();

            if (!snapshot.exists()) {

                System.out.println(
                        "Ticket not found: " +
                                ticketId);

                return null;
            }

            return mapToTicket(snapshot);

        } catch (Exception e) {

            System.err.println(
                    "Error fetching ticket: " +
                            ticketId);

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // GET DRIVER TICKETS
    //
    // IMPORTANT:
    // Only DRIVER tickets belonging to this
    // driver's email are returned.
    // =========================================================

    public List<SupportTicket> getTicketsByDriver(
            String driverEmail) {

        List<SupportTicket> tickets =
                new ArrayList<>();

        try {

            if (driverEmail == null ||
                    driverEmail.isBlank()) {

                return tickets;
            }

            Query query =
                    firestore.collection(COLLECTION)
                            .whereEqualTo(
                                    "driverEmail",
                                    driverEmail)
                            .whereEqualTo(
                                    "role",
                                    "DRIVER");

            QuerySnapshot snapshot =
                    query.get().get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                SupportTicket ticket =
                        mapToTicket(document);

                if (ticket != null) {
                    tickets.add(ticket);
                }
            }

            System.out.println(
                    "Driver tickets loaded: " +
                            tickets.size());

        } catch (Exception e) {

            System.err.println(
                    "Error fetching driver tickets.");

            e.printStackTrace();
        }

        return tickets;
    }

    // =========================================================
    // GET ALL DRIVER TICKETS
    //
    // This method can be used by DRIVER dashboard
    // if you intentionally want every driver's ticket.
    //
    // DO NOT use this for personal "My Tickets".
    // =========================================================

    public List<SupportTicket> getAllDriverTickets() {

        List<SupportTicket> tickets =
                new ArrayList<>();

        try {

            Query query =
                    firestore.collection(COLLECTION)
                            .whereEqualTo(
                                    "role",
                                    "DRIVER");

            QuerySnapshot snapshot =
                    query.get().get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                SupportTicket ticket =
                        mapToTicket(document);

                if (ticket != null) {
                    tickets.add(ticket);
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Error fetching all driver tickets.");

            e.printStackTrace();
        }

        return tickets;
    }

    // =========================================================
    // UPDATE FEEDBACK
    // =========================================================

    public boolean updateFeedback(
            String ticketId,
            int rating,
            String feedback) {

        try {

            if (rating < 1 ||
                    rating > 5) {

                System.err.println(
                        "Rating must be between 1 and 5.");

                return false;
            }

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "rating",
                    rating);

            updates.put(
                    "feedback",
                    feedback);

            updates.put(
                    "updatedAt",
                    getCurrentTime());

            firestore.collection(COLLECTION)
                    .document(ticketId)
                    .update(updates)
                    .get();

            System.out.println(
                    "Feedback updated: " +
                            ticketId);

            return true;

        } catch (Exception e) {

            System.err.println(
                    "Error updating feedback.");

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    public boolean updateStatus(
            String ticketId,
            String status,
            String resolution) {

        try {

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "status",
                    status);

            updates.put(
                    "resolution",
                    resolution);

            updates.put(
                    "updatedAt",
                    getCurrentTime());

            firestore.collection(COLLECTION)
                    .document(ticketId)
                    .update(updates)
                    .get();

            System.out.println(
                    "Ticket status updated: " +
                            ticketId);

            return true;

        } catch (Exception e) {

            System.err.println(
                    "Error updating ticket status.");

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // UPDATE TIMELINE
    // =========================================================

    public boolean updateTimeline(
            String ticketId,
            List<TimelineEvent> timeline) {

        try {

            List<Map<String, Object>> timelineData =
                    new ArrayList<>();

            if (timeline != null) {

                for (TimelineEvent event :
                        timeline) {

                    Map<String, Object> eventMap =
                            new HashMap<>();

                    eventMap.put(
                            "action",
                            event.getAction());

                    eventMap.put(
                            "description",
                            event.getDescription());

                    eventMap.put(
                            "time",
                            event.getTime());

                    eventMap.put(
                            "completed",
                            event.isCompleted());

                    timelineData.add(eventMap);
                }
            }

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "timeline",
                    timelineData);

            updates.put(
                    "updatedAt",
                    getCurrentTime());

            firestore.collection(COLLECTION)
                    .document(ticketId)
                    .update(updates)
                    .get();

            return true;

        } catch (Exception e) {

            System.err.println(
                    "Error updating timeline.");

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // CONVERT MODEL → FIRESTORE
    // =========================================================

    private Map<String, Object> ticketToMap(
            SupportTicket ticket) {

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "id",
                ticket.getId());

        data.put(
                "driverEmail",
                ticket.getDriverEmail());

        data.put("driverName", ticket.getDriverName());

        data.put(
                "role",
                ticket.getRole());

        data.put(
                "issueType",
                ticket.getIssueType());

        data.put(
                "priority",
                ticket.getPriority());

        data.put(
                "subject",
                ticket.getSubject());

        data.put(
                "description",
                ticket.getDescription());

        data.put(
                "status",
                ticket.getStatus());

        data.put(
                "createdAt",
                ticket.getCreatedAt());

        data.put(
                "updatedAt",
                ticket.getUpdatedAt());

        data.put(
                "resolution",
                ticket.getResolution());

        data.put(
                "attachment",
                ticket.getAttachment());

        data.put(
                "rating",
                ticket.getRating());

        data.put(
                "feedback",
                ticket.getFeedback());

        // =====================================================
        // TIMELINE
        // =====================================================

        List<Map<String, Object>> timeline =
                new ArrayList<>();

        if (ticket.getTimeline() != null) {

            for (TimelineEvent event :
                    ticket.getTimeline()) {

                Map<String, Object> eventMap =
                        new HashMap<>();

                eventMap.put(
                        "action",
                        event.getAction());

                eventMap.put(
                        "description",
                        event.getDescription());

                eventMap.put(
                        "time",
                        event.getTime());

                eventMap.put(
                        "completed",
                        event.isCompleted());

                timeline.add(eventMap);
            }
        }

        data.put(
                "timeline",
                timeline);

        return data;
    }

    // =========================================================
    // FIRESTORE → MODEL
    // =========================================================

    @SuppressWarnings("unchecked")
    private SupportTicket mapToTicket(
            DocumentSnapshot document) {

        SupportTicket ticket =
                new SupportTicket();

        ticket.setId(
                document.getString("id"));

        ticket.setDriverEmail(
                document.getString("driverEmail"));

        ticket.setDriverName(document.getString("driverName"));

        ticket.setRole(
                document.getString("role"));

        ticket.setIssueType(
                document.getString("issueType"));

        ticket.setPriority(
                document.getString("priority"));

        ticket.setSubject(
                document.getString("subject"));

        ticket.setDescription(
                document.getString("description"));

        ticket.setStatus(
                document.getString("status"));

        ticket.setCreatedAt(
                document.getString("createdAt"));

        ticket.setUpdatedAt(
                document.getString("updatedAt"));

        ticket.setResolution(
                document.getString("resolution"));

        ticket.setAssignedTo(
                document.getString("assignedTo"));

        ticket.setAdminResponse(
                document.getString("adminResponse"));

        ticket.setAttachment(
                document.getString("attachment"));

        Long rating =
                document.getLong("rating");

        ticket.setRating(
                rating != null
                        ? rating.intValue()
                        : 0);

        ticket.setFeedback(
                document.getString("feedback"));

        // =====================================================
        // TIMELINE
        // =====================================================

        List<Map<String, Object>> timeline =
                (List<Map<String, Object>>)
                        document.get("timeline");

        List<TimelineEvent> events =
                new ArrayList<>();

        if (timeline != null) {

            for (Map<String, Object> eventMap :
                    timeline) {

                TimelineEvent event =
                        new TimelineEvent();

                event.setAction(
                        (String)
                                eventMap.get("action"));

                event.setDescription(
                        (String)
                                eventMap.get("description"));

                event.setTime(
                        (String)
                                eventMap.get("time"));

                Boolean completed =
                        (Boolean)
                                eventMap.get("completed");

                event.setCompleted(
                        completed != null &&
                                completed);

                events.add(event);
            }
        }

        ticket.setTimeline(events);

        return ticket;
    }

    // =========================================================
    // CURRENT TIME
    // =========================================================

    private String getCurrentTime() {

        return java.time.LocalDateTime
                .now()
                .format(
                        java.time.format.DateTimeFormatter
                                .ofPattern(
                                        "dd MMM yyyy • hh:mm a"));
    }
}

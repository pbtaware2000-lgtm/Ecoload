package com.super_x.dao.userdao;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.super_x.model.usermodel.SupportTicket;
import com.super_x.model.usermodel.TimelineEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SupportDAO {

    private static final String COLLECTION = "supportTickets";

    private final Firestore firestore;

    public SupportDAO(Firestore firestore) {
        this.firestore = firestore;
    }

    // =========================================================
    // CREATE TICKET
    // =========================================================

    public boolean createTicket(SupportTicket ticket) {

        try {

            String ticketId =
                    "EL-" +
                    UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase();

            ticket.setId(ticketId);

            Map<String, Object> data =
                    new HashMap<>();

            data.put("id", ticket.getId());
            data.put("userEmail", ticket.getUserEmail());
            data.put("role", ticket.getRole());

            data.put("issueType", ticket.getIssueType());
            data.put("priority", ticket.getPriority());
            data.put("subject", ticket.getSubject());
            data.put("description", ticket.getDescription());

            data.put("status", ticket.getStatus());
            data.put("createdAt", ticket.getCreatedAt());
            data.put("updatedAt", ticket.getUpdatedAt());

            data.put("attachment", ticket.getAttachment());
            data.put("resolution", ticket.getResolution());

            data.put("rating", ticket.getRating());
            data.put("feedback", ticket.getFeedback());

            data.put(
                    "timeline",
                    timelineToFirestore(
                            ticket.getTimeline()));

            firestore
                    .collection(COLLECTION)
                    .document(ticketId)
                    .set(data)
                    .get();

            System.out.println(
                    "Support ticket saved: " +
                    ticketId);

            return true;

        } catch (Exception e) {

            System.err.println(
                    "ERROR: Could not save support ticket");

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET USER TICKETS
    // =========================================================

    public List<SupportTicket> getTicketsByUser(
            String userEmail) {

        List<SupportTicket> tickets =
                new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future =
                    firestore
                            .collection(COLLECTION)
                            .whereEqualTo(
                                    "userEmail",
                                    userEmail)
                            .get();

            QuerySnapshot snapshot =
                    future.get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                SupportTicket ticket =
                        documentToTicket(document);

                if (ticket != null) {
                    tickets.add(ticket);
                }
            }

            return tickets;

        } catch (Exception e) {

            System.err.println(
                    "ERROR: Could not load user tickets");

            e.printStackTrace();

            return tickets;
        }
    }

    // =========================================================
    // GET SINGLE TICKET
    // =========================================================

    public SupportTicket getTicketById(
            String ticketId) {

        try {

            DocumentSnapshot document =
                    firestore
                            .collection(COLLECTION)
                            .document(ticketId)
                            .get()
                            .get();

            if (!document.exists()) {
                return null;
            }

            return documentToTicket(document);

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // UPDATE FEEDBACK
    // =========================================================

    public boolean updateFeedback(
            String ticketId,
            int rating,
            String feedback) {

        try {

            firestore
                    .collection(COLLECTION)
                    .document(ticketId)
                    .update(
                            "rating",
                            rating,
                            "feedback",
                            feedback)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // TIMELINE → FIRESTORE
    // =========================================================

    private List<Map<String, Object>>
    timelineToFirestore(
            List<TimelineEvent> timeline) {

        List<Map<String, Object>> result =
                new ArrayList<>();

        if (timeline == null) {
            return result;
        }

        for (TimelineEvent event : timeline) {

            Map<String, Object> map =
                    new HashMap<>();

            map.put(
                    "title",
                    event.getTitle());

            map.put(
                    "description",
                    event.getDescription());

            map.put(
                    "time",
                    event.getTime());

            map.put(
                    "completed",
                    event.isCompleted());

            result.add(map);
        }

        return result;
    }

    // =========================================================
    // FIRESTORE → MODEL
    // =========================================================

    @SuppressWarnings("unchecked")

    private String getFirestoreString(DocumentSnapshot document, String field) {
    Object value = document.get(field);

    if (value == null) {
        return null;
    }

    if (value instanceof Timestamp) {
        return value.toString();
    }

    return String.valueOf(value);
}
    private SupportTicket documentToTicket(
            DocumentSnapshot document) {

        SupportTicket ticket =
                new SupportTicket();

        ticket.setId(
                document.getString("id"));

        ticket.setUserEmail(
                document.getString("userEmail"));

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
        getFirestoreString(document, "createdAt"));

        ticket.setUpdatedAt(
        getFirestoreString(document, "updatedAt"));

        ticket.setAttachment(
        getFirestoreString(document, "attachment"));

        ticket.setResolution(
                document.getString("resolution"));

        ticket.setAssignedTo(
                document.getString("assignedTo"));

        ticket.setAdminResponse(
                document.getString("adminResponse"));

        Long rating =
                document.getLong("rating");

        ticket.setRating(
                rating == null
                        ? 0
                        : rating.intValue());

        ticket.setFeedback(
                document.getString("feedback"));

        List<Map<String, Object>> rawTimeline =
                (List<Map<String, Object>>)
                        document.get("timeline");

        List<TimelineEvent> timeline =
                new ArrayList<>();

        if (rawTimeline != null) {

            for (Map<String, Object> map :
                    rawTimeline) {

                TimelineEvent event =
                        new TimelineEvent();

                event.setTitle(
                        String.valueOf(
                                map.get("title")));

                event.setDescription(
                        String.valueOf(
                                map.get("description")));

                event.setTime(
                        String.valueOf(
                                map.get("time")));

                Object completed =
                        map.get("completed");

                event.setCompleted(
                        completed instanceof Boolean
                                && (Boolean) completed);

                timeline.add(event);
            }
        }

        ticket.setTimeline(timeline);

        return ticket;
    }
}

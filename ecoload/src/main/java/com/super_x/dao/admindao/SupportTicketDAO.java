package com.super_x.dao.admindao;

import com.super_x.config.FirebaseConfig;
import com.super_x.model.adminmodel.SupportTicket;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupportTicketDAO {

    private static final String COLLECTION = "supportTickets";

    // ============================================================
    // GET ALL SUPPORT TICKETS
    // ============================================================

    public List<SupportTicket> getAllTickets() {

        List<SupportTicket> tickets = new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future =
                    FirebaseConfig.getFireStore()
                            .collection(COLLECTION)
                            .get();

            QuerySnapshot snapshot = future.get();

            for (DocumentSnapshot document : snapshot.getDocuments()) {

                SupportTicket ticket =
                        documentToTicket(document);

                tickets.add(ticket);
            }

            System.out.println(
                    "Support tickets fetched: " + tickets.size()
            );

        } catch (Exception e) {

            System.err.println(
                    "Error fetching support tickets:"
            );

            e.printStackTrace();
        }

        return tickets;
    }


    // ============================================================
    // GET HIGH PRIORITY SUPPORT TICKETS
    // ============================================================

    public List<SupportTicket> getHighPriorityTickets() {

        List<SupportTicket> tickets = new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future =
                    FirebaseConfig.getFireStore()
                            .collection(COLLECTION)
                            .whereEqualTo("priority", "High")
                            .get();

            QuerySnapshot snapshot = future.get();

            for (DocumentSnapshot document : snapshot.getDocuments()) {

                SupportTicket ticket =
                        documentToTicket(document);

                tickets.add(ticket);
            }

            System.out.println(
                    "High priority tickets fetched: "
                            + tickets.size()
            );

        } catch (Exception e) {

            System.err.println(
                    "Error fetching high priority tickets:"
            );

            e.printStackTrace();
        }

        return tickets;
    }


    // ============================================================
    // GET ACTIVE HIGH PRIORITY SUPPORT TICKETS
    //
    // priority = High
    // status   = Open
    //
    // SOS ALERTS साठी हा method वापरणार आहोत.
    // ============================================================

    public List<SupportTicket> getActiveHighPriorityTickets() {

        List<SupportTicket> tickets = new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future =
                    FirebaseConfig.getFireStore()
                            .collection(COLLECTION)
                            .whereEqualTo("priority", "High")
                            .whereEqualTo("status", "Open")
                            .get();

            QuerySnapshot snapshot = future.get();

            for (DocumentSnapshot document : snapshot.getDocuments()) {

                SupportTicket ticket =
                        documentToTicket(document);

                tickets.add(ticket);
            }

            System.out.println(
                    "Active High priority tickets fetched: "
                            + tickets.size()
            );

        } catch (Exception e) {

            System.err.println(
                    "Error fetching active high priority tickets:"
            );

            e.printStackTrace();
        }

        return tickets;
    }


    // ============================================================
    // GET SINGLE SUPPORT TICKET
    // ============================================================

    public SupportTicket getTicketById(String ticketId) {

        try {

            DocumentSnapshot document =
                    FirebaseConfig.getFireStore()
                            .collection(COLLECTION)
                            .document(ticketId)
                            .get()
                            .get();

            if (!document.exists()) {

                System.out.println(
                        "Ticket not found: " + ticketId
                );

                return null;
            }

            return documentToTicket(document);

        } catch (Exception e) {

            System.err.println(
                    "Error fetching ticket: " + ticketId
            );

            e.printStackTrace();

            return null;
        }
    }

    /** Persists the fields an administrator can change from SupportPage. */
    public boolean updateAdminTicket(
            String ticketId,
            String status,
            String assignedTo,
            String adminResponse,
            String updatedAt) {

        if (ticketId == null || ticketId.isBlank()) {
            return false;
        }

        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("status", status == null ? "" : status);
            updates.put("assignedTo", assignedTo == null ? "" : assignedTo);
            updates.put("adminResponse", adminResponse == null ? "" : adminResponse);
            updates.put("updatedAt", updatedAt == null ? "" : updatedAt);

            FirebaseConfig.getFireStore()
                    .collection(COLLECTION)
                    .document(ticketId)
                    .update(updates)
                    .get();
            return true;
        } catch (Exception error) {
            System.err.println("Error updating support ticket " + ticketId + ": "
                    + error.getMessage());
            return false;
        }
    }


    // ============================================================
    // CONVERT FIRESTORE DOCUMENT TO SUPPORT TICKET
    // ============================================================

    private SupportTicket documentToTicket(
            DocumentSnapshot document
    ) {

        SupportTicket ticket =
                new SupportTicket();


        // ========================================================
        // TICKET ID
        // ========================================================

        ticket.setTicketId(
                getString(
                        document,
                        "id",
                        document.getId()
                )
        );


        // ========================================================
        // SUBJECT
        // ========================================================

        ticket.setSubject(
                getString(
                        document,
                        "subject"
                )
        );


        // ========================================================
        // ISSUE TYPE
        // ========================================================

        ticket.setIssueType(
                getString(
                        document,
                        "issueType"
                )
        );


        // ========================================================
        // DESCRIPTION
        // ========================================================

        ticket.setDescription(
                getString(
                        document,
                        "description"
                )
        );


        // ========================================================
        // PRIORITY
        // ========================================================

        ticket.setPriority(
                getString(
                        document,
                        "priority"
                )
        );


        // ========================================================
        // STATUS
        // ========================================================

        ticket.setStatus(
                getString(
                        document,
                        "status"
                )
        );


        // ========================================================
        // USER EMAIL
        // ========================================================

        ticket.setUserEmail(
                getString(
                        document,
                        "userEmail"
                )
        );

        ticket.setUserName(
                getString(
                        document,
                        "userName"
                )
        );

        // Driver-created tickets use their own identity fields rather than
        // userEmail/userName, so map them before the admin table renders.
        ticket.setDriverName(
                getString(
                        document,
                        "driverName"
                )
        );

        ticket.setDriverEmail(
                getString(
                        document,
                        "driverEmail"
                )
        );


        // ========================================================
        // CREATED AT
        //
        // Firebase field:
        // createdAt
        // ========================================================

        ticket.setCreated(
                getString(
                        document,
                        "createdAt"
                )
        );


        // ========================================================
        // UPDATED AT
        // ========================================================

        ticket.setUpdatedAt(
                getString(
                        document,
                        "updatedAt"
                )
        );


        // ========================================================
        // ROLE
        // ========================================================

        ticket.setRole(
                getString(
                        document,
                        "role"
                )
        );

        ticket.setUserType(
                getString(
                        document,
                        "userType"
                )
        );

        ticket.setAssignedTo(
                getString(
                        document,
                        "assignedTo"
                )
        );

        ticket.setAdminResponse(
                getString(
                        document,
                        "adminResponse"
                )
        );


        // ========================================================
        // RESOLUTION
        // ========================================================

        ticket.setResolution(
                getString(
                        document,
                        "resolution"
                )
        );


        // ========================================================
        // FEEDBACK
        // ========================================================

        ticket.setFeedback(
                getString(
                        document,
                        "feedback"
                )
        );


        // ========================================================
        // RATING
        // ========================================================

        ticket.setRating(
                getInt(
                        document,
                        "rating"
                )
        );


        // ========================================================
        // ATTACHMENT
        // ========================================================

        String attachment =
                getString(
                        document,
                        "attachment"
                );

        ticket.setAttachment(
                attachment
        );


        // ========================================================
        // TIMELINE
        // ========================================================

        Object timelineObject =
                document.get("timeline");

        if (timelineObject instanceof List) {

            List<?> rawTimeline =
                    (List<?>) timelineObject;

            List<SupportTicket.TimelineItem> timeline =
                    new ArrayList<>();

            for (Object itemObject : rawTimeline) {

                if (itemObject instanceof Map) {

                    @SuppressWarnings("unchecked")
                    Map<String, Object> item =
                            (Map<String, Object>) itemObject;


                    boolean completed =
                            getBoolean(
                                    item,
                                    "completed"
                            );


                    String description =
                            getMapString(
                                    item,
                                    "description"
                            );


                    String time =
                            getMapString(
                                    item,
                                    "time"
                            );


                    String title =
                            getMapString(
                                    item,
                                    "title"
                            );


                    SupportTicket.TimelineItem timelineItem =
                            new SupportTicket.TimelineItem(
                                    completed,
                                    description,
                                    time,
                                    title
                            );

                    timeline.add(
                            timelineItem
                    );
                }
            }

            ticket.setTimeline(
                    timeline
            );

        } else {

            ticket.setTimeline(
                    new ArrayList<>()
            );
        }


        return ticket;
    }


    // ============================================================
    // GET STRING FROM FIRESTORE
    // ============================================================

    private String getString(
            DocumentSnapshot document,
            String field
    ) {

        return getString(
                document,
                field,
                ""
        );
    }


    // ============================================================
    // GET STRING WITH DEFAULT VALUE
    // ============================================================

    private String getString(
            DocumentSnapshot document,
            String field,
            String defaultValue
    ) {

        Object value =
                document.get(field);

        if (value == null) {

            return defaultValue;
        }

        return String.valueOf(value);
    }


    // ============================================================
    // GET INTEGER
    // ============================================================

    private int getInt(
            DocumentSnapshot document,
            String field
    ) {

        Object value =
                document.get(field);

        if (value == null) {

            return 0;
        }

        if (value instanceof Number) {

            return ((Number) value).intValue();
        }

        try {

            return Integer.parseInt(
                    String.valueOf(value)
            );

        } catch (Exception e) {

            return 0;
        }
    }


    // ============================================================
    // GET BOOLEAN FROM MAP
    // ============================================================

    private boolean getBoolean(
            Map<String, Object> map,
            String field
    ) {

        Object value =
                map.get(field);

        if (value == null) {

            return false;
        }

        if (value instanceof Boolean) {

            return (Boolean) value;
        }

        return Boolean.parseBoolean(
                String.valueOf(value)
        );
    }


    // ============================================================
    // GET STRING FROM MAP
    // ============================================================

    private String getMapString(
            Map<String, Object> map,
            String field
    ) {

        Object value =
                map.get(field);

        if (value == null) {

            return "";
        }

        return String.valueOf(value);
    }
}

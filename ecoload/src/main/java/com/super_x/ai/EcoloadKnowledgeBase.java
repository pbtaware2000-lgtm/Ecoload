package com.super_x.ai;

import java.util.Locale;

/**
 * Source-derived product facts used by the Ecoload support chatbot. Keep this
 * class aligned with implemented application behaviour, not planned features.
 */
public final class EcoloadKnowledgeBase {

    private EcoloadKnowledgeBase() {
    }

    public static String relevantTo(String question, String audience) {
        String normalizedQuestion = question == null ? "" : question.toLowerCase(Locale.ROOT);
        StringBuilder knowledge = new StringBuilder(commonKnowledge(audience));

        if (containsAny(normalizedQuestion, "book", "post", "shipment", "load", "driver accept", "accept")) {
            knowledge.append("\n\nLOAD WORKFLOW:\n")
                    .append("A transporter posts a load from Post Load after accepting terms. " )
                    .append("The form records pickup and drop locations, receiver, load type, weight/unit, required truck type, offer price, and pickup/delivery date and time. " )
                    .append("The saved load initially has PENDING status. A driver can accept only a PENDING load when the vehicle capacity is sufficient. " )
                    .append("Driver acceptance assigns the driver and creates or adds the load to a trip. There is no separate implemented 'book trip' button.\n");
        }

        if (containsAny(normalizedQuestion, "payment", "pay", "razorpay", "successful payment")) {
            knowledge.append("\nPAYMENT WORKFLOW:\n")
                    .append("In My Loads, an accepted load appears as Approved with Payment Pending. " )
                    .append("The user enters an amount, creates a Razorpay payment link, completes payment in the browser, and then presses Verify in the app. " )
                    .append("Only when Razorpay reports the payment link as paid does Ecoload record payment identifiers and timestamp, set paymentStatus to PAYMENT_COMPLETED, move the load to PICKUP, and activate the associated trip. Closing the payment browser alone does not complete payment.\n");
        }

        if (containsAny(normalizedQuestion, "track", "active trip", "status", "delivery", "history")) {
            knowledge.append("\nTRIP WORKFLOW:\n")
                    .append("Transporters can open Trip Tracking from their sidebar to view the active trip, driver, load, and progress. " )
                    .append("Drivers use Active Trip and the driver dashboard. The implemented progress order is PICKUP, DISPATCH, IN TRANSIT, ARRIVED, then DELIVERED. " )
                    .append("When delivery is marked complete, linked loads are marked COMPLETED. Drivers can open Trip History from their sidebar to view their Firebase trip records.\n");
        }

        if (containsAny(normalizedQuestion, "wallet", "balance", "withdraw", "payout")) {
            knowledge.append("\nWALLET LIMITATION:\n")
                    .append("The current Ecoload source does not implement a wallet, balance, withdrawal, transaction ledger, or payout workflow. Do not describe one as available.\n");
        }

        if (containsAny(normalizedQuestion, "matched driver", "assign driver", "match")) {
            knowledge.append("\nMATCHED DRIVERS LIMITATION:\n")
                    .append("The Matched Drivers screen displays driver information and an assignment confirmation UI, but its assignment action is currently a placeholder and does not persist an assignment.\n");
        }

        return knowledge.toString();
    }

    private static String commonKnowledge(String audience) {
        String role = audience == null || audience.isBlank() ? "Ecoload user" : audience;
        return """
                ECOLOAD IMPLEMENTED APPLICATION KNOWLEDGE
                Current audience: %s.
                Ecoload is a JavaFX freight application with transporter and driver dashboards backed by Firebase Firestore.
                Transporter navigation includes Dashboard, Post Load, My Loads, Matched Drivers, Trip Tracking, Analytics, Rating & Review, and Support.
                Driver navigation includes Dashboard, Available Loads, Active Trip, Trip History, Ratings, Support, and Truck Wala.
                Answer only from these supplied facts. If a requested feature or procedure is not described here, say that the feature information is not available in the current Ecoload application.
                """.formatted(role);
    }

    private static boolean containsAny(String text, String... terms) {
        for (String term : terms) {
            if (text.contains(term)) {
                return true;
            }
        }
        return false;
    }
}

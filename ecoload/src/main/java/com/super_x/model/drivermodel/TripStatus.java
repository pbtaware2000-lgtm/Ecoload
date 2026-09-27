package com.super_x.model.drivermodel;

/** Single source of truth for driver trip-progress statuses. */
public enum TripStatus {
    PICKUP, DISPATCH, IN_TRANSIT, ARRIVED, DELIVERED;

    public static TripStatus fromFirestore(String value) {
        if (value == null || value.isBlank()) return PICKUP;
        return switch (value.trim().toUpperCase().replace(' ', '_')) {
            case "ACTIVE", "ACCEPTED", "PICKUP" -> PICKUP;
            case "PICKUP_COMPLETED", "DISPATCH" -> DISPATCH;
            case "IN_TRANSIT" -> IN_TRANSIT;
            case "DELIVERY", "ARRIVED" -> ARRIVED;
            case "COMPLETED", "DELIVERED" -> DELIVERED;
            default -> PICKUP;
        };
    }

    public TripStatus next() {
        return switch (this) {
            case PICKUP -> DISPATCH;
            case DISPATCH -> IN_TRANSIT;
            case IN_TRANSIT -> ARRIVED;
            case ARRIVED, DELIVERED -> DELIVERED;
        };
    }

    public String firestoreValue() { return name().replace('_', ' '); }

    public String nextAction() {
        return switch (this) {
            case PICKUP -> "Start Dispatch";
            case DISPATCH -> "Start Transit";
            case IN_TRANSIT -> "Mark Arrived";
            case ARRIVED -> "Mark Delivered";
            case DELIVERED -> "Trip Completed";
        };
    }
}

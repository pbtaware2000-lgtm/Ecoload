package com.super_x.model.delivery;

import java.time.LocalDateTime;

/** Immutable delivery-proof record read from Firestore. */
public record DeliveryProof(
        String tripId,
        String loadId,
        String driverId,
        String userId,
        String receiverName,
        String deliveryPhotoUrl,
        String signatureUrl,
        LocalDateTime deliveredAt,
        String deliveryStatus) {
}

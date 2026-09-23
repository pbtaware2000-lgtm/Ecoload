package com.super_x.model.drivermodel;

import java.time.LocalDateTime;

/** Verified driver-credit transaction projected from walletTransactions. */
public record EarningsTransaction(
        String tripId,
        String loadId,
        double amount,
        String status,
        LocalDateTime timestamp,
        String dateDisplay,
        String description) {
}

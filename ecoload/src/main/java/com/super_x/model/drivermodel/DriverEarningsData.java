package com.super_x.model.drivermodel;

import java.util.List;

/** Read-only driver earnings snapshot calculated from wallet and verified credits. */
public record DriverEarningsData(
        double totalEarnings,
        double walletBalance,
        double thisMonthEarnings,
        List<EarningsTransaction> transactions) {
}

package com.super_x.dao.driverdao;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.super_x.config.FirebaseConfig;
import com.super_x.model.drivermodel.DriverEarningsData;
import com.super_x.model.drivermodel.EarningsTransaction;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Reads only the logged-in driver's verified wallet-credit records. */
public class DriverEarningsDAO {
    private static final DateTimeFormatter STORED_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Firestore db = FirebaseConfig.getFireStore();

    public DriverEarningsData loadVerifiedEarnings(String driverId) throws Exception {
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("Driver information unavailable.");
        }
        String normalizedDriverId = driverId.trim();
        DocumentSnapshot wallet = db.collection("wallets")
                .document("driver_" + safeId(normalizedDriverId)).get().get();

        List<EarningsTransaction> credits = new ArrayList<>();
        for (QueryDocumentSnapshot document : db.collection("walletTransactions")
                .whereEqualTo("driverId", normalizedDriverId).get().get().getDocuments()) {
            if (!"CREDIT".equalsIgnoreCase(document.getString("type"))
                    || !"COMPLETED".equalsIgnoreCase(document.getString("status"))) {
                continue;
            }
            credits.add(new EarningsTransaction(
                    value(document.getString("tripId")),
                    value(document.getString("loadId")),
                    number(document.get("amount")),
                    value(document.getString("status")),
                    toLocalDateTime(document.get("timestamp")),
                    displayDate(document.get("timestamp")),
                    value(document.getString("description"))));
        }
        credits.sort(Comparator.comparing(EarningsTransaction::timestamp,
                Comparator.nullsLast(Comparator.reverseOrder())));

        double verifiedTotal = credits.stream().mapToDouble(EarningsTransaction::amount).sum();
        Object storedTotal = wallet.exists() ? wallet.get("totalEarnings") : null;
        // A missing/non-numeric wallet total falls back to the verified ledger sum.
        double walletTotal = storedTotal instanceof Number ? number(storedTotal) : verifiedTotal;
        double balance = wallet.exists() ? number(wallet.get("balance")) : 0d;
        YearMonth currentMonth = YearMonth.now();
        double thisMonth = credits.stream()
                .filter(transaction -> transaction.timestamp() != null
                        && YearMonth.from(transaction.timestamp()).equals(currentMonth))
                .mapToDouble(EarningsTransaction::amount).sum();
        return new DriverEarningsData(walletTotal, balance, thisMonth, List.copyOf(credits));
    }

    private LocalDateTime toLocalDateTime(Object value) {
        if (value instanceof Timestamp timestamp) {
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()), ZoneId.systemDefault());
        }
        if (value instanceof java.util.Date date) return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        if (value == null) return null;
        String text = value.toString().trim();
        try { return LocalDateTime.parse(text, STORED_TIME); }
        catch (DateTimeParseException ignored) {
            try { return LocalDateTime.parse(text); }
            catch (DateTimeParseException ignoredAgain) {
                try { return LocalDate.parse(text).atStartOfDay(); }
                catch (DateTimeParseException ignoredThird) { return null; }
            }
        }
    }

    private String displayDate(Object value) {
        LocalDateTime parsed = toLocalDateTime(value);
        return parsed == null ? "N/A" : parsed.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }
    private static double number(Object value) { return value instanceof Number number ? number.doubleValue() : 0d; }
    private static String value(String value) { return value == null || value.isBlank() ? "N/A" : value; }
    private static String safeId(String value) { return value.replace('/', '_'); }
}

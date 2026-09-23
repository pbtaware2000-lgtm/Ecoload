package com.super_x.dao.admindao;

import com.super_x.config.FirebaseConfig;
import com.super_x.model.adminmodel.Review;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReviewDAO {

    private static final DateTimeFormatter REVIEW_CREATED_AT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ============================================================
    // GET ALL ACTIVE APP REVIEWS
    // ============================================================

    public List<Review> getActiveAppReviews() {

        List<Review> reviews = new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future =
                    FirebaseConfig.getFireStore()
                            .collection("reviews")
                            .get();

            QuerySnapshot snapshot = future.get();

            for (DocumentSnapshot document : snapshot.getDocuments()) {

                String targetType =
                        getString(document, "targetType");

                String targetMail =
                        getString(document, "targetMail");

                String status =
                        getString(document, "status");

                // ------------------------------------------------
                // ONLY ACTIVE APP REVIEWS
                // ------------------------------------------------

                if (!"APP".equalsIgnoreCase(targetType)) {
                    continue;
                }

                if (!"APP".equalsIgnoreCase(targetMail)) {
                    continue;
                }

                if (!"ACTIVE".equalsIgnoreCase(status)) {
                    continue;
                }

                // ------------------------------------------------
                // CREATE REVIEW MODEL
                // ------------------------------------------------

                Review review = new Review();

                review.setId(document.getId());

                review.setReviewerMail(
                        getString(
                                document,
                                "reviewerMail"
                        )
                );

                review.setReviewerRole(
                        getString(
                                document,
                                "reviewerRole"
                        )
                );

                review.setRating(
                        getRating(document)
                );

                review.setComment(
                        getString(
                                document,
                                "comment"
                        )
                );

                review.setCreatedAt(
                        getString(
                                document,
                                "createdAt"
                        )
                );

                review.setStatus(
                        status
                );

                review.setTargetMail(
                        targetMail
                );

                review.setTargetType(
                        targetType
                );

                review.setTripId(
                        getString(
                                document,
                                "tripId"
                        )
                );

                reviews.add(review);
            }

            // ----------------------------------------------------
            // NEWEST REVIEW FIRST
            // ----------------------------------------------------

            reviews.sort(
                    Comparator.comparing(
                            Review::getCreatedAt,
                            Comparator.nullsLast(
                                    Comparator.reverseOrder()
                            )
                    )
            );

        } catch (Exception e) {

            System.err.println(
                    "Error loading reviews from Firebase:"
            );

            e.printStackTrace();
        }

        return reviews;
    }

    // ============================================================
    // GET ALL REVIEWS
    // ============================================================

    public List<Review> getAllReviews() {

        List<Review> reviews = new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future =
                    FirebaseConfig.getFireStore()
                            .collection("reviews")
                            .get();

            QuerySnapshot snapshot = future.get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Review review = new Review();

                review.setId(
                        document.getId()
                );

                review.setReviewerMail(
                        getString(
                                document,
                                "reviewerMail"
                        )
                );

                review.setReviewerRole(
                        getString(
                                document,
                                "reviewerRole"
                        )
                );

                review.setRating(
                        getRating(document)
                );

                review.setComment(
                        getString(
                                document,
                                "comment"
                        )
                );

                review.setCreatedAt(
                        getString(
                                document,
                                "createdAt"
                        )
                );

                review.setStatus(
                        getString(
                                document,
                                "status"
                        )
                );

                review.setTargetMail(
                        getString(
                                document,
                                "targetMail"
                        )
                );

                review.setTargetType(
                        getString(
                                document,
                                "targetType"
                        )
                );

                review.setTripId(
                        getString(
                                document,
                                "tripId"
                        )
                );

                reviews.add(review);
            }

            // Newest first
            reviews.sort(
                    Comparator.comparing(
                            Review::getCreatedAt,
                            Comparator.nullsLast(
                                    Comparator.reverseOrder()
                            )
                    )
            );

        } catch (Exception e) {

            System.err.println(
                    "Error loading all reviews:"
            );

            e.printStackTrace();
        }

        return reviews;
    }

    /**
     * Counts reviews by their persisted {@code createdAt} date for admin
     * analytics. Invalid or missing legacy dates are ignored safely.
     */
    public Map<LocalDate, Integer> getDailyReviewCounts(Instant startTime, ZoneId zoneId) {
        Map<LocalDate, Integer> counts = new TreeMap<>();
        ZoneId effectiveZone = zoneId == null ? ZoneId.systemDefault() : zoneId;
        LocalDate startDate = startTime == null ? null
                : startTime.atZone(effectiveZone).toLocalDate();

        try {
            QuerySnapshot snapshot = FirebaseConfig.getFireStore()
                    .collection("reviews")
                    .get()
                    .get();

            for (DocumentSnapshot document : snapshot.getDocuments()) {
                LocalDate reviewDate = reviewDate(document.get("createdAt"), effectiveZone);
                if (reviewDate == null || (startDate != null && reviewDate.isBefore(startDate))) {
                    continue;
                }
                counts.merge(reviewDate, 1, Integer::sum);
            }
        } catch (Exception error) {
            System.err.println("Error loading daily review counts from Firebase: "
                    + error.getMessage());
        }

        return counts;
    }

    private LocalDate reviewDate(Object value, ZoneId zoneId) {
        if (value instanceof Timestamp timestamp) {
            return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos())
                    .atZone(zoneId).toLocalDate();
        }
        if (value instanceof Date date) {
            return date.toInstant().atZone(zoneId).toLocalDate();
        }
        if (value == null || String.valueOf(value).isBlank()) {
            return null;
        }

        String text = String.valueOf(value).trim();
        try {
            return LocalDateTime.parse(text, REVIEW_CREATED_AT).toLocalDate();
        } catch (DateTimeParseException ignored) {
            try {
                return LocalDateTime.parse(text).toLocalDate();
            } catch (DateTimeParseException ignoredAgain) {
                try {
                    return LocalDate.parse(text);
                } catch (DateTimeParseException ignoredOnceMore) {
                    return null;
                }
            }
        }
    }

    // ============================================================
    // GET SINGLE REVIEW BY ID
    // ============================================================

    public Review getReviewById(String reviewId) {

        try {

            DocumentSnapshot document =
                    FirebaseConfig.getFireStore()
                            .collection("reviews")
                            .document(reviewId)
                            .get()
                            .get();

            if (!document.exists()) {
                return null;
            }

            Review review = new Review();

            review.setId(
                    document.getId()
            );

            review.setReviewerMail(
                    getString(
                            document,
                            "reviewerMail"
                    )
            );

            review.setReviewerRole(
                    getString(
                            document,
                            "reviewerRole"
                    )
            );

            review.setRating(
                    getRating(document)
            );

            review.setComment(
                    getString(
                            document,
                            "comment"
                    )
            );

            review.setCreatedAt(
                    getString(
                            document,
                            "createdAt"
                    )
            );

            review.setStatus(
                    getString(
                            document,
                            "status"
                    )
            );

            review.setTargetMail(
                    getString(
                            document,
                            "targetMail"
                    )
            );

            review.setTargetType(
                    getString(
                            document,
                            "targetType"
                    )
            );

            review.setTripId(
                    getString(
                            document,
                            "tripId"
                    )
            );

            return review;

        } catch (Exception e) {

            System.err.println(
                    "Error loading review: "
                            + reviewId
            );

            e.printStackTrace();

            return null;
        }
    }

    // ============================================================
    // GET STRING FIELD
    // ============================================================

    private String getString(
            DocumentSnapshot document,
            String field
    ) {

        Object value =
                document.get(field);

        if (value == null) {
            return "";
        }

        return String.valueOf(value);
    }

    // ============================================================
    // GET RATING
    // ============================================================

    private int getRating(
            DocumentSnapshot document
    ) {

        Object value =
                document.get("rating");

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
}

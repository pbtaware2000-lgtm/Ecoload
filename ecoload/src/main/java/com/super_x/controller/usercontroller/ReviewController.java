package com.super_x.controller.usercontroller;

import com.super_x.dao.userdao.ReviewDAO;
import com.super_x.model.reviewmodel.ReviewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReviewController {

    private final ReviewDAO reviewDAO = new ReviewDAO();

    

    public List<ReviewModel> getDriverReviews(
            String driverMail) {

        if (driverMail == null
                || driverMail.trim().isEmpty()) {

            return new ArrayList<>();
        }

        return reviewDAO.getReviewsForDriver(
                driverMail);
    }
    // =========================================================
    // USER → DRIVER REVIEW
    // =========================================================

    public boolean submitDriverReview(
            String userMail,
            String driverMail,
            String tripId,
            int rating,
            String comment) {

        // Check user email
        if (userMail == null
                || userMail.trim().isEmpty()) {

            System.out.println(
                    "User email is missing.");

            return false;
        }

        // Check driver email
        if (driverMail == null
                || driverMail.trim().isEmpty()) {

            System.out.println(
                    "Driver email is missing.");

            return false;
        }

        // Check trip ID
        if (tripId == null
                || tripId.trim().isEmpty()) {

            System.out.println(
                    "Trip ID is missing.");

            return false;
        }

        // Check rating
        if (rating < 1 || rating > 5) {

            System.out.println(
                    "Rating must be between 1 and 5.");

            return false;
        }

        // Check comment
        if (comment == null
                || comment.trim().isEmpty()) {

            System.out.println(
                    "Review comment is empty.");

            return false;
        }

        // =====================================================
        // CREATE REVIEW
        // =====================================================

        String createdAt = LocalDateTime.now()
                .format(
                        DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss"));

        ReviewModel review = new ReviewModel();

        review.setReviewerMail(
                userMail);

        review.setReviewerRole(
                "USER");

        review.setTargetMail(
                driverMail);

        review.setTargetType(
                "DRIVER");

        review.setTripId(
                tripId);

        review.setRating(
                rating);

        review.setComment(
                comment.trim());

        review.setCreatedAt(
                createdAt);

        review.setStatus(
                "ACTIVE");

        // =====================================================
        // SAVE REVIEW
        // =====================================================

        return reviewDAO.saveDriverReview(
                review);
    }

    // =========================================================
// USER → APP REVIEW
// =========================================================

public boolean submitAppReview(
        String userMail,
        int rating,
        String comment
) {

    if (userMail == null
            || userMail.trim().isEmpty()) {

        System.out.println(
                "User email is missing."
        );

        return false;
    }

    if (rating < 1 || rating > 5) {

        System.out.println(
                "Rating must be between 1 and 5."
        );

        return false;
    }

    if (comment == null
            || comment.trim().isEmpty()) {

        System.out.println(
                "Review comment is empty."
        );

        return false;
    }

    String createdAt =
            LocalDateTime.now()
                    .format(
                            DateTimeFormatter.ofPattern(
                                    "yyyy-MM-dd HH:mm:ss"
                            )
                    );

    ReviewModel review =
            new ReviewModel();

    review.setReviewerMail(
            userMail
    );

    review.setReviewerRole(
            "USER"
    );

    review.setTargetMail(
            "APP"
    );

    review.setTargetType(
            "APP"
    );

    review.setTripId(
            null
    );

    review.setRating(
            rating
    );

    review.setComment(
            comment.trim()
    );

    review.setCreatedAt(
            createdAt
    );

    review.setStatus(
            "ACTIVE"
    );

    return reviewDAO.saveAppReview(
            review
    );
}
// =========================================================
// DRIVER → APP REVIEW
// =========================================================

public boolean submitDriverAppReview(
        String driverMail,
        int rating,
        String comment
) {

    if (driverMail == null
            || driverMail.trim().isEmpty()) {

        System.out.println(
                "Driver email is missing."
        );

        return false;
    }

    if (rating < 1 || rating > 5) {

        System.out.println(
                "Rating must be between 1 and 5."
        );

        return false;
    }

    if (comment == null
            || comment.trim().isEmpty()) {

        System.out.println(
                "Review comment is empty."
        );

        return false;
    }

    String createdAt =
            LocalDateTime.now()
                    .format(
                            DateTimeFormatter.ofPattern(
                                    "yyyy-MM-dd HH:mm:ss"
                            )
                    );

    ReviewModel review =
            new ReviewModel();

    review.setReviewerMail(
            driverMail
    );

    review.setReviewerRole(
            "DRIVER"
    );

    review.setTargetMail(
            "APP"
    );

    review.setTargetType(
            "APP"
    );

    review.setTripId(
            null
    );

    review.setRating(
            rating
    );

    review.setComment(
            comment.trim()
    );

    review.setCreatedAt(
            createdAt
    );

    review.setStatus(
            "ACTIVE"
    );

    return reviewDAO.saveAppReview(
            review
    );
}
}
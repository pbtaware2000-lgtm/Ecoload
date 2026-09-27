package com.super_x.controller.admincontroller;

import com.super_x.dao.admindao.ReviewDAO;
import com.super_x.model.adminmodel.Review;

import java.util.List;

public class ReviewController {

    private final ReviewDAO reviewDAO;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public ReviewController() {

        this.reviewDAO = new ReviewDAO();
    }

    // ============================================================
    // GET ACTIVE APP REVIEWS
    // ============================================================

    public List<Review> getActiveAppReviews() {

        return reviewDAO.getActiveAppReviews();
    }

    // ============================================================
    // GET ALL REVIEWS
    // ============================================================

    public List<Review> getAllReviews() {

        return reviewDAO.getAllReviews();
    }

    // ============================================================
    // GET REVIEW BY ID
    // ============================================================

    public Review getReviewById(String reviewId) {

        if (reviewId == null ||
                reviewId.trim().isEmpty()) {

            return null;
        }

        return reviewDAO.getReviewById(reviewId);
    }

    // ============================================================
    // FILTER REVIEWS BY RATING
    // ============================================================

    public List<Review> filterByRating(
            List<Review> reviews,
            int rating
    ) {

        if (reviews == null) {

            return List.of();
        }

        if (rating <= 0) {

            return reviews;
        }

        return reviews.stream()
                .filter(review ->
                        review.getRating() == rating
                )
                .toList();
    }

    // ============================================================
    // CALCULATE AVERAGE RATING
    // ============================================================

    public double calculateAverageRating(
            List<Review> reviews
    ) {

        if (reviews == null ||
                reviews.isEmpty()) {

            return 0.0;
        }

        int totalRating = 0;

        for (Review review : reviews) {

            totalRating += review.getRating();
        }

        return (double) totalRating /
                reviews.size();
    }

    // ============================================================
    // COUNT POSITIVE REVIEWS
    // ============================================================

    public long countPositiveReviews(
            List<Review> reviews
    ) {

        if (reviews == null ||
                reviews.isEmpty()) {

            return 0;
        }

        return reviews.stream()
                .filter(review ->
                        review.getRating() >= 4
                )
                .count();
    }

    // ============================================================
    // CALCULATE POSITIVE PERCENTAGE
    // ============================================================

    public double calculatePositivePercentage(
            List<Review> reviews
    ) {

        if (reviews == null ||
                reviews.isEmpty()) {

            return 0.0;
        }

        long positive =
                countPositiveReviews(reviews);

        return ((double) positive /
                reviews.size()) * 100.0;
    }
}
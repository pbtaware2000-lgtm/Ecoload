package com.super_x.dao.userdao;

import java.util.ArrayList;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.super_x.config.FirebaseConfig;
import com.super_x.model.reviewmodel.ReviewModel;
//import java.util.ArrayList;
import java.util.List;
public class ReviewDAO {

    private final Firestore db = FirebaseConfig.getFireStore();

    // =========================================================
    // SAVE USER → DRIVER REVIEW
    // =========================================================

    public boolean saveDriverReview(
            ReviewModel review) {

        try {

            db.collection("reviews")
                    .add(review)
                    .get();

            System.out.println(
                    "Driver review saved successfully.");

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
// SAVE USER → APP REVIEW
// =========================================================

public boolean saveAppReview(
        ReviewModel review
) {

    try {

        db.collection("reviews")
                .add(review)
                .get();

        System.out.println(
                "App review saved successfully."
        );

        return true;

    } catch (Exception e) {

        e.printStackTrace();

        return false;
    }
}

    // =========================================================
    // FETCH REVIEWS FOR DRIVER
    // =========================================================

    public List<ReviewModel> getReviewsForDriver(
            String driverMail) {

        try {

            List<ReviewModel> reviews = new ArrayList<>();

            for (QueryDocumentSnapshot document : db.collection("reviews")
                    .whereEqualTo(
                            "targetMail",
                            driverMail)
                    .whereEqualTo(
                            "targetType",
                            "DRIVER")
                    .whereEqualTo(
                            "status",
                            "ACTIVE")
                    .get()
                    .get()
                    .getDocuments()) {

                reviews.add(
                        document.toObject(
                                ReviewModel.class));
            }

            return reviews;

        } catch (Exception e) {

            e.printStackTrace();

            return new ArrayList<>();
        }
    }
}
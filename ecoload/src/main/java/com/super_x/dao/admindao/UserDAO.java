package com.super_x.dao.admindao;

import com.super_x.config.FirebaseConfig;
import com.super_x.model.adminmodel.User;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.Timestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UserDAO {

    private final Firestore db;

    public UserDAO() {
        db = FirebaseConfig.getFireStore();
    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection("users")
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                User user =
                        document.toObject(User.class);

                if (user != null) {

                    // Firebase Document ID
                    user.setId(document.getId());

                    users.add(user);

                    System.out.println(
                        "User fetched: "
                                + user.getUsername()
                                + " | ID: "
                                + user.getId()
                                + " | Email: "
                                + user.getEmail()
);

                
                }
            }

            System.out.println(
                    "Total users fetched: "
                            + users.size()
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to fetch users from Firebase."
            );

            e.printStackTrace();
        }

        return users;
    }

    /**
     * Counts registrations by the Firestore document creation date.  User
     * documents do not have an application-level createdAt field, so the
     * immutable server-managed create time is the authoritative registration
     * date and is not altered by profile updates.
     */
    public Map<LocalDate, Integer> getRegistrationCounts(Instant startTime, ZoneId zoneId) {

        Map<LocalDate, Integer> registrations = new TreeMap<>();
        ZoneId effectiveZone = zoneId == null ? ZoneId.systemDefault() : zoneId;

        try {
            QuerySnapshot snapshot = db.collection("users").get().get();

            for (DocumentSnapshot document : snapshot.getDocuments()) {
                Timestamp createTime = document.getCreateTime();
                if (createTime == null) {
                    continue;
                }

                Instant registeredAt = Instant.ofEpochSecond(
                        createTime.getSeconds(), createTime.getNanos());
                if (startTime != null && registeredAt.isBefore(startTime)) {
                    continue;
                }

                LocalDate registrationDate = registeredAt.atZone(effectiveZone).toLocalDate();
                registrations.merge(registrationDate, 1, Integer::sum);
            }
        } catch (Exception error) {
            System.err.println("Failed to fetch user registration counts from Firebase: "
                    + error.getMessage());
        }

        return registrations;
    }
}

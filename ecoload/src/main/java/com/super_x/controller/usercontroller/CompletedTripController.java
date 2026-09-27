package com.super_x.controller.usercontroller;

import com.super_x.dao.userdao.LoadDao;
import com.super_x.dao.driverdao.TripDAO;
import com.super_x.model.usermodel.Load;
import com.super_x.model.drivermodel.Trip;

import java.util.List;

public class CompletedTripController {

    private final LoadDao loadDao = new LoadDao();
    private final TripDAO tripDAO = new TripDAO();

    public Trip getLatestCompletedTripForUser(
        String userId
) throws Exception {

    List<Load> completedLoads =
            loadDao.getCompletedLoadsByUserId(userId);

    if (completedLoads == null
            || completedLoads.isEmpty()) {

        System.out.println(
                "No completed loads found for user: "
                        + userId
        );

        return null;
    }

    Trip latestTrip = null;

    for (Load load : completedLoads) {

        if (load.getLoadId() == null) {
            continue;
        }

        Trip trip =
                tripDAO.getCompletedTripByLoadId(
                        load.getLoadId()
                );

        if (trip == null) {
            continue;
        }

        if (latestTrip == null) {

            latestTrip = trip;

        } else if (
                trip.getCompletedTime() != null
                        && latestTrip.getCompletedTime() != null
                        && trip.getCompletedTime()
                                .compareTo(
                                        latestTrip.getCompletedTime()
                                ) > 0
        ) {

            latestTrip = trip;
        }
    }

    if (latestTrip != null) {

        System.out.println(
                "================================="
        );

        System.out.println(
                "LATEST COMPLETED TRIP"
        );

        System.out.println(
                "Trip ID: "
                        + latestTrip.getTripId()
        );

        System.out.println(
                "Driver: "
                        + latestTrip.getDriverName()
        );

        System.out.println(
                "Driver Email: "
                        + latestTrip.getDriverId()
        );

        System.out.println(
                "Completed Time: "
                        + latestTrip.getCompletedTime()
        );

        System.out.println(
                "================================="
        );

    } else {

        System.out.println(
                "No completed trip found for user."
        );
    }

    return latestTrip;
}
}
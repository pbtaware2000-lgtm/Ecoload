package com.super_x.model.drivermodel;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.LinkedHashSet;

import com.super_x.dao.driverdao.TripDAO;
import com.super_x.dao.userdao.LoadDao;

/**
 * Process-wide owner of the current active trip. A trip is loaded once for the
 * current session key and every screen reads this exact object afterwards.
 */
public final class ActiveTripManager {

    private static final ActiveTripManager INSTANCE = new ActiveTripManager();

    private final TripDAO tripDAO = new TripDAO();
    private final LoadDao loadDao = new LoadDao();
    private final CopyOnWriteArrayList<Consumer<Trip>> observers = new CopyOnWriteArrayList<>();
    private Trip activeTrip;
    private String sessionKey;
    private boolean loaded;

    private ActiveTripManager() { }

    public static ActiveTripManager getInstance() {
        return INSTANCE;
    }

    public synchronized Trip getOrLoadForUser(String userId) throws Exception {
        if (loaded && activeTrip != null && sameId(userId, activeTrip.getUserId())) {
            return activeTrip;
        }
        return getOrLoad("user:" + userId, () -> tripDAO.getActiveTripByUserId(userId));
    }

    public synchronized Trip getOrLoadForDriver(String driverId) throws Exception {
        if (loaded && activeTrip != null && sameId(driverId, activeTrip.getDriverId())) {
            return activeTrip;
        }
        return getOrLoad("driver:" + driverId, () -> tripDAO.getActiveTripByDriverId(driverId));
    }

    private Trip getOrLoad(String key, TripLoader loader) throws Exception {
        if (!loaded || !key.equals(sessionKey)) {
            activeTrip = loader.load();
            sessionKey = key;
            loaded = true;
            notifyObservers();
        }
        return activeTrip;
    }

    public synchronized Trip getActiveTrip() {
        return activeTrip;
    }

    /** Used by acceptance code when a new active-trip document has just been created. */
    public synchronized void setActiveTrip(Trip trip) {
        activeTrip = trip;
        sessionKey = null;
        loaded = true;
        notifyObservers();
    }

    /** Explicit refresh only; pages must not call this from timers or listeners. */
    public synchronized Trip refreshForUser(String userId) throws Exception {
        loaded = false;
        return getOrLoadForUser(userId);
    }

    public synchronized void updateStatus(String status, String time) throws Exception {
        if (activeTrip == null) return;
        TripStatus canonical = TripStatus.fromFirestore(status);
        String persistedStatus = canonical == TripStatus.DELIVERED
                ? "COMPLETED" : canonical.firestoreValue();
        tripDAO.updateTripStatus(activeTrip.getTripId(), persistedStatus, time);
        activeTrip.setStatus(persistedStatus);
        if (canonical == TripStatus.DELIVERED) {
            // Keep the session object consistent with the Firestore record as
            // soon as a trip completes. Location lookups still read Firestore.
            activeTrip.setCompletedTime(time);
            LinkedHashSet<String> loadIds = new LinkedHashSet<>();
            if (activeTrip.getLoadId() != null) loadIds.add(activeTrip.getLoadId());
            if (activeTrip.getLoadIds() != null) loadIds.addAll(activeTrip.getLoadIds());
            for (String loadId : loadIds) {
                if (loadId != null && !loadId.isBlank()) {
                    loadDao.updateLoadStatusOnly(loadId, "COMPLETED");
                }
            }
        }
        notifyObservers();
    }

    public void addObserver(Consumer<Trip> observer) {
        observers.addIfAbsent(observer);
    }

    public void removeObserver(Consumer<Trip> observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Consumer<Trip> observer : observers) observer.accept(activeTrip);
    }

    private boolean sameId(String requestedId, String tripId) {
        return requestedId != null && tripId != null
                && requestedId.trim().equalsIgnoreCase(tripId.trim());
    }

    @FunctionalInterface
    private interface TripLoader {
        Trip load() throws Exception;
    }
}

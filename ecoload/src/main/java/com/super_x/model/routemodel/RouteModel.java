package com.super_x.model.routemodel;

public class RouteModel {

    private String origin;
    private String destination;

    private double distanceKm;
    private String duration;

    private String encodedPolyline;

    public RouteModel() {
    }

    // =========================================================
    // ORIGIN
    // =========================================================

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    // =========================================================
    // DESTINATION
    // =========================================================

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    // =========================================================
    // DISTANCE
    // =========================================================

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    // =========================================================
    // DURATION / ETA
    // =========================================================

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    // =========================================================
    // POLYLINE
    // =========================================================

    public String getEncodedPolyline() {
        return encodedPolyline;
    }

    public void setEncodedPolyline(String encodedPolyline) {
        this.encodedPolyline = encodedPolyline;
    }
}
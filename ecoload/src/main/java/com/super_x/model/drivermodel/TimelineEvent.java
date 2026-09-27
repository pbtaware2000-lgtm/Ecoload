package com.super_x.model.drivermodel;

public class TimelineEvent {

    private String action;
    private String description;
    private String time;
    private boolean completed;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public TimelineEvent() {
    }

    // =========================================================
    // PARAMETERIZED CONSTRUCTOR
    // =========================================================

    public TimelineEvent(
            String action,
            String description,
            String time,
            boolean completed) {

        this.action = action;
        this.description = description;
        this.time = time;
        this.completed = completed;
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public boolean isCompleted() {
        return completed;
    }

    // =========================================================
    // SETTERS
    // =========================================================

    public void setAction(String action) {
        this.action = action;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
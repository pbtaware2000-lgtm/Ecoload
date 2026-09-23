package com.super_x.model.usermodel;

public class TimelineEvent {

    private String title;
    private String description;
    private String time;
    private boolean completed;

    public TimelineEvent() {
    }

    public TimelineEvent(
            String title,
            String description,
            String time,
            boolean completed) {

        this.title = title;
        this.description = description;
        this.time = time;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
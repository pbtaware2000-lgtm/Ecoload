
package com.super_x;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;


import java.util.function.Consumer;
/**
 * Application-wide Navigation Service.
 *
 * Handles:
 * 1. Registering scenes
 * 2. Navigating between scenes
 * 3. Changing the main Stage title
 * 4. Providing shortcut methods for admin pages
 */
public class NavigationService {

    private final Stage stage;

    private final Map<String, Scene> scenes = new HashMap<>();
    private final Map<String, String> titles = new HashMap<>();

    private Consumer<String> navigationHandler;

    /**
     * Creates NavigationService using the main application Stage.
     */

    public void setNavigationHandler(Consumer<String> navigationHandler) {
    this.navigationHandler = navigationHandler;
}

    public NavigationService(Stage stage) {
        this.stage = stage;
    }

    /**
     * Register a scene with a key and title.
     */
    public void register(String key, Scene scene, String title) {
        scenes.put(key, scene);
        titles.put(key, title);
    }

    /**
     * Navigate to a registered scene.
     */

    public void navigate(String key) {

    if (navigationHandler != null) {
        navigationHandler.accept(key);
    }

    Scene scene = scenes.get(key);

    if (scene != null) {
        stage.setScene(scene);

        String title = titles.get(key);

        if (title != null) {
            stage.setTitle(title);
        }
    }
}
    // public void navigate(String key) {

    //     Scene scene = scenes.get(key);

    //     if (scene == null) {
    //         System.out.println(
    //             "Navigation failed: No scene registered for key: " + key
    //         );
    //         return;
    //     }

    //     stage.setScene(scene);

    //     String title = titles.get(key);

    //     if (title != null) {
    //         stage.setTitle(title);
    //     }
    // }

    // =========================
    // ADMIN PAGE NAVIGATION
    // =========================

    public void showDashboard() {
        navigate("dashboard");
    }

    public void showDrivers() {
        navigate("drivers");
    }

    public void showPendingDrivers() {
        navigate("pendingDrivers");
    }

    public void showLoadsTrips() {
        navigate("loads");
    }

    public void showReports() {
        navigate("reports");
    }

    public void showReviews() {
        navigate("reviews");
    }

    public void showSupport() {
        navigate("support");
    }

    public void showProfile() {
        navigate("profile");
    }

    public void toggleSOSAlerts() {
        navigate("toggleAlerts");
    }

    /*
     * Driver Documents is currently not registered
     * as a separate main scene.
     *
     * If you later decide to make it a separate scene,
     * uncomment this method and register the scene.
     */
    // public void showDriverDocuments() {
    //     navigate("driverDocuments");
    // }
}


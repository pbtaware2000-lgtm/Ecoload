package com.super_x.view.DriverView;

import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class TruckWala {

    public Scene getTruckWalaScene() {

        System.out.println("Creating Truck Wala scene...");

        BorderPane mainroot = new BorderPane();

        // =====================================================
        // SIDEBAR
        // =====================================================

        mainroot.setLeft(
                DriverNavigation.createSidebar("Truck Wala"));

        // =====================================================
        // WEBVIEW
        // =====================================================

        WebView webView = new WebView();

        webView.setPrefSize(1200, 650);

        WebEngine webEngine = webView.getEngine();

        webEngine.setJavaScriptEnabled(true);

        // =====================================================
        // WEBVIEW LOADING STATUS
        // =====================================================

        webEngine.getLoadWorker().stateProperty().addListener(
                (obs, oldState, newState) -> {

                    System.out.println(
                            "WebView State: " + newState);

                    if (newState == Worker.State.SUCCEEDED) {

                        System.out.println(
                                "Highway Raat website loaded!");
                    }

                    if (newState == Worker.State.FAILED) {

                        System.out.println(
                                "Failed to load Highway Raat!");
                    }
                });

        // =====================================================
        // WEBVIEW ERROR
        // =====================================================

        webEngine.getLoadWorker()
                .exceptionProperty()
                .addListener(
                        (obs, oldException, newException) -> {

                            if (newException != null) {

                                System.out.println(
                                        "WebView Error: "
                                                + newException);
                            }
                        });

        // =====================================================
        // LOAD MUSIC WEBSITE
        // =====================================================

        webEngine.load(
                "https://deluxesalon.in/playlists/highway-raat");

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        BorderPane main = new BorderPane();

        main.setTop(
                DriverNavigation.createNavbar());

        main.setCenter(webView);

        mainroot.setCenter(main);

        // =====================================================
        // SCENE
        // =====================================================

        return new Scene(
                mainroot,
                1536,
                750);
    }
}
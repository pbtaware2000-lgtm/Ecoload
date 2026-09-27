package com.super_x.view.UserView;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class UserPlaceholder {

    private final String pageName;

    public UserPlaceholder(String pageName) {
        this.pageName = pageName;
    }

    public Scene getScene() {

        BorderPane root = new BorderPane();

        // =====================================================
        // SIDEBAR
        // =====================================================

        root.setLeft(
                UserNavigation.createSidebar(pageName));

        // =====================================================
        // NAVBAR
        // =====================================================

        BorderPane mainContent = new BorderPane();
        mainContent.setTop(UserNavigation.createNavbar());

        // =====================================================
        // CENTER
        // =====================================================

        VBox content = new VBox(15);
        mainContent.setCenter(content);

        content.setAlignment(
                Pos.CENTER);

        Label title = new Label(
                pageName);

        title.setStyle(
                "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #014B3A;");

        Label message = new Label(
                "This is the " + pageName + " page.");

        message.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: #64748B;");

        content.getChildren().addAll(
                title,
                message);

        root.setCenter(mainContent);
        // =====================================================
        // SCENE
        // =====================================================

        return new Scene(
                root,
                1536,
                750);
    }
}

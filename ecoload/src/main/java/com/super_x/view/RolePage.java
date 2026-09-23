package com.super_x.view;

import com.super_x.view.DriverView.DriverRegistration;
import com.super_x.view.UserView.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Priority;
import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Circle;

public class RolePage {

    private Scene rolePageScene;

    public Scene getRolePageScene() {

        StackPane root = new StackPane();
        // Back to Home button
        Button backButton = new Button("←  Back to Home");

        backButton.setPrefWidth(160);
        backButton.setPrefHeight(42);

        backButton.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 12;
                -fx-border-color: #16a34a;
                -fx-border-radius: 12;
                -fx-border-width: 1.5;
                -fx-text-fill: #166534;
                -fx-font-size: 14;
                -fx-font-weight: bold;
                -fx-cursor: hand;
                """);

        // Hover effect
        backButton.setOnMouseEntered(e -> {
            backButton.setStyle("""
                    -fx-background-color: #16a34a;
                    -fx-background-radius: 12;
                    -fx-border-color: #16a34a;
                    -fx-border-radius: 12;
                    -fx-border-width: 1.5;
                    -fx-text-fill: white;
                    -fx-font-size: 14;
                    -fx-font-weight: bold;
                    -fx-cursor: hand;
                    """);
        });

        backButton.setOnMouseExited(e -> {
            backButton.setStyle("""
                    -fx-background-color: white;
                    -fx-background-radius: 12;
                    -fx-border-color: #16a34a;
                    -fx-border-radius: 12;
                    -fx-border-width: 1.5;
                    -fx-text-fill: #166534;
                    -fx-font-size: 14;
                    -fx-font-weight: bold;
                    -fx-cursor: hand;
                    """);
        });

        // Go back to HomePage
        backButton.setOnAction(e -> {
            HomePage homePage = new HomePage();
            homePage.showHomePage(HomePage.homeStage);
        });
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(25, 0, 0, 30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ecf7e9 0%, #d7ebd8 35%, #b8d9b3 100%);");

        Circle softGlow1 = new Circle(180, Color.web("#bbf7d0", 0.35));
        softGlow1.setTranslateX(-520);
        softGlow1.setTranslateY(-220);

        Circle softGlow2 = new Circle(120, Color.web("#4ade80", 0.25));
        softGlow2.setTranslateX(520);
        softGlow2.setTranslateY(-240);

        Circle softGlow3 = new Circle(220, Color.web("#86efac", 0.18));
        softGlow3.setTranslateX(420);
        softGlow3.setTranslateY(240);

        Circle softGlow4 = new Circle(150, Color.web("#bbf7d0", 0.2));
        softGlow4.setTranslateX(-460);
        softGlow4.setTranslateY(280);

        VBox content = new VBox(40);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(40));
        content.setStyle(
                "-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 28; -fx-border-radius: 28; -fx-border-color: rgba(34, 197, 94, 0.25); -fx-border-width: 1;");

        // Title
        Label title = new Label("Continue As");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        title.setTextFill(Color.web("#166534"));

        // Subtitle
        Label subtitle = new Label("Choose your role to see the right dashboard");
        subtitle.setFont(Font.font("Arial", 18));
        subtitle.setTextFill(Color.web("#374151"));

        // Small blue line
        Label line = new Label("━━");
        line.setStyle("-fx-text-fill:#2563EB; -fx-font-size:18;");

        VBox heading = new VBox(8);
        heading.setAlignment(Pos.CENTER);
        heading.getChildren().addAll(title, subtitle, line);

        // Cards container
        HBox cards = new HBox(40);
        cards.setAlignment(Pos.CENTER);
        cards.setPadding(new Insets(20, 0, 0, 0));

        // Driver card and Transporter card will be added here
        cards.getChildren().addAll(
                createDriverCard(),
                createTransporterCard());

        content.getChildren().addAll(heading, cards);
        root.getChildren().addAll(softGlow1, softGlow2, softGlow3, softGlow4, content, backButton);
        Scene scene = new Scene(root, 1536, 750);
        rolePageScene = scene;

        return rolePageScene;
    }

    private VBox createDriverCard() {

        VBox card = new VBox(15);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(25));
        card.setPrefSize(430, 560);

        card.setStyle(
                "-fx-background-color: rgba(255,255,255,0.95); -fx-background-radius:24; -fx-border-radius:24; -fx-border-color: rgba(34, 197, 94, 0.18); -fx-border-width: 1;");

        DropShadow shadow = new DropShadow();
        shadow.setRadius(22);
        shadow.setColor(Color.rgb(34, 197, 94, 0.18));
        card.setEffect(shadow);

        card.setOnMouseEntered(e -> {
            card.setTranslateY(-8);
            shadow.setRadius(28);
        });

        card.setOnMouseExited(e -> {
            card.setTranslateY(0);
            shadow.setRadius(18);
        });

        ImageView image = new ImageView(
                new Image("assets\\images\\DriverRole.png"));

        image.setFitWidth(340);
        image.setFitHeight(200);
        image.setPreserveRatio(false);

        Label title = new Label("DRIVER");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setTextFill(Color.web("#134e4a"));

        Label subtitle = new Label("HAUL & EARN");
        subtitle.setStyle("-fx-text-fill: #0d4bc8; -fx-font-weight:bold;");

        VBox features = new VBox(12);

        features.getChildren().addAll(
                createFeature("Find delivery jobs near you"),
                createFeature("Haul packages and earn money"),
                createFeature("Track earnings and history"));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btn = new Button("Continue as Driver  →");
        btn.setPrefWidth(330);
        btn.setPrefHeight(50);

        btn.setOnAction(e -> {
            DriverRegistration driverRegistration = new DriverRegistration();
            HomePage.homeStage.setScene(driverRegistration.getDriverRegistrationScene());
        });

        btn.setStyle("""
                -fx-background-color:#16a34a;
                -fx-background-radius:16;
                -fx-text-fill:white;
                -fx-font-size:16;
                -fx-font-weight:bold;
                """);

        btn.setOnMouseEntered(e -> {
            btn.setStyle("""
                    -fx-background-color:#059669;
                    -fx-background-radius:16;
                    -fx-text-fill:white;
                    -fx-font-size:16;
                    -fx-font-weight:bold;
                    -fx-cursor:hand
                    """);
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle("""
                    -fx-background-color:#16a34a;
                    -fx-background-radius:16;
                    -fx-text-fill:white;
                    -fx-font-size:16;
                    -fx-font-weight:bold;
                    """);
        });

        card.getChildren().addAll(image, title, subtitle, features, spacer, btn);
        return card;
    }

    private HBox createFeature(String text) {

        Label icon = new Label("✔");

        icon.setStyle("""
                -fx-text-fill:white;
                -fx-background-color:#2563EB;
                -fx-background-radius:20;
                -fx-padding:3 6 3 6;
                -fx-font-size:10;
                """);

        Label label = new Label(text);
        label.setFont(Font.font(15));

        HBox row = new HBox(10);

        row.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(icon, label);

        return row;
    }

    private VBox createTransporterCard() {

        VBox card = new VBox(15);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(25));
        card.setPrefSize(430, 560);

        card.setStyle("""
                -fx-background-color: rgba(255,255,255,0.95);
                -fx-background-radius:24;
                -fx-border-radius:24;
                -fx-border-color: rgba(16, 185, 129, 0.16);
                -fx-border-width: 1;
                """);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(22);
        shadow.setColor(Color.rgb(16, 185, 129, 0.18));
        card.setEffect(shadow);

        card.setOnMouseEntered(e -> {
            card.setTranslateY(-8);
            shadow.setRadius(28);
        });

        card.setOnMouseExited(e -> {
            card.setTranslateY(0);
            shadow.setRadius(18);
        });

        ImageView image = new ImageView(
                new Image("assets\\images\\UserRole.png"));

        image.setFitWidth(340);
        image.setFitHeight(220);
        image.setPreserveRatio(false);

        Label title = new Label("User");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setTextFill(Color.web("#166534"));

        Label subtitle = new Label("POST & SHIP");
        subtitle.setStyle("""
                -fx-text-fill: #EA580C;
                -fx-font-weight:bold;
                """);

        VBox features = new VBox(12);

        features.getChildren().addAll(

                createOrangeFeature("Post load & ship easily"),
                createOrangeFeature("Manage your shipments"),
                createOrangeFeature("Track and update status")

        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btn = new Button("Continue as User →");

        btn.setPrefWidth(330);
        btn.setPrefHeight(50);

        btn.setOnAction(e -> {
            UserRegistration registration = new UserRegistration();
            HomePage.homeStage.setScene(registration.getTransporterRegistrationScene());

        });

        btn.setOnMouseEntered(e -> {
            btn.setStyle("""
                        -fx-background-color: #EA580C;
                        -fx-background-radius:12;
                        -fx-text-fill:white;
                        -fx-font-size:16;
                        -fx-font-weight:bold;
                        -fx-cursor:hand
                    """);
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle("""
                     -fx-background-color:#F97316;
                     -fx-background-radius:12;
                     -fx-text-fill:white;
                    -fx-font-size:16;
                    -fx-font-weight:bold;
                    """);
        });

        btn.setStyle("""
                -fx-background-color:#F97316;
                -fx-background-radius:12;
                -fx-text-fill:white;
                -fx-font-size:16;
                -fx-font-weight:bold;
                """);

        card.getChildren().addAll(image, title, subtitle, features, spacer, btn);
        return card;
    }

    private HBox createOrangeFeature(String text) {

        Label icon = new Label("✔");

        icon.setStyle("""
                -fx-text-fill:white;
                -fx-background-color:#F97316;
                -fx-background-radius:20;
                -fx-padding:3 6 3 6;
                -fx-font-size:10;
                """);

        Label label = new Label(text);
        label.setFont(Font.font(15));

        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(icon, label);

        return row;
    }
}
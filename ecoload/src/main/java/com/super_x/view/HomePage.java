package com.super_x.view;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.util.Duration;

public class HomePage extends Application {
        public static Stage homeStage;

        @Override
        public void start(Stage stage) {

                homeStage = stage;

                SplashScreen splashScreen = new SplashScreen(stage);

                splashScreen.show(() -> {

                        showHomePage(stage);

                });
        }

        public void showHomePage(Stage stage) {
                homeStage = stage;

                // Theme Colors
                String GREEN = "#0B6B2A";
                String DARK_GREEN = "#075A24";
                // String LIGHT_GREEN = "#85B788";
                String BG = "#E6F1E8";
                String TEXT = "#202820";
                // String BORDER = "#55E378";

                // ====================== NAVBAR ======================

                // Logo
                Image logoImage = new Image(
                                getClass().getResource("/assets/icons/Ecoloadnewlogo.png").toExternalForm());

                ImageView logo = new ImageView(logoImage);
                logo.setFitHeight(43);
                logo.setPreserveRatio(true);

                logo.setScaleX(1);
                logo.setScaleY(1);
                logo.setLayoutY(-1);

                Pane logoPane = new Pane(logo);
                logoPane.setPrefSize(100, 50);
                logoPane.setMinSize(100, 50);
                logoPane.setMaxSize(100, 50);

                ScaleTransition logoScale = new ScaleTransition(Duration.millis(200), logo);

                logo.setOnMouseEntered(e -> {
                        logoScale.stop();
                        logoScale.setToX(1.65);
                        logoScale.setToY(1.65);
                        logoScale.playFromStart();
                });

                logo.setOnMouseExited(e -> {
                        logoScale.stop();
                        logoScale.setToX(1.5);
                        logoScale.setToY(1.5);
                        logoScale.playFromStart();
                });

                // Navigation Labels
                Label overview = new Label("Overview");
                Label features = new Label("Features");
                Label reviews = new Label("Reviews");
                Label about = new Label("About Us");
                Label contact = new Label("Contact Us");

                String navStyle = "-fx-font-size:20px;" + "-fx-font-weight:bold;" + "-fx-text-fill:" + TEXT + ";"
                                + "-fx-cursor:hand;";

                overview.setStyle(navStyle);
                features.setStyle(navStyle);
                reviews.setStyle(navStyle);
                about.setStyle(navStyle);
                contact.setStyle(navStyle);

                // Navigation Box
                HBox navItems = new HBox(60);
                navItems.setAlignment(Pos.CENTER);
                navItems.getChildren().addAll(overview, features, reviews, about, contact);

                // Sign Up Button
                Button signUpBtn = new Button("Sign Up");

                signUpBtn.setOnAction(e -> {
                        RolePage rolePage = new RolePage();
                        homeStage.setScene(rolePage.getRolePageScene());
                });

                signUpBtn.setStyle("-fx-background-color:white;" + "-fx-border-color:" + GREEN + ";"
                                + "-fx-border-width:2;" +
                                "-fx-text-fill:" + GREEN + ";" + "-fx-font-size:15px;" + "-fx-font-weight:bold;"
                                + "-fx-padding:6 18;" +
                                "-fx-background-radius:25;" + "-fx-border-radius:25;" + "-fx-cursor:hand;");

                // Login Button
                Button loginBtn = new Button("Login");
                loginBtn.setStyle(
                                "-fx-background-color:" + GREEN + ";" + "-fx-text-fill:white;" + "-fx-font-size:15px;" +
                                                "-fx-font-weight:bold;" + "-fx-padding:6 18;"
                                                + "-fx-background-radius:25;" + "-fx-cursor:hand;");
                loginBtn.setOnAction(e -> {
                        stage.setScene(new Login().getScene());
                });

                // Button Box
                HBox buttonBox = new HBox(15);
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                buttonBox.getChildren().addAll(signUpBtn, loginBtn);

                // Spacer
                Region leftSpacer = new Region();
                Region rightSpacer = new Region();

                HBox.setHgrow(leftSpacer, Priority.ALWAYS);
                HBox.setHgrow(rightSpacer, Priority.ALWAYS);

                // Navbar
                HBox navbar = new HBox(25);
                navbar.setAlignment(Pos.CENTER_LEFT);
                navbar.setPadding(new Insets(8, 40, 8, 40));
                navbar.setStyle("-fx-background-color:white;");
                DropShadow shadow = new DropShadow();
                shadow.setRadius(10);
                shadow.setOffsetY(3);
                shadow.setColor(Color.rgb(0, 0, 0, 0.18));

                navbar.setEffect(shadow);

                navbar.getChildren().addAll(logoPane, leftSpacer, navItems, rightSpacer, buttonBox);

                // HERO SECTION
                HBox hero = new HBox(80);
                hero.setAlignment(Pos.CENTER);
                hero.setPadding(new Insets(70, 80, 70, 80));
                hero.setMinHeight(650);
                hero.setStyle("-fx-background-color:" + BG + ";");

                VBox left = new VBox(25);
                left.setAlignment(Pos.CENTER_LEFT);
                left.setMaxWidth(520);

                Label smallTitle = new Label("SMART FREIGHT SOLUTIONS");

                smallTitle.setStyle("-fx-text-fill:" + GREEN + ";" + "-fx-font-size:14px;" + "-fx-font-weight:bold;"
                                + "-fx-letter-spacing:1px;");

                Label heading = new Label("Smart Logistics for a\nGreener Future");

                heading.setStyle("-fx-font-size:48px;" + "-fx-font-weight:bold;" + "-fx-text-fill:" + TEXT + ";");

                Label description = new Label("Experience the next generation of logistics management.\n" +
                                "Manage drivers, transport vehicles and shipments\n"
                                + "efficiently from one modern platform.");

                description.setStyle("-fx-font-size:18px;" + "-fx-text-fill:#5F5F5F;");

                Button startBtn = new Button("Get Started");

                startBtn.setStyle("-fx-background-color:" + GREEN + ";" + "-fx-text-fill:white;" + "-fx-font-size:16px;"
                                + "-fx-font-weight:bold;" +
                                "-fx-padding:12 28;" + "-fx-background-radius:30;" + "-fx-cursor:hand;");

                startBtn.setOnMouseEntered(e -> startBtn.setStyle(
                                "-fx-background-color:" + DARK_GREEN + ";" + "-fx-text-fill:white;"
                                                + "-fx-font-size:16px;" +
                                                "-fx-font-weight:bold;" + "-fx-padding:12 28;"
                                                + "-fx-background-radius:30;"));

                startBtn.setOnMouseExited(e -> startBtn.setStyle(
                                "-fx-background-color:" + GREEN + ";" + "-fx-text-fill:white;" + "-fx-font-size:16px;" +
                                                "-fx-font-weight:bold;" + "-fx-padding:12 28;"
                                                + "-fx-background-radius:30;"));
                ScaleTransition getStartedScale = new ScaleTransition(Duration.millis(180), startBtn);

                startBtn.setOnMouseEntered(e -> {
                        getStartedScale.setToX(1.08);
                        getStartedScale.setToY(1.08);
                        getStartedScale.playFromStart();
                });

                startBtn.setOnAction(e -> {
                        RolePage rolePage = new RolePage();
                        homeStage.setScene(rolePage.getRolePageScene());
                });

                startBtn.setOnMouseExited(e -> {
                        getStartedScale.setToX(1.0);
                        getStartedScale.setToY(1.0);
                        getStartedScale.playFromStart();
                });

                Button learnBtn = new Button("About Core2Web");

                learnBtn.setStyle(
                                "-fx-background-color:white;" + "-fx-border-color:" + GREEN + ";"
                                                + "-fx-border-width:2;"
                                                + "-fx-text-fill:" + GREEN + ";" +
                                                "-fx-font-size:16px;" + "-fx-font-weight:bold;" + "-fx-padding:12 28;"
                                                + "-fx-background-radius:30;" +
                                                "-fx-border-radius:30;" + "-fx-cursor:hand;");
                ScaleTransition learnMoreScale = new ScaleTransition(Duration.millis(180), learnBtn);

                learnBtn.setOnMouseEntered(e -> {
                        learnMoreScale.setToX(1.08);
                        learnMoreScale.setToY(1.08);
                        learnMoreScale.playFromStart();
                });

                learnBtn.setOnMouseExited(e -> {
                        learnMoreScale.setToX(1.0);
                        learnMoreScale.setToY(1.0);
                        learnMoreScale.playFromStart();
                });
                learnBtn.setOnAction(e -> {

    AboutCore2web aboutPage =
            new AboutCore2web(stage);

    aboutPage.showAboutPage();
});
                HBox buttons = new HBox(15);
                buttons.getChildren().addAll(startBtn, learnBtn);

                // Label trusted = new Label("Trusted by 500+ Businesses");
                // left.getChildren().addAll(smallTitle,heading,description,buttons,trusted);

                Label trusted = new Label("Trusted by 500+ Businesses");

                trusted.setStyle(
                                "-fx-text-fill:#666666;" +
                                                "-fx-font-size:15px;");

                // ====================== STATS ======================

                HBox stats = new HBox(40);
                stats.setAlignment(Pos.CENTER_LEFT);

                VBox stat1 = new VBox(5);
                Label statNumber1 = new Label("500+");
                statNumber1.setStyle(
                                "-fx-font-size:28px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:" + GREEN + ";");

                Label statText1 = new Label("Businesses");
                statText1.setStyle("-fx-text-fill:#666666;");

                stat1.getChildren().addAll(statNumber1, statText1);

                VBox stat2 = new VBox(5);

                Label statNumber2 = new Label("10K+");
                statNumber2.setStyle(
                                "-fx-font-size:28px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:" + GREEN + ";");

                Label statText2 = new Label("Deliveries");
                statText2.setStyle("-fx-text-fill:#666666;");

                stat2.getChildren().addAll(statNumber2, statText2);

                VBox stat3 = new VBox(5);

                Label statNumber3 = new Label("98%");
                statNumber3.setStyle(
                                "-fx-font-size:28px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:" + GREEN + ";");

                Label statText3 = new Label("Success Rate");
                statText3.setStyle("-fx-text-fill:#666666;");

                stat3.getChildren().addAll(statNumber3, statText3);

                stats.getChildren().addAll(
                                stat1,
                                stat2,
                                stat3);

                left.getChildren().addAll(
                                smallTitle,
                                heading,
                                description,
                                buttons,
                                trusted,
                                stats);

                Image truckImage = new Image("assets\\images\\Truck.png");

                ImageView truck = new ImageView(truckImage);
                truck.setFitWidth(600);
                truck.setFitHeight(380);
                truck.setPreserveRatio(false);
                truck.setSmooth(true);

                // Rounded corners
                Rectangle clip = new Rectangle(600, 380);
                clip.setArcWidth(40);
                clip.setArcHeight(40);
                truck.setClip(clip);

                // White card
                StackPane truckCard = new StackPane(truck);
                truckCard.setPadding(new Insets(8));

                truckCard.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 40;" +
                                                "-fx-border-radius: 40;" +
                                                "-fx-border-color: #E5E5E5;" +
                                                "-fx-border-width: 2;");

                // Shadow
                DropShadow shadow1 = new DropShadow();
                shadow1.setRadius(30);
                shadow1.setOffsetY(5);
                shadow1.setColor(Color.rgb(0, 0, 0, 0.25));

                truckCard.setEffect(shadow1);

                // Add to hero
                hero.getChildren().addAll(left, truckCard);
                TranslateTransition floatTruck = new TranslateTransition(Duration.seconds(1.5), truckCard);

                floatTruck.setFromY(0);
                floatTruck.setToY(-18);

                floatTruck.setAutoReverse(true);
                floatTruck.setCycleCount(Animation.INDEFINITE);

                floatTruck.play();

                // ===========================================================Overview=============================================

                HBox overviewSection = new HBox(70);
                overviewSection.setAlignment(Pos.CENTER);
                overviewSection.setPadding(new Insets(80, 80, 80, 80));
                overviewSection.setStyle("-fx-background-color:white;");

                Image overviewImage = new Image("assets\\images\\overview.jpeg");

                ImageView overview1 = new ImageView(overviewImage);
                overview1.setFitWidth(500);
                overview1.setFitHeight(330);
                overview1.setPreserveRatio(false);
                overview1.setSmooth(true);
                Rectangle clip1 = new Rectangle();
                clip1.widthProperty().bind(overview1.fitWidthProperty());
                clip1.heightProperty().bind(overview1.fitHeightProperty());
                clip1.setArcWidth(40);
                clip1.setArcHeight(40);

                overview1.setClip(clip1);
                // Image Container
                StackPane imageCard = new StackPane();
                imageCard.getChildren().add(overview1);

                imageCard.setPadding(new Insets(6));

                imageCard.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 20;" +
                                                "-fx-border-color: " + GREEN + ";" +
                                                "-fx-border-width: 3;" +
                                                "-fx-border-radius: 20;");

                // Shadow
                DropShadow imageShadow = new DropShadow();
                imageShadow.setRadius(15);
                imageShadow.setOffsetY(8);
                imageShadow.setColor(Color.rgb(0, 0, 0, 0.18));

                imageCard.setEffect(imageShadow);
                ScaleTransition breathe = new ScaleTransition(Duration.seconds(2), overview1);

                breathe.setFromX(1.0);
                breathe.setFromY(1.0);

                breathe.setToX(1.03);
                breathe.setToY(1.03);

                breathe.setAutoReverse(true);
                breathe.setCycleCount(Animation.INDEFINITE);

                breathe.play();

                VBox overviewText = new VBox(20);
                overviewText.setAlignment(Pos.CENTER_LEFT);
                overviewText.setMaxWidth(500);

                Label overviewTitle = new Label("OVERVIEW");

                overviewTitle.setStyle(
                                "-fx-font-size:18px;" + "-fx-font-weight:bold;" + "-fx-text-fill:" + GREEN + ";");
                Label overviewHeading = new Label("Transforming Logistics\nwith Smart Technology");

                overviewHeading.setStyle(
                                "-fx-font-size:38px;" + "-fx-font-weight:bold;" + "-fx-text-fill:" + TEXT + ";");
                Label overviewDesc = new Label(
                                "EcoLoad provides an easy way to manage\n"
                                                + "drivers, shipments and transport vehicles.");

                overviewDesc.setStyle("-fx-font-size:18px;" + "-fx-text-fill:#666666;");

                Label item1 = new Label("✓  Transport Management");
                Label item2 = new Label("✓  Driver Management");
                Label item3 = new Label("✓  Shipment Tracking");
                Label item4 = new Label("✓  Route Optimization");
                String listStyle = "-fx-font-size:17px;" + "-fx-text-fill:" + TEXT + ";";

                item1.setStyle(listStyle);
                item2.setStyle(listStyle);
                item3.setStyle(listStyle);
                item4.setStyle(listStyle);

                overviewText.getChildren().addAll(overviewTitle, overviewHeading, overviewDesc, item1, item2, item3,
                                item4);

                overviewSection.getChildren().addAll(imageCard, overviewText);

                // =====================================================FEATURES==========================================
                VBox featureSection = new VBox(50);
                featureSection.setAlignment(Pos.CENTER);
                featureSection.setPadding(new Insets(80, 80, 80, 80));
                featureSection.setStyle("-fx-background-color:white;");

                Label featureTitle = new Label("OUR FEATURES");

                featureTitle.setStyle("-fx-font-size:40px;" + "-fx-font-weight:bold;" + "-fx-text-fill:" + TEXT + ";");

                HBox featureRow = new HBox(30);
                featureRow.setAlignment(Pos.CENTER);

                String cardStyle = "-fx-background-color:white;" + "-fx-background-radius:18;" + "-fx-border-radius:18;"
                                +
                                "-fx-border-color:#E5E5E5;" + "-fx-border-width:1;" + "-fx-padding:25;";

                VBox card1 = new VBox(15);
                card1.setAlignment(Pos.TOP_CENTER);
                card1.setPrefWidth(220);
                card1.setStyle(cardStyle);

                Label icon1 = new Label("🚚");
                icon1.setStyle("-fx-font-size:32px;");

                Label title1 = new Label("Fast Delivery");
                title1.setStyle("-fx-font-size:20px;" + "-fx-font-weight:bold;");

                Label text1 = new Label("Deliver shipments\nquickly and safely.");

                text1.setWrapText(true);
                text1.setAlignment(Pos.CENTER);

                card1.getChildren().addAll(icon1, title1, text1);

                // card2
                VBox card2 = new VBox(15);
                card2.setAlignment(Pos.TOP_CENTER);
                card2.setPrefWidth(220);
                card2.setStyle(cardStyle);

                ImageView trackingIcon = new ImageView(new Image("assets\\images\\tracker.png"));
                trackingIcon.setFitWidth(45);
                trackingIcon.setFitHeight(45);
                trackingIcon.setPreserveRatio(true);

                Label title2 = new Label("Live Tracking");
                title2.setStyle("-fx-font-size:20px;" + "-fx-font-weight:bold;");

                Label text2 = new Label("Track your shipments in\nreal time from pickup\nto delivery.");

                text2.setWrapText(true);
                text2.setAlignment(Pos.CENTER);

                card2.getChildren().addAll(trackingIcon, title2, text2);

                // card3
                VBox card3 = new VBox(15);
                card3.setAlignment(Pos.TOP_CENTER);
                card3.setPrefWidth(220);
                card3.setStyle(cardStyle);

                ImageView securityIcon = new ImageView(new Image("assets\\images\\security.png"));
                securityIcon.setFitWidth(45);
                securityIcon.setFitHeight(45);
                securityIcon.setPreserveRatio(true);

                Label title3 = new Label("Secure Transport");
                title3.setStyle("-fx-font-size:20px;" + "-fx-font-weight:bold;");

                Label text3 = new Label("Safe transportation with\nverified drivers and\nsecure deliveries.");

                text3.setWrapText(true);
                text3.setAlignment(Pos.CENTER);

                card3.getChildren().addAll(securityIcon, title3, text3);

                // card4
                VBox card4 = new VBox(15);
                card4.setAlignment(Pos.TOP_CENTER);
                card4.setPrefWidth(220);
                card4.setStyle(cardStyle);

                ImageView ecoIcon = new ImageView(new Image("assets\\images\\eco.png"));
                ecoIcon.setFitWidth(45);
                ecoIcon.setFitHeight(70);
                ecoIcon.setPreserveRatio(true);

                Label title4 = new Label("Eco Friendly");
                title4.setStyle("-fx-font-size:20px;" + "-fx-font-weight:bold;");

                Label text4 = new Label("Reduce carbon emissions\nthrough smart and\nefficient logistics.");

                text4.setWrapText(true);
                text4.setAlignment(Pos.CENTER);

                card4.getChildren().addAll(ecoIcon, title4, text4);

                featureRow.getChildren().addAll(card1, card2, card3, card4);

                featureSection.getChildren().addAll(featureTitle, featureRow);

                DropShadow cardShadow = new DropShadow();
                cardShadow.setRadius(12);
                cardShadow.setOffsetY(4);
                cardShadow.setColor(Color.rgb(0, 0, 0, 0.12));

                card1.setEffect(cardShadow);
                card2.setEffect(cardShadow);
                card3.setEffect(cardShadow);
                card4.setEffect(cardShadow);

                // ====================== CARD 1 HOVER ======================

                ScaleTransition cardScale1 = new ScaleTransition(Duration.millis(180), card1);
                ScaleTransition iconScale1 = new ScaleTransition(Duration.millis(180), icon1);
                TranslateTransition moveCard1 = new TranslateTransition(Duration.millis(180), card1);

                card1.setOnMouseEntered(e -> {

                        cardScale1.setToX(1.05);
                        cardScale1.setToY(1.05);

                        iconScale1.setToX(1.15);
                        iconScale1.setToY(1.15);

                        moveCard1.setToY(-10);

                        card1.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.22)));

                        card1.setStyle(cardStyle +
                                        "-fx-border-color:" + GREEN + ";" +
                                        "-fx-border-width:2;");

                        title1.setStyle(
                                        "-fx-font-size:20px;" +
                                                        "-fx-font-weight:bold;" +
                                                        "-fx-text-fill:" + GREEN + ";");

                        cardScale1.playFromStart();
                        iconScale1.playFromStart();
                        moveCard1.playFromStart();
                });

                card1.setOnMouseExited(e -> {

                        cardScale1.setToX(1);
                        cardScale1.setToY(1);

                        iconScale1.setToX(1);
                        iconScale1.setToY(1);

                        moveCard1.setToY(0);

                        card1.setEffect(cardShadow);
                        card1.setStyle(cardStyle);

                        title1.setStyle(
                                        "-fx-font-size:20px;" +
                                                        "-fx-font-weight:bold;");

                        cardScale1.playFromStart();
                        iconScale1.playFromStart();
                        moveCard1.playFromStart();
                });

                card1.setCursor(javafx.scene.Cursor.HAND);
                card1.setOnMouseClicked(e -> showLearnMoreDialog(
                                stage,
                                "Load Posting & Driver Matching",
                                "EcoLoad helps users turn a delivery requirement into a clear, manageable trip.",
                                new String[][] {
                                                { "Post a load",
                                                                "Users enter shipment, pickup, destination, and timing details from their dashboard." },
                                                { "Match the right driver",
                                                                "Available drivers can review suitable delivery work and accept loads that fit their vehicle and route." },
                                                { "Benefits for everyone",
                                                                "Users get organized shipment management while drivers can discover work opportunities and manage active trips." }
                                }));

                // ====================== CARD 2 HOVER ======================

                ScaleTransition cardScale2 = new ScaleTransition(Duration.millis(180), card2);
                ScaleTransition iconScale2 = new ScaleTransition(Duration.millis(180), trackingIcon);
                TranslateTransition moveCard2 = new TranslateTransition(Duration.millis(180), card2);

                card2.setOnMouseEntered(e -> {

                        cardScale2.setToX(1.05);
                        cardScale2.setToY(1.05);

                        iconScale2.setToX(1.15);
                        iconScale2.setToY(1.15);

                        moveCard2.setToY(-10);

                        card2.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.22)));

                        card2.setStyle(cardStyle +
                                        "-fx-border-color:" + GREEN + ";" +
                                        "-fx-border-width:2;");

                        title2.setStyle(
                                        "-fx-font-size:20px;" +
                                                        "-fx-font-weight:bold;" +
                                                        "-fx-text-fill:" + GREEN + ";");

                        cardScale2.playFromStart();
                        iconScale2.playFromStart();
                        moveCard2.playFromStart();
                });

                card2.setOnMouseExited(e -> {

                        cardScale2.setToX(1);
                        cardScale2.setToY(1);

                        iconScale2.setToX(1);
                        iconScale2.setToY(1);

                        moveCard2.setToY(0);

                        card2.setEffect(cardShadow);
                        card2.setStyle(cardStyle);

                        title2.setStyle(
                                        "-fx-font-size:20px;" +
                                                        "-fx-font-weight:bold;");

                        cardScale2.playFromStart();
                        iconScale2.playFromStart();
                        moveCard2.playFromStart();
                });

                card2.setCursor(javafx.scene.Cursor.HAND);
                card2.setOnMouseClicked(e -> showLearnMoreDialog(
                                stage,
                                "Trip Tracking & Status Updates",
                                "Stay informed from pickup through delivery with a shared view of each shipment's progress.",
                                new String[][] {
                                                { "Live trip visibility",
                                                                "Drivers manage their active trip while users can follow delivery progress from the EcoLoad dashboard." },
                                                { "Clear status updates",
                                                                "Trip statuses make it easier to understand whether a shipment is assigned, in transit, or completed." },
                                                { "Helpful support",
                                                                "Support features give users and drivers a clear place to raise questions or report delivery issues." }
                                }));

                // ====================== CARD 3 HOVER ======================

                ScaleTransition cardScale3 = new ScaleTransition(Duration.millis(180), card3);
                ScaleTransition iconScale3 = new ScaleTransition(Duration.millis(180), securityIcon);
                TranslateTransition moveCard3 = new TranslateTransition(Duration.millis(180), card3);

                card3.setOnMouseEntered(e -> {

                        cardScale3.setToX(1.05);
                        cardScale3.setToY(1.05);

                        iconScale3.setToX(1.15);
                        iconScale3.setToY(1.15);

                        moveCard3.setToY(-10);

                        card3.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.22)));

                        card3.setStyle(cardStyle +
                                        "-fx-border-color:" + GREEN + ";" +
                                        "-fx-border-width:2;");

                        title3.setStyle(
                                        "-fx-font-size:20px;" +
                                                        "-fx-font-weight:bold;" +
                                                        "-fx-text-fill:" + GREEN + ";");

                        cardScale3.playFromStart();
                        iconScale3.playFromStart();
                        moveCard3.playFromStart();
                });

                card3.setOnMouseExited(e -> {

                        cardScale3.setToX(1);
                        cardScale3.setToY(1);

                        iconScale3.setToX(1);
                        iconScale3.setToY(1);

                        moveCard3.setToY(0);

                        card3.setEffect(cardShadow);
                        card3.setStyle(cardStyle);

                        title3.setStyle(
                                        "-fx-font-size:20px;" +
                                                        "-fx-font-weight:bold;");

                        cardScale3.playFromStart();
                        iconScale3.playFromStart();
                        moveCard3.playFromStart();
                });

                card3.setCursor(javafx.scene.Cursor.HAND);
                card3.setOnMouseClicked(e -> showLearnMoreDialog(
                                stage,
                                "Safe, Reliable & Sustainable Delivery",
                                "EcoLoad is designed to support dependable logistics decisions for users, drivers, and the wider community.",
                                new String[][] {
                                                { "Safety and reliability",
                                                                "Verified driver workflows, clear trip information, and delivery status updates help keep operations accountable." },
                                                { "Payments and assistance",
                                                                "The platform supports transparent trip management alongside payment-related workflows and accessible support." },
                                                { "A greener logistics future",
                                                                "Smarter load coordination and efficient journeys help reduce avoidable travel and support more sustainable transportation." }
                                }));

                // ====================== CARD 4 HOVER ======================

                ScaleTransition cardScale4 = new ScaleTransition(Duration.millis(180), card4);
                ScaleTransition iconScale4 = new ScaleTransition(Duration.millis(180), ecoIcon);
                TranslateTransition moveCard4 = new TranslateTransition(Duration.millis(180), card4);

                card4.setOnMouseEntered(e -> {

                        cardScale4.setToX(1.05);
                        cardScale4.setToY(1.05);

                        iconScale4.setToX(1.15);
                        iconScale4.setToY(1.15);

                        moveCard4.setToY(-10);

                        card4.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.22)));

                        card4.setStyle(cardStyle +
                                        "-fx-border-color:" + GREEN + ";" +
                                        "-fx-border-width:2;");

                        title4.setStyle(
                                        "-fx-font-size:20px;" +
                                                        "-fx-font-weight:bold;" +
                                                        "-fx-text-fill:" + GREEN + ";");

                        cardScale4.playFromStart();
                        iconScale4.playFromStart();
                        moveCard4.playFromStart();
                });

                card4.setOnMouseExited(e -> {

                        cardScale4.setToX(1);
                        cardScale4.setToY(1);

                        iconScale4.setToX(1);
                        iconScale4.setToY(1);

                        moveCard4.setToY(0);

                        card4.setEffect(cardShadow);
                        card4.setStyle(cardStyle);

                        title4.setStyle(
                                        "-fx-font-size:20px;" +
                                                        "-fx-font-weight:bold;");

                        cardScale4.playFromStart();
                        iconScale4.playFromStart();
                        moveCard4.playFromStart();
                });

                // ======================================================REVIEWS================================

                VBox testimonialSection = new VBox(50);
                testimonialSection.setAlignment(Pos.CENTER);
                testimonialSection.setPadding(new Insets(80, 80, 80, 80));
                testimonialSection.setStyle("-fx-background-color:" + BG + ";");

                Label testimonialTitle = new Label("WHAT OUR CLIENTS SAY");

                testimonialTitle.setStyle(
                                "-fx-font-size:18px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:" + GREEN + ";");
                Label testimonialHeading = new Label("Trusted by Businesses");

                testimonialHeading.setStyle(
                                "-fx-font-size:38px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:" + TEXT + ";");

                HBox reviewRow = new HBox(30);
                reviewRow.setAlignment(Pos.CENTER);

                String reviewStyle = "-fx-background-color:white;" +
                                "-fx-background-radius:18;" +
                                "-fx-border-radius:18;" +
                                "-fx-border-color:#E5E5E5;" +
                                "-fx-border-width:1;" +
                                "-fx-padding:25;";

                // ====================== REVIEW 1 ======================

                Rectangle reviewBack1 = new Rectangle(280, 190);
                reviewBack1.setArcWidth(18);
                reviewBack1.setArcHeight(18);
                reviewBack1.setFill(Color.web("#0B7A33"));
                reviewBack1.setTranslateX(12);
                reviewBack1.setTranslateY(12);
                reviewBack1.setVisible(false);

                VBox review1 = new VBox(15);
                review1.setPrefWidth(280);
                review1.setStyle(reviewStyle);

                Label stars1 = new Label("★★★★★");
                stars1.setStyle("-fx-font-size:20px;-fx-text-fill:#F4B400;");

                Label text11 = new Label(
                                "EcoLoad made managing our fleet much easier and improved our delivery efficiency.");
                text11.setWrapText(true);

                Label name1 = new Label("- Rahul Sharma");
                name1.setStyle("-fx-font-weight:bold;");

                review1.getChildren().addAll(stars1, text11, name1);

                DropShadow reviewShadow = new DropShadow();
                reviewShadow.setRadius(10);
                reviewShadow.setOffsetY(4);
                reviewShadow.setColor(Color.rgb(0, 0, 0, 0.12));

                review1.setEffect(reviewShadow);

                StackPane reviewCard1 = new StackPane(reviewBack1, review1);
                reviewBack1.toBack();

                TranslateTransition lift1 = new TranslateTransition(Duration.millis(220), review1);

                ScaleTransition scale1 = new ScaleTransition(Duration.millis(220), review1);

                reviewCard1.setOnMouseEntered(e -> {
                        reviewBack1.setVisible(true);

                        lift1.setToY(-12);

                        scale1.setToX(1.03);
                        scale1.setToY(1.03);

                        lift1.playFromStart();
                        scale1.playFromStart();
                });

                reviewCard1.setOnMouseExited(e -> {
                        reviewBack1.setVisible(false);

                        lift1.setToY(0);

                        scale1.setToX(1);
                        scale1.setToY(1);

                        lift1.playFromStart();
                        scale1.playFromStart();
                });

                // ====================== REVIEW 2 ======================

                Rectangle reviewBack2 = new Rectangle(280, 190);
                reviewBack2.setArcWidth(18);
                reviewBack2.setArcHeight(18);
                reviewBack2.setFill(Color.web("#0B7A33"));
                reviewBack2.setTranslateX(12);
                reviewBack2.setTranslateY(12);
                reviewBack2.setVisible(false);

                VBox review2 = new VBox(15);
                review2.setPrefWidth(280);
                review2.setStyle(reviewStyle);

                Label stars2 = new Label("★★★★★");
                stars2.setStyle("-fx-font-size:20px;-fx-text-fill:#F4B400;");

                Label text21 = new Label(
                                "The live tracking feature keeps us updated throughout the delivery process. It's reliable and very easy to use.");
                text21.setWrapText(true);

                Label name2 = new Label("- Priya Patel");
                name2.setStyle("-fx-font-weight:bold;");

                review2.getChildren().addAll(stars2, text21, name2);

                review2.setEffect(reviewShadow);

                StackPane reviewCard2 = new StackPane(reviewBack2, review2);

                reviewBack2.toBack();

                TranslateTransition lift2 = new TranslateTransition(Duration.millis(220), review2);

                ScaleTransition scale2 = new ScaleTransition(Duration.millis(220), review2);

                reviewCard2.setOnMouseEntered(e -> {
                        reviewBack2.setVisible(true);
                        lift2.setToY(-12);

                        scale2.setToX(1.03);
                        scale2.setToY(1.03);

                        lift2.playFromStart();
                        scale2.playFromStart();
                });

                reviewCard2.setOnMouseExited(e -> {
                        reviewBack2.setVisible(false);
                        lift2.setToY(0);

                        scale2.setToX(1);
                        scale2.setToY(1);

                        lift2.playFromStart();
                        scale2.playFromStart();
                });

                // ====================== REVIEW 3 ======================

                Rectangle reviewBack3 = new Rectangle(280, 190);
                reviewBack3.setArcWidth(18);
                reviewBack3.setArcHeight(18);
                reviewBack3.setFill(Color.web("#0B7A33"));
                reviewBack3.setTranslateX(12);
                reviewBack3.setTranslateY(12);
                reviewBack3.setVisible(false);

                VBox review3 = new VBox(15);
                review3.setPrefWidth(280);
                review3.setStyle(reviewStyle);

                Label stars3 = new Label("★★★★★");
                stars3.setStyle(
                                "-fx-font-size:20px;" +
                                                "-fx-text-fill:#F4B400;");

                Label text31 = new Label(
                                "EcoLoad has simplified our logistics operations. Managing vehicles and shipments is now faster and more organized.");
                text31.setWrapText(true);

                Label name3 = new Label("- Amit Singh");
                name3.setStyle("-fx-font-weight:bold;");

                review3.getChildren().addAll(
                                stars3,
                                text31,
                                name3);

                // Shadow
                review3.setEffect(reviewShadow);

                // StackPane
                StackPane reviewCard3 = new StackPane(reviewBack3, review3);

                // Hover Animation
                TranslateTransition lift3 = new TranslateTransition(Duration.millis(220), review3);

                ScaleTransition scale3 = new ScaleTransition(Duration.millis(220), review3);

                reviewCard3.setOnMouseEntered(e -> {
                        reviewBack3.setVisible(true);
                        lift3.setToY(-12);

                        scale3.setToX(1.03);
                        scale3.setToY(1.03);

                        lift3.playFromStart();
                        scale3.playFromStart();
                });

                reviewCard3.setOnMouseExited(e -> {
                        reviewBack3.setVisible(false);
                        lift3.setToY(0);

                        scale3.setToX(1);
                        scale3.setToY(1);

                        lift3.playFromStart();
                        scale3.playFromStart();
                });
                // reviewRow.getChildren().addAll(review1, review2, review3);
                reviewRow.getChildren().addAll(reviewCard1, reviewCard2, reviewCard3);

                testimonialSection.getChildren().addAll(
                                testimonialTitle,
                                testimonialHeading,
                                reviewRow);

                // ======================================ABOUT
                // US========================================
                VBox workSection = new VBox(50);
                workSection.setAlignment(Pos.CENTER);
                workSection.setPadding(new Insets(80, 80, 80, 80));
                workSection.setStyle("-fx-background-color:" + BG + ";");

                Label workTitle = new Label("HOW IT WORKS");

                workTitle.setStyle(
                                "-fx-font-size:18px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:" + GREEN + ";");

                Label workHeading = new Label("Deliver in Four Easy Steps");

                workHeading.setStyle(
                                "-fx-font-size:38px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:" + TEXT + ";");
                HBox stepRow = new HBox(35);
                stepRow.setAlignment(Pos.CENTER);
                String stepStyle = "-fx-background-color:white;" +
                                "-fx-background-radius:18;" +
                                "-fx-border-radius:18;" +
                                "-fx-padding:25;" +
                                "-fx-border-color:#E5E5E5;";

                // step1
                VBox step1 = new VBox(15);
                step1.setAlignment(Pos.TOP_CENTER);
                step1.setPrefWidth(220);
                step1.setStyle(stepStyle);

                Label number1 = new Label("①");
                number1.setStyle(
                                "-fx-font-size:40px;" +
                                                "-fx-text-fill:" + GREEN + ";");

                Label stepTitle1 = new Label("Register");
                stepTitle1.setStyle(
                                "-fx-font-size:20px;" +
                                                "-fx-font-weight:bold;");

                Label stepText1 = new Label(
                                "Create your account\nand set up your\nbusiness profile.");

                stepText1.setWrapText(true);
                stepText1.setAlignment(Pos.CENTER);

                step1.getChildren().addAll(number1, stepTitle1, stepText1);

                // step 2
                VBox step2 = new VBox(15);
                step2.setAlignment(Pos.TOP_CENTER);
                step2.setPrefWidth(220);
                step2.setStyle(stepStyle);

                Label number2 = new Label("②");
                number2.setStyle(
                                "-fx-font-size:40px;" +
                                                "-fx-text-fill:" + GREEN + ";");

                Label stepTitle2 = new Label("Add Vehicles");
                stepTitle2.setStyle(
                                "-fx-font-size:20px;" +
                                                "-fx-font-weight:bold;");

                Label stepText2 = new Label(
                                "Register your trucks\nand drivers into\nthe system.");

                stepText2.setWrapText(true);
                stepText2.setAlignment(Pos.CENTER);

                step2.getChildren().addAll(
                                number2,
                                stepTitle2,
                                stepText2);

                // step3
                VBox step3 = new VBox(15);
                step3.setAlignment(Pos.TOP_CENTER);
                step3.setPrefWidth(220);
                step3.setStyle(stepStyle);

                Label number3 = new Label("③");
                number3.setStyle(
                                "-fx-font-size:40px;" +
                                                "-fx-text-fill:" + GREEN + ";");

                Label stepTitle3 = new Label("Assign Shipment");
                stepTitle3.setStyle(
                                "-fx-font-size:20px;" +
                                                "-fx-font-weight:bold;");

                Label stepText3 = new Label(
                                "Assign shipments\nand routes to\nyour drivers.");

                stepText3.setWrapText(true);
                stepText3.setAlignment(Pos.CENTER);

                step3.getChildren().addAll(
                                number3,
                                stepTitle3,
                                stepText3);

                // step4
                VBox step4 = new VBox(15);
                step4.setAlignment(Pos.TOP_CENTER);
                step4.setPrefWidth(220);
                step4.setStyle(stepStyle);

                Label number4 = new Label("④");
                number4.setStyle(
                                "-fx-font-size:40px;" +
                                                "-fx-text-fill:" + GREEN + ";");

                Label stepTitle4 = new Label("Track Delivery");
                stepTitle4.setStyle(
                                "-fx-font-size:20px;" +
                                                "-fx-font-weight:bold;");

                Label stepText4 = new Label(
                                "Monitor delivery\nstatus in real time\nuntil completion.");

                stepText4.setWrapText(true);
                stepText4.setAlignment(Pos.CENTER);

                step4.getChildren().addAll(
                                number4,
                                stepTitle4,
                                stepText4);

                DropShadow stepShadow = new DropShadow();
                stepShadow.setRadius(10);
                stepShadow.setOffsetY(4);
                stepShadow.setColor(Color.rgb(0, 0, 0, 0.12));

                DropShadow hoverShadow = new DropShadow();
                hoverShadow.setRadius(20);
                hoverShadow.setOffsetY(10);
                hoverShadow.setColor(Color.rgb(0, 0, 0, 0.25));

                step1.setEffect(stepShadow);
                step2.setEffect(stepShadow);
                step3.setEffect(stepShadow);
                step4.setEffect(stepShadow);

                // Hover effect for Step 1
                ScaleTransition scaleStep1 = new ScaleTransition(Duration.millis(180), step1);
                TranslateTransition moveStep1 = new TranslateTransition(Duration.millis(180), step1);

                step1.setOnMouseEntered(e -> {
                        scaleStep1.setToX(1.05);
                        scaleStep1.setToY(1.05);

                        moveStep1.setToY(-10);

                        step1.setEffect(hoverShadow);

                        scaleStep1.playFromStart();
                        moveStep1.playFromStart();
                });

                step1.setOnMouseExited(e -> {
                        scaleStep1.setToX(1);
                        scaleStep1.setToY(1);

                        moveStep1.setToY(0);

                        step1.setEffect(stepShadow);

                        scaleStep1.playFromStart();
                        moveStep1.playFromStart();
                });
                // Hover effect for Step 2
                ScaleTransition scaleStep2 = new ScaleTransition(Duration.millis(180), step2);
                TranslateTransition moveStep2 = new TranslateTransition(Duration.millis(180), step2);

                step2.setOnMouseEntered(e -> {
                        scaleStep2.setToX(1.05);
                        scaleStep2.setToY(1.05);

                        moveStep2.setToY(-10);

                        step2.setEffect(hoverShadow);

                        scaleStep2.playFromStart();
                        moveStep2.playFromStart();
                });

                step2.setOnMouseExited(e -> {
                        scaleStep2.setToX(1);
                        scaleStep2.setToY(1);

                        moveStep2.setToY(0);

                        step2.setEffect(stepShadow);

                        scaleStep2.playFromStart();
                        moveStep2.playFromStart();
                });

                // Hover effect for Step 3
                ScaleTransition scaleStep3 = new ScaleTransition(Duration.millis(180), step3);
                TranslateTransition moveStep3 = new TranslateTransition(Duration.millis(180), step3);

                step3.setOnMouseEntered(e -> {
                        scaleStep3.setToX(1.05);
                        scaleStep3.setToY(1.05);

                        moveStep3.setToY(-10);

                        step3.setEffect(hoverShadow);

                        scaleStep3.playFromStart();
                        moveStep3.playFromStart();
                });

                step3.setOnMouseExited(e -> {
                        scaleStep3.setToX(1);
                        scaleStep3.setToY(1);

                        moveStep3.setToY(0);

                        step3.setEffect(stepShadow);

                        scaleStep3.playFromStart();
                        moveStep3.playFromStart();
                });

                // Hover effect for Step 4
                ScaleTransition scaleStep4 = new ScaleTransition(Duration.millis(180), step4);
                TranslateTransition moveStep4 = new TranslateTransition(Duration.millis(180), step4);

                step4.setOnMouseEntered(e -> {
                        scaleStep4.setToX(1.05);
                        scaleStep4.setToY(1.05);

                        moveStep4.setToY(-10);

                        step4.setEffect(hoverShadow);

                        scaleStep4.playFromStart();
                        moveStep4.playFromStart();
                });

                step4.setOnMouseExited(e -> {
                        scaleStep4.setToX(1);
                        scaleStep4.setToY(1);

                        moveStep4.setToY(0);

                        step4.setEffect(stepShadow);

                        scaleStep4.playFromStart();
                        moveStep4.playFromStart();
                });

                // ================= CONNECTOR 1 =================

                Line line1 = new Line(0, 0, 70, 0);
                line1.setStroke(Color.web(GREEN));
                line1.setStrokeWidth(3);

                Circle dot1 = new Circle(4, Color.web(GREEN));

                StackPane connector1 = new StackPane();
                connector1.getChildren().addAll(line1, dot1);
                StackPane.setAlignment(dot1, Pos.CENTER_RIGHT);

                // ================= CONNECTOR 2 =================

                Line line2 = new Line(0, 0, 70, 0);
                line2.setStroke(Color.web(GREEN));
                line2.setStrokeWidth(3);

                Circle dot2 = new Circle(4, Color.web(GREEN));

                StackPane connector2 = new StackPane();
                connector2.getChildren().addAll(line2, dot2);
                StackPane.setAlignment(dot2, Pos.CENTER_RIGHT);

                // ================= CONNECTOR 3 =================

                Line line3 = new Line(0, 0, 70, 0);
                line3.setStroke(Color.web(GREEN));
                line3.setStrokeWidth(3);

                Circle dot3 = new Circle(4, Color.web(GREEN));

                StackPane connector3 = new StackPane();
                connector3.getChildren().addAll(line3, dot3);
                StackPane.setAlignment(dot3, Pos.CENTER_RIGHT);

                stepRow.getChildren().addAll(
                                step1,
                                connector1,
                                step2,
                                connector2,
                                step3,
                                connector3,
                                step4);
                workSection.getChildren().addAll(
                                workTitle,
                                workHeading,
                                stepRow);

                // =============================================WORKING WITH
                // US=====================================================
                VBox chooseSection = new VBox(40);
                chooseSection.setAlignment(Pos.CENTER);
                chooseSection.setPadding(new Insets(80, 80, 80, 80));
                chooseSection.setStyle("-fx-background-color:white;");

                Label chooseTitle = new Label("WHY CHOOSE ECOLOAD");

                chooseTitle.setStyle(
                                "-fx-font-size:18px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:" + GREEN + ";");
                Label chooseHeading = new Label("Smarter Logistics,\nBetter Results");

                chooseHeading.setStyle(
                                "-fx-font-size:38px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:" + TEXT + ";");

                Label chooseDesc = new Label(
                                "EcoLoad helps businesses streamline logistics\n" +
                                                "through modern technology, secure operations,\n" +
                                                "and efficient delivery management.");

                chooseDesc.setStyle(
                                "-fx-font-size:18px;" +
                                                "-fx-text-fill:#666666;");

                HBox chooseContent = new HBox(70);
                chooseContent.setAlignment(Pos.CENTER);

                Image chooseImage = new Image("assets\\images\\Analytics.png");

                ImageView dashboard = new ImageView(chooseImage);
                dashboard.setFitWidth(520);
                dashboard.setPreserveRatio(true);
                dashboard.setSmooth(true);

                VBox points = new VBox(20);
                points.setAlignment(Pos.CENTER_LEFT);

                Label p1 = new Label("✓  Real-Time Shipment Tracking");
                Label p2 = new Label("✓  Secure Driver Management");
                Label p3 = new Label("✓  Easy Route Planning");
                Label p4 = new Label("✓  Eco-Friendly Transportation");

                String pointStyle = "-fx-font-size:18px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-text-fill:" + TEXT + ";";

                p1.setStyle(pointStyle);
                p2.setStyle(pointStyle);
                p3.setStyle(pointStyle);
                p4.setStyle(pointStyle);

                points.getChildren().addAll(
                                chooseTitle,
                                chooseHeading,
                                chooseDesc,
                                p1,
                                p2,
                                p3,
                                p4);

                chooseContent.getChildren().addAll(
                                dashboard,
                                points);

                chooseSection.getChildren().add(chooseContent);

                // ====================================================ABOUT
                // US======================================
                HBox aboutSection = new HBox(70);
                aboutSection.setAlignment(Pos.CENTER);
                aboutSection.setPadding(new Insets(80, 80, 80, 80));
                aboutSection.setStyle("-fx-background-color:white;");

                Image aboutImage = new Image("assets\\images\\aboutus.png");

                ImageView about1 = new ImageView(aboutImage);
                about1.setFitWidth(500);
                about1.setPreserveRatio(true);
                about1.setSmooth(true);

                VBox aboutText = new VBox(20);
                aboutText.setAlignment(Pos.CENTER_LEFT);
                aboutText.setMaxWidth(520);

                Label aboutTitle = new Label("ABOUT US");

                aboutTitle.setStyle(
                                "-fx-font-size:18px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:" + GREEN + ";");

                Label aboutHeading = new Label(
                                "Building Smarter Logistics\nfor a Sustainable Future");

                aboutHeading.setStyle(
                                "-fx-font-size:38px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:" + TEXT + ";");

                Label aboutDesc = new Label(
                                "EcoLoad is a smart logistics platform that helps\n" +
                                                "businesses manage transport, drivers, and shipments\n" +
                                                "efficiently through one easy-to-use dashboard.");

                aboutDesc.setStyle(
                                "-fx-font-size:18px;" +
                                                "-fx-text-fill:#666666;");

                Label a1 = new Label("✓  Easy to Use");
                Label a2 = new Label("✓  Secure Platform");
                Label a3 = new Label("✓  Fast Management");
                Label a4 = new Label("✓  Eco-Friendly");

                String aboutStyle = "-fx-font-size:18px;" +
                                "-fx-text-fill:" + TEXT + ";" +
                                "-fx-font-weight:bold;";

                a1.setStyle(aboutStyle);
                a2.setStyle(aboutStyle);
                a3.setStyle(aboutStyle);
                a4.setStyle(aboutStyle);

                aboutText.getChildren().addAll(
                                aboutTitle,
                                aboutHeading,
                                aboutDesc,
                                a1,
                                a2,
                                a3,
                                a4);

                aboutSection.getChildren().addAll(
                                about1,
                                aboutText);

                // ========================================================CONTACT
                // US=====================================
                HBox footer = new HBox(200);

                footer.setPadding(new Insets(30, 80, 20, 80));
                footer.setAlignment(Pos.TOP_CENTER);
                footer.setStyle("-fx-background-color:" + DARK_GREEN + ";");

                VBox company = new VBox(10);

                Label companyName = new Label("EcoLoad");
                companyName.setStyle(
                                "-fx-font-size:30px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:white;");

                Label companyDesc = new Label(
                                "Smart logistics platform for efficient\n" +
                                                "transportation management and\n" +
                                                "eco-friendly deliveries.");

                companyDesc.setStyle(
                                "-fx-font-size:16px;" +
                                                "-fx-text-fill:white;");

                company.getChildren().addAll(companyName, companyDesc);

                VBox links = new VBox(2);

                Label quick = new Label("Quick Links");
                quick.setStyle(
                                "-fx-font-size:22px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:white;");

                Label l1 = new Label("Overview");
                Label l2 = new Label("Features");
                Label l3 = new Label("About Us");

                String footerStyle = "-fx-font-size:16px;" +
                                "-fx-text-fill:white;";

                l1.setStyle(footerStyle);
                l2.setStyle(footerStyle);
                l3.setStyle(footerStyle);

                links.getChildren().addAll(
                                quick,
                                l1,
                                l2,
                                l3);

                VBox contactBox = new VBox(10);

                Label contactTitle = new Label("Contact");
                contactTitle.setStyle(
                                "-fx-font-size:22px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-text-fill:white;");

                Label c1 = new Label("📍 Pune, Maharashtra");
                Label c2 = new Label("📞 +91 9876543210");
                Label c3 = new Label("✉ ecoload@gmail.com");

                c1.setStyle(footerStyle);
                c2.setStyle(footerStyle);
                c3.setStyle(footerStyle);

                contactBox.getChildren().addAll(
                                contactTitle,
                                c1,
                                c2,
                                c3);
                footer.getChildren().addAll(
                                company,
                                links,
                                contactBox);
                Label copyright = new Label(
                                "© 2026 EcoLoad. All Rights Reserved.");

                copyright.setStyle(
                                "-fx-text-fill:white;" +
                                                "-fx-font-size:15px;");

                HBox copyrightBar = new HBox(copyright);
                copyrightBar.setAlignment(Pos.CENTER);
                copyrightBar.setPadding(new Insets(10));
                copyrightBar.setStyle("-fx-background-color:#06471D;");

                // ========================================Main
                // page========================================
                VBox mainContent = new VBox();
                mainContent.getChildren().addAll(hero, overviewSection, featureSection, workSection, chooseSection,
                                testimonialSection, aboutSection, footer, copyrightBar);

                // ScrollPane
                ScrollPane scrollPane = new ScrollPane(mainContent);
                scrollPane.setFitToWidth(true);
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

                // Create ScrollPane
                // ScrollPane scrollPane = new ScrollPane(mainContent);

                // Add listener here
                String navStyle1 = "-fx-font-size:20px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-text-fill:" + TEXT + ";" +
                                "-fx-cursor:hand;";

                String activeNavStyle = "-fx-font-size:20px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-text-fill:" + GREEN + ";" +
                                "-fx-border-color:" + GREEN + ";" +
                                "-fx-border-width:0 0 3 0;" +
                                "-fx-padding:0 0 8 0;" +
                                "-fx-cursor:hand;";

                // Active navbar code
                scrollPane.vvalueProperty().addListener((obs, oldValue, newValue) -> {

                        double scroll = newValue.doubleValue();

                        if (scroll < 0.15) {

                                setActiveNav(
                                                overview,
                                                features,
                                                reviews,
                                                about,
                                                contact,
                                                overview,
                                                navStyle1,
                                                activeNavStyle);

                        } else if (scroll < 0.45) {

                                setActiveNav(
                                                overview,
                                                features,
                                                reviews,
                                                about,
                                                contact,
                                                features,
                                                navStyle1,
                                                activeNavStyle);

                        } else if (scroll < 0.70) {

                                setActiveNav(
                                                overview,
                                                features,
                                                reviews,
                                                about,
                                                contact,
                                                reviews,
                                                navStyle1,
                                                activeNavStyle);

                        } else if (scroll < 0.90) {

                                setActiveNav(
                                                overview,
                                                features,
                                                reviews,
                                                about,
                                                contact,
                                                about,
                                                navStyle1,
                                                activeNavStyle);

                        } else {

                                setActiveNav(
                                                overview,
                                                features,
                                                reviews,
                                                about,
                                                contact,
                                                contact,
                                                navStyle1,
                                                activeNavStyle);
                        }
                });
                // Navbar click events
                overview.setOnMouseClicked(e -> scrollPane.setVvalue(0.0));
                features.setOnMouseClicked(e -> scrollPane.setVvalue(0.28));
                reviews.setOnMouseClicked(e -> scrollPane.setVvalue(0.65));
                about.setOnMouseClicked(e -> scrollPane.setVvalue(0.82));
                contact.setOnMouseClicked(e -> scrollPane.setVvalue(1.0));

                // BorderPane
                BorderPane rootBorderPane = new BorderPane();
                rootBorderPane.setTop(navbar);
                rootBorderPane.setCenter(scrollPane);

                // Scene
                Scene scene = new Scene(rootBorderPane, 1536, 750);

                Image appIcon = new Image(getClass().getResource("/assets/icons/EcoloadLogo.png").toExternalForm());

                Rectangle2D screen = Screen.getPrimary().getVisualBounds();
                stage.getIcons().add(appIcon);
                stage.setX(screen.getMinX());
                stage.setY(screen.getMinY());
                stage.setWidth(screen.getWidth());
                stage.setHeight(screen.getHeight());
                stage.setMinWidth(1536);
                stage.setMinHeight(750);
                stage.setResizable(true);

                stage.setScene(scene);
                stage.setTitle("EcoLoad");
                stage.show();

        }

        private void showLearnMoreDialog(
                        Stage owner,
                        String titleText,
                        String introduction,
                        String[][] sections) {

                Stage popup = new Stage();
                popup.initOwner(owner);
                popup.initModality(Modality.WINDOW_MODAL);
                popup.setTitle("EcoLoad - " + titleText);

                Label eyebrow = new Label("ECOLOAD INSIGHTS");
                eyebrow.setStyle("-fx-text-fill:#0B6B2A; -fx-font-size:12px; -fx-font-weight:bold;");

                Label title = new Label(titleText);
                title.setWrapText(true);
                title.setStyle("-fx-text-fill:#202820; -fx-font-size:28px; -fx-font-weight:bold;");

                Label intro = new Label(introduction);
                intro.setWrapText(true);
                intro.setMaxWidth(580);
                intro.setStyle("-fx-text-fill:#5F5F5F; -fx-font-size:15px; -fx-line-spacing:3px;");

                VBox sectionBox = new VBox(14);
                for (String[] section : sections) {
                        Label sectionTitle = new Label(section[0]);
                        sectionTitle.setStyle("-fx-text-fill:#075A24; -fx-font-size:16px; -fx-font-weight:bold;");

                        Label sectionText = new Label(section[1]);
                        sectionText.setWrapText(true);
                        sectionText.setMaxWidth(540);
                        sectionText.setStyle("-fx-text-fill:#4B5563; -fx-font-size:14px; -fx-line-spacing:2px;");

                        VBox sectionCard = new VBox(6, sectionTitle, sectionText);
                        sectionCard.setPadding(new Insets(14, 16, 14, 16));
                        sectionCard.setStyle(
                                        "-fx-background-color:#F4FAF5; -fx-background-radius:10;"
                                                        + "-fx-border-color:#D8ECDC; -fx-border-radius:10;");
                        sectionBox.getChildren().add(sectionCard);
                }

                ScrollPane details = new ScrollPane(sectionBox);
                details.setFitToWidth(true);
                details.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                details.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                details.setStyle("-fx-background-color:transparent; -fx-background:transparent; -fx-border-color:transparent;");

                Button closeButton = new Button("Close");
                closeButton.setPrefWidth(120);
                closeButton.setStyle(
                                "-fx-background-color:#0B6B2A; -fx-text-fill:white; -fx-font-size:14px;"
                                                + "-fx-font-weight:bold; -fx-background-radius:20; -fx-cursor:hand;");
                closeButton.setOnAction(e -> popup.close());

                HBox actions = new HBox(closeButton);
                actions.setAlignment(Pos.CENTER_RIGHT);

                VBox content = new VBox(16, eyebrow, title, intro, details, actions);
                content.setPadding(new Insets(28));
                content.setPrefSize(650, 520);
                VBox.setVgrow(details, Priority.ALWAYS);
                content.setStyle("-fx-background-color:white;");

                Scene scene = new Scene(content, 650, 520);
                popup.setScene(scene);
                popup.setResizable(false);
                popup.showAndWait();
        }

        private void setActiveNav(
                        Label overview,
                        Label features,
                        Label reviews,
                        Label about,
                        Label contact,
                        Label active,
                        String navStyle,
                        String activeNavStyle) {

                overview.setStyle(navStyle);
                features.setStyle(navStyle);
                reviews.setStyle(navStyle);
                about.setStyle(navStyle);
                contact.setStyle(navStyle);

                active.setStyle(activeNavStyle);
        }
}

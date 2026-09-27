package com.super_x.view;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AboutCore2web {

    private final Stage stage;

    // Same colour theme as your EcoLoad homepage
    private final String GREEN = "#0B6B2A";
    private final String DARK_GREEN = "#075A24";
    private final String BG = "#E6F1E8";
    private final String TEXT = "#202820";
    private final String SECONDARY_TEXT = "#5F5F5F";
    private final String WHITE = "#FFFFFF";

    public AboutCore2web(Stage stage) {
        this.stage = stage;
    }

    public void showAboutPage() {

        // ==========================================
        // MAIN CONTENT
        // ==========================================

        VBox content = new VBox(35);
        content.setPadding(new Insets(35, 80, 50, 80));
        content.setStyle(
                "-fx-background-color: " + BG + ";");

        // ==========================================
        // BACK BUTTON
        // ==========================================

        Button backButton = new Button("←  Back to Home");

        backButton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + GREEN + ";" +
                        "-fx-border-width: 2;" +
                        "-fx-text-fill: " + GREEN + ";" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 22;" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-cursor: hand;");

        backButton.setOnAction(e -> {

            HomePage homePage = new HomePage();

            homePage.showHomePage(stage);
        });

        // ==========================================
        // HERO SECTION - LEFT
        // ==========================================

        VBox heroLeft = new VBox(16);
        heroLeft.setAlignment(Pos.CENTER_LEFT);
        heroLeft.setMaxWidth(600);

        Label poweredBy = new Label("POWERED BY CORE2WEB");

        poweredBy.setStyle(
                "-fx-text-fill: " + GREEN + ";" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        Label heroTitle = new Label(
                "From Code to\nReal World Impact");

        heroTitle.setStyle(
                "-fx-font-size: 42px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT + ";");

        Label heroDescription = new Label(
                "EcoLoad is proudly built with the inspiration of Core2web, "
                        + "a coding academy and startup ecosystem focused on turning "
                        + "students into skilled professionals and entrepreneurs.");

        heroDescription.setWrapText(true);
        heroDescription.setMaxWidth(560);

        heroDescription.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: " + SECONDARY_TEXT + ";" +
                        "-fx-line-spacing: 5px;");

        Button exploreButton = new Button("Explore Core2web  →");

        exploreButton.setStyle(
                "-fx-background-color: " + GREEN + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;");

        heroLeft.getChildren().addAll(
                poweredBy,
                heroTitle,
                heroDescription,
                exploreButton);

        // ==========================================
        // HERO SECTION - RIGHT
        // ==========================================

        StackPane heroVisual = new StackPane();

        Circle outerCircle = new Circle(150);
        outerCircle.setFill(
                Color.web("#CBEAD4"));

        Circle innerCircle = new Circle(110);
        innerCircle.setFill(
                Color.web("#FFFFFF"));

        ImageView logoView = null;

        try {

            Image logoImage = new Image(
                    getClass()
                            .getResource(
                                    "/assets/images/Core2Web.png")
                            .toExternalForm());

            logoView = new ImageView(logoImage);

            logoView.setFitWidth(180);
            logoView.setPreserveRatio(true);

        } catch (Exception ignored) {
        }

        Label coreText = new Label("Core2web");

        coreText.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT + ";");

        Label buildText = new Label(
                "Learn • Build • Grow");

        buildText.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: " + SECONDARY_TEXT + ";");

        VBox visualText = new VBox(8);

        visualText.setAlignment(Pos.CENTER);

        visualText.getChildren().addAll(
                coreText,
                buildText);

        if (logoView != null) {

            heroVisual.getChildren().addAll(
                    outerCircle,
                    innerCircle,
                    logoView);

        } else {

            heroVisual.getChildren().addAll(
                    outerCircle,
                    innerCircle,
                    visualText);
        }

        // ==========================================
        // HERO LAYOUT
        // ==========================================

        HBox heroSection = new HBox(100);

        heroSection.setAlignment(Pos.CENTER);
        heroSection.setPadding(
                new Insets(25, 40, 25, 40));

        heroSection.getChildren().addAll(
                heroLeft,
                heroVisual);

        // ==========================================
        // FOUNDER SECTION
        // ==========================================

        HBox founderSection = new HBox(30);

        founderSection.setAlignment(
                Pos.CENTER_LEFT);

        founderSection.setPadding(
                new Insets(30));

        founderSection.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;");

        DropShadow founderShadow = new DropShadow();

        founderShadow.setRadius(15);
        founderShadow.setOffsetY(5);
        founderShadow.setColor(
                Color.rgb(0, 0, 0, 0.10));

        founderSection.setEffect(
                founderShadow);

        // Founder placeholder

        Image founderPhoto = new Image(
                getClass()
                        .getResource(
                                "/assets/images/Shashi_Bagal.png")
                        .toExternalForm());

        ImageView founderImageView = new ImageView(founderPhoto);

        founderImageView.setFitWidth(170);
        founderImageView.setFitHeight(170);
        founderImageView.setPreserveRatio(true);

        // Founder details

        VBox founderInfo = new VBox(8);

        founderInfo.setMaxWidth(600);

        Label founderName = new Label(
                "Shashikant Bagal");

        founderName.setStyle(
                "-fx-font-size: 25px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT + ";");

        Label founderRole = new Label(
                "Founder");

        founderRole.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + GREEN + ";");

        Label founderDescription = new Label(
                "Shashikant Bagal, popularly known as Shashi Sir, "
                        + "is a technologist, educator and entrepreneur. "
                        + "Through Core2web, he has helped students develop "
                        + "strong programming fundamentals and industry-ready "
                        + "technical skills.");

        founderDescription.setWrapText(true);

        founderDescription.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: " + SECONDARY_TEXT + ";" +
                        "-fx-line-spacing: 4px;");

        founderInfo.getChildren().addAll(
                founderName,
                founderRole,
                founderDescription);

        // Quote section

        VBox quoteBox = new VBox(10);

        quoteBox.setAlignment(Pos.CENTER);
        quoteBox.setPrefWidth(300);

        Label quote = new Label(
                "\"Bridging the gap between education and industry, "
                        + "empowering the next generation.\"");

        quote.setWrapText(true);

        quote.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + GREEN + ";");

        Label quoteAuthor = new Label(
                "— Shashikant Bagal");

        quoteAuthor.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: " + SECONDARY_TEXT + ";");

        quoteBox.getChildren().addAll(
                quote,
                quoteAuthor);

        founderSection.getChildren().addAll(
                founderImageView,
                founderInfo,
                quoteBox);

        // ==========================================
        // FEATURES HEADING
        // ==========================================

        Label featureTitle = new Label(
                "Explore Core2web's Key Features");

        featureTitle.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT + ";");

        Label featureSubtitle = new Label(
                "Learning, career growth and entrepreneurship in one ecosystem.");

        featureSubtitle.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: " + SECONDARY_TEXT + ";");

        VBox featureHeading = new VBox(7);

        featureHeading.setAlignment(Pos.CENTER);

        featureHeading.getChildren().addAll(
                featureTitle,
                featureSubtitle);

        // ==========================================
        // FEATURE CARDS
        // ==========================================

        GridPane featureGrid = new GridPane();

        featureGrid.setHgap(20);
        featureGrid.setVgap(20);

        featureGrid.setAlignment(Pos.CENTER);

        featureGrid.add(
                createFeatureCard(
                        "🎓",
                        "High-Quality Courses",
                        "Industry-focused learning taught by experienced professionals."),
                0, 0);

        featureGrid.add(
                createFeatureCard(
                        "💼",
                        "Placement Assistance",
                        "Career support and opportunities for students."),
                1, 0);

        featureGrid.add(
                createFeatureCard(
                        "💰",
                        "Value For Money",
                        "High-quality IT education at an affordable cost."),
                2, 0);

        featureGrid.add(
                createFeatureCard(
                        "🚀",
                        "Startup Support",
                        "Guidance and mentorship for future entrepreneurs."),
                0, 1);

        featureGrid.add(
                createFeatureCard(
                        "🌐",
                        "Community Support",
                        "A strong network of learners and professionals."),
                1, 1);

        featureGrid.add(
                createFeatureCard(
                        "📈",
                        "Real-World Exposure",
                        "Practical skills and hands-on project experience."),
                2, 1);



        // ==========================================
// TEAM INFORMATION SECTION
// ==========================================

HBox teamSection = new HBox(30);

teamSection.setAlignment(Pos.CENTER_LEFT);
teamSection.setPadding(new Insets(30));

teamSection.setStyle(
        "-fx-background-color: white;" +
        "-fx-background-radius: 20;");

// Shadow effect
DropShadow teamShadow = new DropShadow();

teamShadow.setRadius(15);
teamShadow.setOffsetY(5);
teamShadow.setColor(
        Color.rgb(0, 0, 0, 0.10));

teamSection.setEffect(teamShadow);


// ==========================================
// LEFT SIDE - TEAM INFORMATION
// ==========================================

VBox teamInfo = new VBox(12);

teamInfo.setPrefWidth(500);

Label teamTitle = new Label(
        "👥  TEAM INFORMATION");

teamTitle.setStyle(
        "-fx-font-size: 24px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: " + TEXT + ";");


Label teamDescription = new Label(
        "Our team worked together to design and develop EcoTransit, " +
        "combining innovation, technology and practical solutions to " +
        "create a smarter and more sustainable transportation system.");

teamDescription.setWrapText(true);

teamDescription.setStyle(
        "-fx-font-size: 14px;" +
        "-fx-text-fill: " + SECONDARY_TEXT + ";" +
        "-fx-line-spacing: 4px;");


Label teamName = new Label(
        "Team Name: EcoTransit");

teamName.setStyle(
        "-fx-font-size: 18px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: " + GREEN + ";");


teamInfo.getChildren().addAll(
        teamTitle,
        teamDescription,
        teamName);


// ==========================================
// RIGHT SIDE - TEAM MEMBERS
// ==========================================

VBox membersBox = new VBox(12);

membersBox.setPadding(new Insets(20));

membersBox.setPrefWidth(350);

membersBox.setStyle(
        "-fx-background-color: #F5FAF6;" +
        "-fx-background-radius: 15;" +
        "-fx-border-color: #D5E8D9;" +
        "-fx-border-radius: 15;");


Label membersTitle = new Label(
        "👤  Team Members");

membersTitle.setStyle(
        "-fx-font-size: 19px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: " + TEXT + ";");


String[] teamMembers = {
        "•  Pavan Taware",
        "•  Kishor Kalane",
        "•  Omkar Kokate",
        "•  Tushar Banker",
        "•  Prathmesh Gaikwad"
};


membersBox.getChildren().add(
        membersTitle);


for (String member : teamMembers) {

    Label memberLabel = new Label(member);

    memberLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + SECONDARY_TEXT + ";");

    membersBox.getChildren().add(memberLabel);
}

// ==========================================
// RIGHT SIDE - TEAM VISION
// ==========================================

VBox visionBox = new VBox(15);

visionBox.setAlignment(Pos.CENTER);
visionBox.setPadding(new Insets(25));
visionBox.setPrefWidth(300);

visionBox.setStyle(
        "-fx-background-color: #EAF6EC;" +
        "-fx-background-radius: 15;" +
        "-fx-border-color: #C8E3CE;" +
        "-fx-border-radius: 15;"
);

Label visionIcon = new Label("🌱");

visionIcon.setStyle(
        "-fx-font-size: 45px;"
);

Label visionTitle = new Label("Our Vision");

visionTitle.setStyle(
        "-fx-font-size: 20px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: " + GREEN + ";"
);

Label visionDescription = new Label(
        "Building a smarter, cleaner and more sustainable future for transportation."
);

visionDescription.setWrapText(true);
visionDescription.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

visionDescription.setStyle(
        "-fx-font-size: 14px;" +
        "-fx-text-fill: " + SECONDARY_TEXT + ";" +
        "-fx-line-spacing: 4px;"
);

visionBox.getChildren().addAll(
        visionIcon,
        visionTitle,
        visionDescription
);

// Add both sections
teamSection.getChildren().addAll(
        teamInfo,
        membersBox,
        visionBox);



// ==========================================
// THANK YOU SECTION
// ==========================================

// Main container
VBox thankYouSection = new VBox(20);


// ==========================================
// TOP ROW
// ==========================================

HBox thanksTopRow = new HBox(20);

thanksTopRow.setAlignment(Pos.CENTER);


// ==========================================
// THANKS TO INSTRUCTORS
// ==========================================

VBox instructorsBox = new VBox(15);

instructorsBox.setAlignment(Pos.CENTER);
instructorsBox.setPadding(new Insets(25));

instructorsBox.setPrefWidth(600);
instructorsBox.setPrefHeight(160);

instructorsBox.setStyle(
        "-fx-background-color: white;" +
        "-fx-background-radius: 20;"
);


DropShadow instructorShadow = new DropShadow();

instructorShadow.setRadius(12);
instructorShadow.setOffsetY(4);
instructorShadow.setColor(
        Color.rgb(0, 0, 0, 0.08)
);

instructorsBox.setEffect(instructorShadow);


Label instructorsTitle = new Label(
        "🎓  THANKS TO INSTRUCTORS"
);

instructorsTitle.setStyle(
        "-fx-font-size: 20px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: " + TEXT + ";"
);


// Instructor names
HBox instructorNames = new HBox(40);

instructorNames.setAlignment(Pos.CENTER);

String[] instructors = {
        "Sachin Sir",
        "Pramod Sir",
        "Akshay Sir"
};


for (String instructor : instructors) {

    Label instructorLabel = new Label(instructor);

    instructorLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + SECONDARY_TEXT + ";"
    );

    instructorNames.getChildren().add(instructorLabel);
}


instructorsBox.getChildren().addAll(
        instructorsTitle,
        instructorNames
);


// ==========================================
// THANKS TO SUPER MENTORS
// ==========================================

VBox mentorsBox = new VBox(15);

mentorsBox.setAlignment(Pos.CENTER);
mentorsBox.setPadding(new Insets(25));

mentorsBox.setPrefWidth(600);
mentorsBox.setPrefHeight(160);

mentorsBox.setStyle(
        "-fx-background-color: white;" +
        "-fx-background-radius: 20;"
);


DropShadow mentorShadow = new DropShadow();

mentorShadow.setRadius(12);
mentorShadow.setOffsetY(4);
mentorShadow.setColor(
        Color.rgb(0, 0, 0, 0.08)
);

mentorsBox.setEffect(mentorShadow);


Label superMentorsTitle = new Label(
        "⭐  THANKS TO SUPER MENTORS"
);

superMentorsTitle.setStyle(
        "-fx-font-size: 20px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: " + TEXT + ";"
);


// Super Mentor names
HBox mentorNames = new HBox(40);

mentorNames.setAlignment(Pos.CENTER);

String[] superMentors = {
        "Shiv Sir",
        "Subodh Sir"
};


for (String mentor : superMentors) {

    Label mentorLabel = new Label(mentor);

    mentorLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + SECONDARY_TEXT + ";"
    );

    mentorNames.getChildren().add(mentorLabel);
}


mentorsBox.getChildren().addAll(
        superMentorsTitle,
        mentorNames
);


// Add both top cards
thanksTopRow.getChildren().addAll(
        instructorsBox,
        mentorsBox
);


// ==========================================
// BOTTOM THANK YOU CARD
// ==========================================

HBox finalThanksBox = new HBox();

finalThanksBox.setAlignment(Pos.CENTER_LEFT);
finalThanksBox.setPadding(new Insets(30));

finalThanksBox.setPrefHeight(180);

finalThanksBox.setStyle(
        "-fx-background-color: white;" +
        "-fx-background-radius: 20;"
);


DropShadow finalShadow = new DropShadow();

finalShadow.setRadius(12);
finalShadow.setOffsetY(4);
finalShadow.setColor(
        Color.rgb(0, 0, 0, 0.08)
);

finalThanksBox.setEffect(finalShadow);


// Left content
VBox finalThanksText = new VBox(12);

finalThanksText.setMaxWidth(800);


Label finalTitle = new Label(
        "💚  THANKS TO MENTORS & TEAM LEADS"
);

finalTitle.setStyle(
        "-fx-font-size: 20px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: " + TEXT + ";"
);


Label finalDescription = new Label(
        "We sincerely thank our mentors and team leads for their continuous " +
        "guidance, support, motivation and valuable feedback throughout our " +
        "project journey."
);

finalDescription.setWrapText(true);

finalDescription.setStyle(
        "-fx-font-size: 14px;" +
        "-fx-text-fill: " + SECONDARY_TEXT + ";" +
        "-fx-line-spacing: 4px;"
);


finalThanksText.getChildren().addAll(
        finalTitle,
        finalDescription
);


// Spacer
Region thankSpacer = new Region();

HBox.setHgrow(
        thankSpacer,
        Priority.ALWAYS
);


// Right Thank You text
Label thankYouText = new Label(
        "Thank You!  💚"
);

thankYouText.setStyle(
        "-fx-font-size: 28px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: " + GREEN + ";"
);


finalThanksBox.getChildren().addAll(
        finalThanksText,
        thankSpacer,
        thankYouText
);


// Add everything
thankYouSection.getChildren().addAll(
        thanksTopRow,
        finalThanksBox
);



        // ==========================================
        // BOTTOM CTA
        // ==========================================

        HBox bottomSection = new HBox(40);

        bottomSection.setAlignment(Pos.CENTER);
        bottomSection.setPadding(
                new Insets(30));

        bottomSection.setStyle(
                "-fx-background-color: #D5ECD9;" +
                        "-fx-background-radius: 20;");

        VBox bottomText = new VBox(6);

        Label bottomTitle = new Label(
                "Inspired by innovation?");

        bottomTitle.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT + ";");

        Label bottomDescription = new Label(
                "Join EcoLoad and be part of the future of smart logistics.");

        bottomDescription.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: " + SECONDARY_TEXT + ";");

        bottomText.getChildren().addAll(
                bottomTitle,
                bottomDescription);

        Button joinButton = new Button(
                "Get Started  →");

        joinButton.setStyle(
                "-fx-background-color: " + GREEN + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 28;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;");

        joinButton.setOnAction(e -> {

            RolePage rolePage = new RolePage();

            stage.setScene(
                    rolePage.getRolePageScene());
        });

        bottomSection.getChildren().addAll(
                bottomText,
                joinButton);

        // ==========================================
        // ADD ALL CONTENT
        // ==========================================

        content.getChildren().addAll(
                backButton,
                heroSection,
                founderSection,
                featureHeading,
                featureGrid,
                teamSection,
                thankYouSection,
                bottomSection);

        // ==========================================
        // SCROLL PANE
        // ==========================================

        ScrollPane scrollPane = new ScrollPane(content);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background: " + BG + ";" +
                        "-fx-background-color: " + BG + ";");

        // ==========================================
        // NAVBAR
        // ==========================================

        BorderPane root = new BorderPane();

        Image logoImage = new Image(
                getClass()
                        .getResource(
                                "/assets/icons/Ecoloadnewlogo.png")
                        .toExternalForm());

        ImageView logo = new ImageView(logoImage);

        logo.setFitHeight(43);
        logo.setPreserveRatio(true);

        HBox logoBox = new HBox(logo);

        logoBox.setAlignment(
                Pos.CENTER_LEFT);

        Region spacer = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Button homeButton = new Button("Home");

        homeButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;");

        homeButton.setOnAction(e -> {

            HomePage homePage = new HomePage();

            homePage.showHomePage(stage);
        });

        Label overview = new Label("Overview");
        Label features = new Label("Features");
        Label reviews = new Label("Reviews");
        Label aboutUs = new Label("About Us");
        Label contactUs = new Label("Contact Us");

        String navStyle = "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #263238;" +
                "-fx-cursor: hand;";

        overview.setStyle(navStyle);
        features.setStyle(navStyle);
        reviews.setStyle(navStyle);
        aboutUs.setStyle(navStyle);
        contactUs.setStyle(navStyle);

        HBox navLinks = new HBox(70);

        navLinks.setAlignment(Pos.CENTER);

        navLinks.getChildren().addAll(
                overview,
                features,
                reviews,
                aboutUs,
                contactUs);

        HBox navbar = new HBox(
                25,
                logoBox,
                spacer,
                navLinks,
                spacer2,
                homeButton);

        navbar.setAlignment(
                Pos.CENTER);

        navbar.setPadding(
                new Insets(10, 40, 10, 40));

        navbar.setStyle(
                "-fx-background-color: white;");

        root.setTop(navbar);
        root.setCenter(scrollPane);

        // ==========================================
        // CREATE SCENE
        // ==========================================

        Rectangle2D screen = Screen.getPrimary()
                .getVisualBounds();

        Scene scene = new Scene(
                root,
                screen.getWidth(),
                screen.getHeight());

        stage.setScene(scene);
        stage.setTitle("EcoLoad - About Core2web");
        stage.show();
    }

    // ==========================================
    // FEATURE CARD METHOD
    // ==========================================

    private VBox createFeatureCard(
            String icon,
            String title,
            String description) {

        VBox card = new VBox(10);

        card.setPadding(
                new Insets(20));

        card.setPrefWidth(300);
        card.setMinHeight(180);

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;");

        DropShadow shadow = new DropShadow();

        shadow.setRadius(10);
        shadow.setOffsetY(3);
        shadow.setColor(
                Color.rgb(0, 0, 0, 0.08));

        card.setEffect(shadow);

        Label iconLabel = new Label(icon);

        iconLabel.setStyle(
                "-fx-font-size: 28px;");

        Label titleLabel = new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT + ";");

        Label descriptionLabel = new Label(description);

        descriptionLabel.setWrapText(true);

        descriptionLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: " + SECONDARY_TEXT + ";");

        card.getChildren().addAll(
                iconLabel,
                titleLabel,
                descriptionLabel);

        return card;
    }
}
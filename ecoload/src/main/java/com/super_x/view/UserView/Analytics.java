package com.super_x.view.UserView;

import com.super_x.dao.userdao.LoadDao;
import com.super_x.dao.driverdao.TripDAO;
import com.super_x.model.drivermodel.Trip;
import com.super_x.model.usermodel.CurrentUser;
import com.super_x.model.usermodel.Load;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Analytics {

    // =========================================================
    // COLORS
    // =========================================================

    private static final String GREEN = "#087F43";
    private static final String GREEN_LIGHT = "#55D895";
    private static final String GREEN_PALE = "#BDEED2";
    private static final String GREEN_BG = "#EAF7F0";

    private static final String RED = "#F44343";
    private static final String RED_BG = "#FFF0F0";

    private static final String TEXT = "#18352B";
    private static final String TEXT_DARK = "#10251C";
    private static final String GREY = "#71807A";

    private static final String BORDER = "#DFE9E4";
    private static final String LINE = "#E5ECE8";

    private static final String BACKGROUND = "#F3FAF6";

    /* One immutable, page-local snapshot backs every value on this screen. */
    private AnalyticsData analytics = AnalyticsData.empty();

    // =========================================================
    // MAIN SCENE
    // =========================================================

    public Scene getAnalyticsScene() {

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: " + BACKGROUND + ";");

        // Header
        // root.setTop(createHeader());

        // =====================================================
        // CONTENT
        // =====================================================

        VBox content = new VBox(14);

        content.setPadding(
                new Insets(
                        18,
                        18,
                        25,
                        18));

        content.setFillWidth(true);

        populateContent(content);
        loadAnalyticsOnce(content);

        // =====================================================
        // IMPORTANT:
        // FORCE LABEL COLORS AFTER ALL CONTROLS ARE CREATED
        // =====================================================

        forceLabelColors(content);

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane scrollPane = new ScrollPane(content);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setPannable(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-background: transparent;"
                        + "-fx-border-color: transparent;");

        root.setCenter(scrollPane);

        BorderPane mainContent = new BorderPane();
        mainContent.setTop(UserNavigation.createNavbar());
        mainContent.setCenter(root);

        BorderPane mainroot = new BorderPane();
        mainroot.setLeft(UserNavigation.createSidebar("Analytics"));

        mainroot.setCenter(mainContent);

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene = new Scene(
                mainroot,
                1536,
                750);

        scene.setFill(
                Color.web(BACKGROUND));

        return scene;
    }

    private void populateContent(VBox content) {
        content.getChildren().setAll(
                createPageTitle(), createKpiSection(), createMiddleSection(),
                createRouteSection(), createBottomSection());
        forceLabelColors(content);
    }

    /** Fetches the same user-owned loads used by My Loads exactly once per page instance. */
    private void loadAnalyticsOnce(VBox content) {
        if (!CurrentUser.getInstance().isLoggedIn()
                || CurrentUser.getInstance().getUser().getEmail() == null) {
            return;
        }
        String userId = CurrentUser.getInstance().getUser().getEmail().trim();
        CompletableFuture.supplyAsync(() -> new AnalyticsSnapshot(
                        new LoadDao().getLoadsByUserId(userId), new TripDAO().getTripsByUserId(userId)))
                .thenApply(snapshot -> AnalyticsData.from(snapshot.loads, snapshot.trips))
                .exceptionally(error -> {
                    System.err.println("Unable to load user analytics: " + error.getMessage());
                    return AnalyticsData.empty();
                })
                .thenAccept(data -> Platform.runLater(() -> {
                    analytics = data;
                    populateContent(content);
                }));
    }

    // =========================================================
    // FORCE LABEL COLORS
    // =========================================================

    private void forceLabelColors(
            javafx.scene.Node node) {

        if (node instanceof Label) {

            Label label = (Label) node;

            /*
             * IMPORTANT:
             * Inline CSS prevents your external stylesheet
             * from making the dashboard text white.
             */

            if (label.getText() != null
                    && label.getText().equals("ADMIN PANEL")) {

                label.setStyle(
                        "-fx-text-fill: " + GREEN + ";");

            } else {

                label.setStyle(
                        "-fx-text-fill: " + TEXT + ";");
            }
        }

        if (node instanceof Parent) {

            Parent parent = (Parent) node;

            for (javafx.scene.Node child : parent.getChildrenUnmodifiable()) {

                forceLabelColors(child);
            }
        }
    }

    // =========================================================
    // PAGE TITLE
    // =========================================================

    private VBox createPageTitle() {

        VBox box = new VBox(4);

        Label title = new Label(
                "Analytics Overview");

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        24));

        title.setStyle(
                "-fx-text-fill: " + TEXT_DARK + ";");

        Label subtitle = new Label(
                "Monitor your business performance and transport insights.");

        subtitle.setFont(
                Font.font(
                        "System",
                        FontWeight.NORMAL,
                        11));

        subtitle.setStyle(
                "-fx-text-fill: " + GREY + ";");

        box.getChildren().addAll(
                title,
                subtitle);

        return box;
    }

    // =========================================================
    // KPI SECTION
    // =========================================================

    private HBox createKpiSection() {

        HBox row = new HBox(12);

        row.setAlignment(
                Pos.TOP_LEFT);

        VBox total = createKpiCard(
                "TOTAL LOADS",
                String.valueOf(analytics.totalLoads),
                "▣",
                "All transport loads",
                GREEN);

        VBox delivered = createKpiCard(
                "DELIVERED",
                String.valueOf(analytics.delivered),
                "✓",
                "",
                GREEN);

        VBox cancelled = createKpiCard(
                "CANCELLED",
                String.valueOf(analytics.cancelled),
                "×",
                "",
                RED);

        HBox.setHgrow(
                total,
                Priority.ALWAYS);

        HBox.setHgrow(
                delivered,
                Priority.ALWAYS);

        HBox.setHgrow(
                cancelled,
                Priority.ALWAYS);

        row.getChildren().addAll(
                total,
                delivered,
                cancelled);

        return row;
    }

    // =========================================================
    // KPI CARD
    // =========================================================

    private VBox createKpiCard(
            String heading,
            String number,
            String iconText,
            String bottomText,
            String color) {

        if ("DELIVERED".equals(heading)) bottomText = analytics.deliveredComparison;
        if ("CANCELLED".equals(heading)) bottomText = analytics.cancelledComparison;

        VBox card = new VBox(5);

        card.setPadding(
                new Insets(
                        16,
                        18,
                        16,
                        18));

        card.setPrefHeight(125);
        card.setMinHeight(125);
        card.setMaxHeight(125);

        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 10;"
                        + "-fx-background-radius: 10;");

        HBox top = new HBox();

        top.setAlignment(
                Pos.CENTER_LEFT);

        Label headingLabel = new Label(
                heading);

        headingLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        10));

        headingLabel.setStyle(
                "-fx-text-fill: " + GREY + ";");

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Circle iconCircle = new Circle(
                14,
                Color.web(
                        color.equals(RED)
                                ? RED_BG
                                : GREEN_BG));

        Label icon = new Label(
                iconText);

        icon.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        10));

        icon.setStyle(
                "-fx-text-fill: " + color + ";");

        StackPane iconPane = new StackPane(
                iconCircle,
                icon);

        top.getChildren().addAll(
                headingLabel,
                spacer,
                iconPane);

        Label value = new Label(
                number);

        value.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        30));

        value.setStyle(
                "-fx-text-fill: " + TEXT_DARK + ";");

        Label bottom = new Label(
                bottomText);

        bottom.setFont(
                Font.font(
                        "System",
                        FontWeight.NORMAL,
                        9));

        bottom.setStyle(
                "-fx-text-fill: " + color + ";");

        card.getChildren().addAll(
                top,
                value,
                bottom);

        return card;
    }

    // =========================================================
    // MIDDLE SECTION
    // =========================================================

    private HBox createMiddleSection() {

        HBox row = new HBox(12);

        VBox status = createStatusCard();

        status.setPrefWidth(440);
        status.setMinWidth(400);

        HBox insights = createInsightColumn();

        HBox.setHgrow(
                insights,
                Priority.ALWAYS);

        row.getChildren().addAll(
                status,
                insights);

        return row;
    }

    // =========================================================
    // STATUS CARD
    // =========================================================

    private VBox createStatusCard() {

        VBox card = whiteCard();

        card.setPrefHeight(215);
        card.setMinHeight(215);

        Label title = sectionTitle(
                "Load Status Breakdown");

        Label menu = new Label("⋮");

        menu.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        16));

        menu.setStyle(
                "-fx-text-fill: " + GREY + ";");

        HBox heading = new HBox();

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        heading.getChildren().addAll(
                title,
                spacer,
                menu);

        Line line = new Line(
                0,
                0,
                390,
                0);

        line.setStroke(
                Color.web(LINE));

        HBox chartArea = new HBox(28);

        chartArea.setAlignment(
                Pos.CENTER_LEFT);

        chartArea.setPadding(
                new Insets(
                        8,
                        10,
                        5,
                        10));

        chartArea.getChildren().addAll(
                createDonut(),
                createLegend());

        card.getChildren().addAll(
                heading,
                line,
                chartArea);

        return card;
    }

    // =========================================================
    // DONUT
    // =========================================================

    private StackPane createDonut() {

        StackPane container = new StackPane();

        container.setPrefSize(
                145,
                145);

        PieChart.Data delivered = new PieChart.Data(
                "Delivered",
                analytics.delivered);

        PieChart.Data inTransit = new PieChart.Data(
                "In Transit",
                analytics.inTransit);

        PieChart.Data open = new PieChart.Data(
                "Open",
                analytics.open);

        PieChart.Data cancelled = new PieChart.Data(
                "Cancelled",
                analytics.cancelled);

        PieChart chart = new PieChart();

        chart.getData().addAll(
                delivered,
                inTransit,
                open,
                cancelled);

        chart.setPrefSize(
                135,
                135);

        chart.setMinSize(
                135,
                135);

        chart.setMaxSize(
                135,
                135);

        chart.setLegendVisible(false);
        chart.setLabelsVisible(false);
        chart.setAnimated(false);
        chart.setStartAngle(90);

        chart.setStyle(
                "-fx-background-color: transparent;");

        Circle center = new Circle(
                39,
                Color.WHITE);

        Label number = new Label(String.valueOf(analytics.totalLoads));

        number.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        26));

        number.setStyle(
                "-fx-text-fill: " + TEXT_DARK + ";");

        Label total = new Label(
                "TOTAL LOADS");

        total.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        6));

        total.setStyle(
                "-fx-text-fill: " + GREY + ";");

        VBox centerContent = new VBox(
                0,
                number,
                total);

        centerContent.setAlignment(
                Pos.CENTER);

        container.getChildren().addAll(
                chart,
                center,
                centerContent);

        chart.applyCss();
        chart.layout();

        if (delivered.getNode() != null) {

            delivered.getNode().setStyle(
                    "-fx-pie-color: " + GREEN + ";");
        }

        if (inTransit.getNode() != null) {

            inTransit.getNode().setStyle(
                    "-fx-pie-color: " + GREEN_LIGHT + ";");
        }

        if (open.getNode() != null) {

            open.getNode().setStyle(
                    "-fx-pie-color: " + GREEN_PALE + ";");
        }

        if (cancelled.getNode() != null) {

            cancelled.getNode().setStyle(
                    "-fx-pie-color: " + RED + ";");
        }

        return container;
    }

    // =========================================================
    // LEGEND
    // =========================================================

    private VBox createLegend() {

        VBox legend = new VBox(12);

        legend.setPrefWidth(125);

        legend.getChildren().addAll(

                legendRow(
                        GREEN,
                        "Delivered",
                        String.valueOf(analytics.delivered)),

                legendRow(
                        GREEN_LIGHT,
                        "In Transit",
                        String.valueOf(analytics.inTransit)),

                legendRow(
                        GREEN_PALE,
                        "Open",
                        String.valueOf(analytics.open)),

                legendRow(
                        RED,
                        "Cancelled",
                        String.valueOf(analytics.cancelled)));

        return legend;
    }

    private HBox legendRow(
            String color,
            String name,
            String number) {

        Circle dot = new Circle(
                4.5,
                Color.web(color));

        Label nameLabel = new Label(name);

        nameLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.NORMAL,
                        13));

        nameLabel.setStyle(
                "-fx-text-fill: " + TEXT + ";");

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Label value = new Label(number);

        value.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        9));

        value.setStyle(
                "-fx-text-fill: " + TEXT + ";");

        HBox row = new HBox(
                8,
                dot,
                nameLabel,
                spacer,
                value);

        row.setAlignment(
                Pos.CENTER_LEFT);

        return row;
    }

    // =========================================================
    // INSIGHT CARDS
    // =========================================================

    private HBox createInsightColumn() {

        HBox row = new HBox(12);

        VBox best = createInsight(
                "BEST ROUTE",
                "",
                "",
                "➜");
        best.setPrefHeight(150);
        best.setMinHeight(150);
        best.setMaxHeight(150);

        VBox average = createInsight(
                "AVG DELIVERY TIME",
                "",
                "",
                "◷");
        average.setPrefHeight(150);
        average.setMinHeight(150);
        average.setMaxHeight(150);

        VBox requested = createInsight(
                "MOST REQUESTED",
                "",
                "",
                "▣");
        requested.setPrefHeight(150);
        requested.setMinHeight(150);
        requested.setMaxHeight(150);

        HBox.setHgrow(
                best,
                Priority.ALWAYS);

        HBox.setHgrow(
                average,
                Priority.ALWAYS);

        HBox.setHgrow(
                requested,
                Priority.ALWAYS);

        row.getChildren().addAll(
                best,
                average,
                requested);

        return row;
    }

    // =========================================================
    // SINGLE INSIGHT
    // =========================================================

    private VBox createInsight(
            String heading,
            String value,
            String subtitle,
            String iconText) {

        if ("BEST ROUTE".equals(heading)) {
            value = analytics.bestRoute;
            subtitle = analytics.bestRouteSubtitle;
        } else if ("AVG DELIVERY TIME".equals(heading)) {
            value = analytics.averageDeliveryTime;
            subtitle = analytics.averageDeliverySubtitle;
        } else if ("MOST REQUESTED".equals(heading)) {
            value = analytics.mostRequested;
            subtitle = analytics.mostRequestedSubtitle;
        }

        VBox card = new VBox(7);

        card.setPadding(
                new Insets(
                        13,
                        14,
                        12,
                        14));

        card.setPrefHeight(105);
        card.setMinHeight(105);

        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-radius: 10;"
                        + "-fx-background-radius: 10;");

        HBox top = new HBox(9);

        top.setAlignment(
                Pos.CENTER_LEFT);

        Circle circle = new Circle(
                13,
                Color.web(GREEN_BG));

        Label icon = new Label(iconText);

        icon.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        9));

        icon.setStyle(
                "-fx-text-fill: " + GREEN + ";");

        StackPane iconPane = new StackPane(
                circle,
                icon);

        Label headingLabel = new Label(heading);

        headingLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        9));

        headingLabel.setStyle(
                "-fx-text-fill: " + GREY + ";");

        top.getChildren().addAll(
                iconPane,
                headingLabel);

        Label valueLabel = new Label(value);

        valueLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        16));

        valueLabel.setStyle(
                "-fx-text-fill: " + TEXT_DARK + ";");

        Label subtitleLabel = new Label(subtitle);

        subtitleLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.NORMAL,
                        8));

        subtitleLabel.setWrapText(true);

        subtitleLabel.setStyle(
                "-fx-text-fill: " + GREY + ";");

        card.getChildren().addAll(
                top,
                valueLabel,
                subtitleLabel);

        return card;
    }

    // =========================================================
    // ROUTE SECTION
    // =========================================================

    private VBox createRouteSection() {

        VBox card = whiteCard();

        card.setPrefHeight(200);
        card.setMinHeight(200);
        card.setMaxHeight(200);

        card.setPadding(
                new Insets(
                        14,
                        18,
                        14,
                        18));

        // =====================================================
        // TITLE
        // =====================================================

        Label title = new Label(
                "Top Routes");

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        14));

        title.setStyle(
                "-fx-text-fill: " + TEXT + ";");

        // =====================================================
        // ROUTES
        // =====================================================

        VBox routes = new VBox(
                10);

        for (RouteTotal route : analytics.topRoutes) {
            double proportion = analytics.topRoutes.isEmpty() || analytics.topRoutes.get(0).amount <= 0
                    ? 0 : route.amount / analytics.topRoutes.get(0).amount;
            routes.getChildren().add(routeRow(route.route, formatRupees(route.amount), proportion, GREEN));
        }

        card.getChildren().addAll(
                title,
                routes);

        return card;
    }

    // =========================================================
    // ROUTE ROW
    // =========================================================

    private VBox routeRow(
            String route,
            String revenue,
            double percentage,
            String color) {

        VBox row = new VBox(5);

        // =====================================================
        // TOP LINE : ROUTE + REVENUE
        // =====================================================

        HBox topLine = new HBox(8);

        Label routeLabel = new Label(
                route);

        routeLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12));

        routeLabel.setStyle(
                "-fx-text-fill: " + TEXT + ";");

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Label revenueLabel = new Label(
                revenue);

        revenueLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12));

        revenueLabel.setStyle(
                "-fx-text-fill: " + TEXT + ";");

        topLine.getChildren().addAll(
                routeLabel,
                spacer,
                revenueLabel);

        topLine.setAlignment(
                Pos.CENTER_LEFT);

        // =====================================================
        // FULL WIDTH BACKGROUND BAR
        // =====================================================

        // StackPane bar =
        // new StackPane();

        // bar.setPrefHeight(6);
        // bar.setMinHeight(6);
        // bar.setMaxHeight(6);

        // bar.setMaxWidth(
        // Double.MAX_VALUE
        // );

        // Region background =
        // new Region();

        // background.setMaxWidth(
        // Double.MAX_VALUE
        // );

        // background.setPrefHeight(6);
        // background.setMaxHeight(6);

        // background.setStyle(
        // "-fx-background-color: #E2E7E4;"
        // + "-fx-background-radius: 6;"
        // );

        // // =====================================================
        // // GREEN PROGRESS
        // // =====================================================

        // Region fill =
        // new Region();

        // fill.setPrefHeight(6);
        // fill.setMaxHeight(6);

        // fill.prefWidthProperty().bind(
        // bar.widthProperty()
        // .multiply(
        // percentage
        // )
        // );

        // fill.setStyle(
        // "-fx-background-color: " + color + ";"
        // + "-fx-background-radius: 6;"
        // );

        // StackPane.setAlignment(
        // fill,
        // Pos.CENTER_LEFT
        // );

        // bar.getChildren().addAll(
        // background,
        // fill
        // );

        // // =====================================================
        // // ROW
        // // =====================================================

        StackPane bar = new StackPane();

        double barWidth = 320 * percentage;

        bar.setPrefWidth(barWidth);
        bar.setMinWidth(barWidth);
        bar.setMaxWidth(barWidth);

        bar.setPrefHeight(6);
        bar.setMinHeight(6);
        bar.setMaxHeight(6);

        Region fill = new Region();

        fill.setPrefWidth(barWidth);
        fill.setMinWidth(barWidth);
        fill.setMaxWidth(barWidth);

        fill.setPrefHeight(6);
        fill.setMinHeight(6);
        fill.setMaxHeight(6);

        fill.setStyle(
                "-fx-background-color: " + color + ";"
                        + "-fx-background-radius: 6;");

        bar.getChildren().add(
                fill);
        row.getChildren().addAll(
                topLine,
                bar);

        return row;
    }

    // =========================================================
    // BOTTOM SECTION
    // =========================================================

    private HBox createBottomSection() {

        HBox row = new HBox(12);

        VBox monthly = createMonthly();

        VBox recent = createRecent();

        HBox.setHgrow(
                monthly,
                Priority.ALWAYS);

        HBox.setHgrow(
                recent,
                Priority.ALWAYS);

        row.getChildren().addAll(
                monthly,
                recent);

        return row;
    }

    // =========================================================
    // MONTHLY
    // =========================================================

    private VBox createMonthly() {

        VBox card = whiteCard();

        card.setPrefHeight(210);
        card.setMinHeight(210);

        HBox heading = new HBox();

        Label title = sectionTitle(
                "Monthly Performance");

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Label report = new Label(
                "View Report");

        report.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        8));

        report.setStyle(
                "-fx-text-fill: " + GREEN + ";");

        heading.getChildren().addAll(
                title,
                spacer,
                report);

        GridPane table = new GridPane();

        table.setHgap(35);
        table.setVgap(12);

        String[] headers = {
                "MONTH",
                "LOADS\nPOSTED",
                "DELIVERED",
                "CANCELLED",
                "GROWTH"
        };

        for (int i = 0; i < headers.length; i++) {

            Label header = new Label(
                    headers[i]);

            header.setFont(
                    Font.font(
                            "System",
                            FontWeight.BOLD,
                            7));

            header.setStyle(
                    "-fx-text-fill: " + GREY + ";");

            table.add(
                    header,
                    i,
                    0);
        }

        for (int index = 0; index < analytics.months.size(); index++) {
            MonthlyStats month = analytics.months.get(index);
            addMonthlyRow(table, index + 1, month.label, String.valueOf(month.total),
                    String.valueOf(month.delivered), String.valueOf(month.cancelled), month.growth);
        }

        card.getChildren().addAll(
                heading,
                table);

        return card;
    }

    // =========================================================
    // MONTHLY ROW
    // =========================================================

    private void addMonthlyRow(
            GridPane table,
            int row,
            String month,
            String loads,
            String delivered,
            String cancelled,
            String growth) {

        String[] values = {
                month,
                loads,
                delivered,
                cancelled,
                growth
        };

        for (int i = 0; i < values.length; i++) {

            Label label = new Label(
                    values[i]);

            label.setFont(
                    Font.font(
                            "System",
                            i == 0
                                    ? FontWeight.BOLD
                                    : FontWeight.NORMAL,
                            12));

            String color = i == 4
                    ? (growth.startsWith("-")
                            ? RED
                            : GREEN)
                    : TEXT;

            label.setStyle(
                    "-fx-text-fill: " + color + ";");

            table.add(
                    label,
                    i,
                    row);
        }
    }

    // =========================================================
    // RECENT ACTIVITY
    // =========================================================

    private VBox createRecent() {

        VBox card = whiteCard();

        card.setPrefHeight(260);
        card.setMinHeight(260);
        card.setMaxHeight(260);

        Label title = sectionTitle(
                "Recent Activity");

        VBox activities = new VBox(12);

        for (Activity activity : analytics.recentActivities) {
            activities.getChildren().add(activityRow(activity.color, activity.title, activity.subtitle));
        }

        card.getChildren().addAll(
                title,
                activities);

        return card;
    }

    // =========================================================
    // ACTIVITY ROW
    // =========================================================

    private HBox activityRow(
            String color,
            String title,
            String subtitle) {

        // =====================================================
        // CIRCLE
        // =====================================================

        Circle dot = new Circle(
                6,
                Color.web(color));

        // =====================================================
        // VERTICAL LINE
        // =====================================================

        Region line = new Region();

        line.setPrefWidth(2);
        line.setMinWidth(2);
        line.setMaxWidth(2);

        line.setPrefHeight(45);

        line.setStyle(
                "-fx-background-color: #CDE8D9;");

        // =====================================================
        // TIMELINE
        // =====================================================

        VBox timeline = new VBox();

        timeline.setAlignment(
                Pos.TOP_CENTER);

        timeline.setPrefWidth(14);
        timeline.setMinWidth(14);
        timeline.setMaxWidth(14);

        timeline.getChildren().addAll(
                dot,
                line);

        // =====================================================
        // TITLE
        // =====================================================

        Label titleLabel = new Label(
                title);

        titleLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        12));

        titleLabel.setStyle(
                "-fx-text-fill: " + TEXT + ";");

        // =====================================================
        // SUBTITLE
        // =====================================================

        Label subtitleLabel = new Label(
                subtitle);

        subtitleLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.NORMAL,
                        10));

        subtitleLabel.setStyle(
                "-fx-text-fill: " + GREY + ";");

        // =====================================================
        // TEXT
        // =====================================================

        VBox text = new VBox(
                3);

        text.getChildren().addAll(
                titleLabel,
                subtitleLabel);

        // =====================================================
        // MAIN ROW
        // =====================================================

        HBox row = new HBox(
                10,
                timeline,
                text);

        row.setAlignment(
                Pos.TOP_LEFT);

        return row;
    }

    private static String formatRupees(double amount) {
        return String.format(Locale.US, "₹%,.0f", amount);
    }

    private static final class AnalyticsData {
        final int totalLoads, delivered, inTransit, open, cancelled;
        final String deliveredComparison, cancelledComparison;
        final String bestRoute, bestRouteSubtitle;
        final String averageDeliveryTime, averageDeliverySubtitle;
        final String mostRequested, mostRequestedSubtitle;
        final List<RouteTotal> topRoutes;
        final List<MonthlyStats> months;
        final List<Activity> recentActivities;

        private AnalyticsData(int totalLoads, int delivered, int inTransit, int open, int cancelled,
                              String deliveredComparison, String cancelledComparison, String bestRoute,
                              String bestRouteSubtitle, String averageDeliveryTime, String averageDeliverySubtitle,
                              String mostRequested, String mostRequestedSubtitle, List<RouteTotal> topRoutes,
                              List<MonthlyStats> months, List<Activity> recentActivities) {
            this.totalLoads = totalLoads; this.delivered = delivered; this.inTransit = inTransit;
            this.open = open; this.cancelled = cancelled;
            this.deliveredComparison = deliveredComparison; this.cancelledComparison = cancelledComparison;
            this.bestRoute = bestRoute; this.bestRouteSubtitle = bestRouteSubtitle;
            this.averageDeliveryTime = averageDeliveryTime; this.averageDeliverySubtitle = averageDeliverySubtitle;
            this.mostRequested = mostRequested; this.mostRequestedSubtitle = mostRequestedSubtitle;
            this.topRoutes = topRoutes; this.months = months; this.recentActivities = recentActivities;
        }

        static AnalyticsData empty() { return from(List.of(), List.of()); }

        static AnalyticsData fromLoads(List<Load> source) {
            return from(source, List.of());
        }

        static AnalyticsData from(List<Load> source, List<Trip> trips) {
            List<Load> loads = source == null ? List.of() : source;
            int delivered = 0, inTransit = 0, open = 0, cancelled = 0;
            Map<String, Integer> deliveredRoutes = new HashMap<>();
            Map<String, Double> routeAmounts = new HashMap<>();
            Map<String, Integer> truckTypes = new HashMap<>();
            Map<YearMonth, Integer> postedMonthly = new HashMap<>();
            Map<YearMonth, Integer> deliveredMonthly = new HashMap<>();
            Map<YearMonth, Integer> cancelledMonthly = new HashMap<>();
            Map<YearMonth, int[]> monthly = new HashMap<>();
            List<Activity> activities = new ArrayList<>();

            for (Load load : loads) {
                if (load == null) continue;
                String status = canonicalStatus(load.getStatus());
                if (isDelivered(status)) delivered++;
                else if ("CANCELLED".equals(status)) cancelled++;
                else if ("IN_TRANSIT".equals(status)) inTransit++;
                else open++; // pending, accepted, pickup, dispatch and arrived remain visible as open/in-progress.

                String route = routeOf(load);
                if (route != null) {
                    if (isDelivered(status)) deliveredRoutes.merge(route, 1, Integer::sum);
                    if (load.getOfferPrice() > 0) routeAmounts.merge(route, load.getOfferPrice(), Double::sum);
                }
                String truckType = clean(load.getTruckType());
                if (truckType != null) truckTypes.merge(truckType, 1, Integer::sum);

                LocalDate created = parseDate(load.getCreatedAt());
                if (created != null) {
                    int[] values = monthly.computeIfAbsent(YearMonth.from(created), ignored -> new int[3]);
                    values[0]++;
                    if (isDelivered(status)) values[1]++;
                    if ("CANCELLED".equals(status)) values[2]++;
                    if (activities.size() < 4) {
                        String loadId = clean(load.getLoadId());
                        activities.add(new Activity(activityColor(status),
                                "Load " + (loadId == null ? "updated" : "#" + loadId) + " " + displayStatus(status),
                                (route == null ? "Route not recorded" : route) + " • " + created));
                    }
                }
            }

            // Posting time comes from createdAt when present, otherwise from the
            // yyyyMMdd component generated in the existing load document ID.
            // It never uses scheduled pickup/delivery dates as a posting date.
            for (Load load : loads) {
                if (load == null) continue;
                String route = routeOf(load);
                LocalDateTime postedAt = loadPostedAt(load);
                if (postedAt != null) {
                    postedMonthly.merge(YearMonth.from(postedAt), 1, Integer::sum);
                    activities.add(Activity.of(GREEN, "Load Posted", route, postedAt, hasTime(load.getCreatedAt())));
                }
                LocalDateTime acceptedAt = parseDateTime(load.getAcceptedAt());
                if (acceptedAt != null) activities.add(Activity.of(GREEN_LIGHT, "Trip Accepted", route, acceptedAt, true));
                LocalDateTime paymentAt = parseDateTime(load.getPaymentCompletedAt());
                if (paymentAt != null) activities.add(Activity.of(GREEN, "Payment Completed", route, paymentAt, true));
            }
            for (Trip trip : trips == null ? List.<Trip>of() : trips) {
                if (trip == null) continue;
                String status = canonicalStatus(trip.getStatus());
                String route = tripRouteOf(trip);
                LocalDateTime started = parseDateTime(trip.getStartTime());
                LocalDateTime completed = parseDateTime(trip.getCompletedTime());
                LocalDateTime cancelledAt = parseDateTime(trip.getCancelledTime());
                if (started != null) activities.add(Activity.of(GREEN_LIGHT, "Trip In Transit", route, started, true));
                if (isDelivered(status) && completed != null) {
                    deliveredMonthly.merge(YearMonth.from(completed), 1, Integer::sum);
                    activities.add(Activity.of(GREEN, "Trip Delivered", route, completed, true));
                }
                if ("CANCELLED".equals(status) && cancelledAt != null) {
                    cancelledMonthly.merge(YearMonth.from(cancelledAt), 1, Integer::sum);
                    activities.add(Activity.of(RED, "Trip Cancelled", route, cancelledAt, true));
                }
            }

            List<RouteTotal> routes = routeAmounts.entrySet().stream()
                    .map(entry -> new RouteTotal(entry.getKey(), entry.getValue()))
                    .sorted(Comparator.comparingDouble((RouteTotal route) -> route.amount).reversed())
                    .limit(3).toList();
            String bestRoute = deliveredRoutes.entrySet().stream()
                    .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("No completed route");
            String bestSubtitle = deliveredRoutes.isEmpty() ? "No delivered loads recorded."
                    : "Most delivered loads.";
            Map.Entry<String, Integer> requested = truckTypes.entrySet().stream()
                    .max(Map.Entry.comparingByValue()).orElse(null);
            String requestedValue = requested == null ? "No truck type recorded" : requested.getKey();
            String requestedSubtitle = requested == null ? "No requested truck type recorded."
                    : "Accounts for " + Math.round(requested.getValue() * 100.0 / loads.size()) + "% of volume.";

            List<MonthlyStats> months = new ArrayList<>();
            YearMonth cursor = YearMonth.now();
            for (int index = 0; index < 3; index++, cursor = cursor.minusMonths(1)) {
                int posted = postedMonthly.getOrDefault(cursor, 0);
                int previous = postedMonthly.getOrDefault(cursor.minusMonths(1), 0);
                String growth = previous == 0 ? "—" : String.format(Locale.US, "%+.1f%%",
                        (posted - previous) * 100.0 / previous);
                months.add(new MonthlyStats(cursor.getMonth().getDisplayName(TextStyle.SHORT, Locale.US),
                        posted, deliveredMonthly.getOrDefault(cursor, 0),
                        cancelledMonthly.getOrDefault(cursor, 0), growth));
            }
            activities.sort(Comparator.comparing((Activity activity) -> activity.when).reversed());
            if (activities.size() > 4) activities = new ArrayList<>(activities.subList(0, 4));
            if (activities.isEmpty()) activities.add(new Activity(GREEN_PALE, "No recent activity", "No dated load or trip event is recorded.", null, false));
            int currentDelivered = deliveredMonthly.getOrDefault(YearMonth.now(), 0);
            int previousDelivered = deliveredMonthly.getOrDefault(YearMonth.now().minusMonths(1), 0);
            int currentCancelled = cancelledMonthly.getOrDefault(YearMonth.now(), 0);
            int previousCancelled = cancelledMonthly.getOrDefault(YearMonth.now().minusMonths(1), 0);
            double durationDays = averageDeliveryDays(trips);
            String duration = durationDays < 0 ? "0.0 Days" : String.format(Locale.US, "%.1f Days", durationDays);
            String durationSubtitle = durationDays < 0 ? "No actual delivery timestamps recorded."
                    : "Based on completed trip timestamps.";
            return new AnalyticsData(loads.size(), delivered, inTransit, open, cancelled,
                    comparison(currentDelivered, previousDelivered), comparison(currentCancelled, previousCancelled), bestRoute, bestSubtitle,
                    duration, durationSubtitle, requestedValue, requestedSubtitle,
                    routes, months, activities);
        }

        private static String canonicalStatus(String value) {
            return value == null ? "" : value.trim().toUpperCase(Locale.ROOT).replace(' ', '_');
        }
        private static boolean isDelivered(String status) { return "DELIVERED".equals(status) || "COMPLETED".equals(status); }
        private static String clean(String value) { return value == null || value.isBlank() ? null : value.trim(); }
        private static String routeOf(Load load) {
            String pickup = clean(load.getPickupLocation()), destination = clean(load.getDestination());
            return pickup == null || destination == null ? null : pickup + " → " + destination;
        }
        private static LocalDate parseDate(String value) {
            String text = clean(value);
            if (text == null) return null;
            try { return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE); }
            catch (Exception ignored) {
                LocalDateTime dateTime = parseDateTime(text);
                return dateTime == null ? null : dateTime.toLocalDate();
            }
        }
        private static LocalDateTime loadPostedAt(Load load) {
            LocalDateTime created = parseDateTime(load.getCreatedAt());
            if (created != null) return created;
            String loadId = clean(load.getLoadId());
            if (loadId == null) return null;
            String[] parts = loadId.split("-");
            if (parts.length < 2 || !parts[1].matches("\\d{8}")) return null;
            try { return LocalDate.parse(parts[1], DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay(); }
            catch (Exception ignored) { return null; }
        }
        private static boolean hasTime(String value) {
            String text = clean(value);
            return text != null && (text.contains(":") || text.contains("T"));
        }
        private static String tripRouteOf(Trip trip) {
            String pickup = clean(trip.getPickupLocation()), destination = clean(trip.getDestination());
            return pickup == null || destination == null ? null : pickup + " → " + destination;
        }
        private static String displayStatus(String status) { return status.isBlank() ? "Updated" : status.replace('_', ' '); }
        private static String activityColor(String status) { return isDelivered(status) ? GREEN : "CANCELLED".equals(status) ? RED : GREEN_LIGHT; }
        private static String comparison(int current, int previous) {
            if (previous == 0) return "Comparison unavailable";
            double change = (current - previous) * 100.0 / previous;
            return String.format(Locale.US, "%s %.1f%% vs last month", change >= 0 ? "↑" : "↓", Math.abs(change));
        }
        private static double averageDeliveryDays(List<Trip> trips) {
            if (trips == null) return -1;
            long totalMinutes = 0; int count = 0;
            for (Trip trip : trips) {
                if (trip == null || !isDelivered(canonicalStatus(trip.getStatus()))) continue;
                LocalDateTime start = parseDateTime(trip.getStartTime());
                LocalDateTime completed = parseDateTime(trip.getCompletedTime());
                if (start != null && completed != null && !completed.isBefore(start)) {
                    totalMinutes += Duration.between(start, completed).toMinutes(); count++;
                }
            }
            return count == 0 ? -1 : totalMinutes / (double) count / (24 * 60);
        }
        private static LocalDateTime parseDateTime(String value) {
            String text = clean(value);
            if (text == null) return null;
            try { return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); }
            catch (Exception ignored) {
                try { return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME); }
                catch (Exception ignoredAgain) {
                    try { return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(); }
                    catch (Exception ignoredOnceMore) { return null; }
                }
            }
        }
    }

    private static final class AnalyticsSnapshot {
        final List<Load> loads; final List<Trip> trips;
        AnalyticsSnapshot(List<Load> loads, List<Trip> trips) { this.loads = loads; this.trips = trips; }
    }

    private static final class RouteTotal { final String route; final double amount; RouteTotal(String route, double amount) { this.route = route; this.amount = amount; } }
    private static final class MonthlyStats { final String label, growth; final int total, delivered, cancelled; MonthlyStats(String label, int total, int delivered, int cancelled, String growth) { this.label = label; this.total = total; this.delivered = delivered; this.cancelled = cancelled; this.growth = growth; } }
    private static final class Activity {
        final String color, title, subtitle;
        final LocalDateTime when;
        final boolean hasTime;
        Activity(String color, String title, String subtitle) { this(color, title, subtitle, LocalDateTime.MIN, false); }
        Activity(String color, String title, String subtitle, LocalDateTime when, boolean hasTime) {
            this.color = color; this.title = title; this.subtitle = subtitle; this.when = when; this.hasTime = hasTime;
        }
        static Activity of(String color, String title, String route, LocalDateTime when, boolean hasTime) {
            String routeText = route == null ? "Route not recorded" : route;
            return new Activity(color, title, routeText + " • " + formatWhen(when, hasTime), when, hasTime);
        }
        private static String formatWhen(LocalDateTime when, boolean hasTime) {
            if (!hasTime) return when.toLocalDate().format(DateTimeFormatter.ofPattern("dd MMM uuuu", Locale.US));
            long minutes = Duration.between(when, LocalDateTime.now()).toMinutes();
            if (minutes < 0) return when.format(DateTimeFormatter.ofPattern("dd MMM uuuu", Locale.US));
            if (minutes < 60) return Math.max(minutes, 1) + " min ago";
            if (minutes < 24 * 60) return (minutes / 60) + " hours ago";
            if (minutes < 7 * 24 * 60) return (minutes / (24 * 60)) + " days ago";
            if (minutes < 28 * 24 * 60) return (minutes / (7 * 24 * 60)) + " weeks ago";
            return when.toLocalDate().format(DateTimeFormatter.ofPattern("dd MMM uuuu", Locale.US));
        }
    }

    // =========================================================
    // WHITE CARD
    // =========================================================

    private VBox whiteCard() {

        VBox card = new VBox(10);

        card.setPadding(
                new Insets(
                        14,
                        15,
                        14,
                        15));

        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-color: " + BORDER + ";"
                        + "-fx-border-width: 1;"
                        + "-fx-border-radius: 10;"
                        + "-fx-background-radius: 10;"
                        + "-fx-effect: dropshadow("
                        + "gaussian,"
                        + "rgba(0,0,0,0.025),"
                        + "5,"
                        + "0,"
                        + "0,"
                        + "1"
                        + ");");

        return card;
    }

    // =========================================================
    // SECTION TITLE
    // =========================================================

    private Label sectionTitle(
            String text) {

        Label title = new Label(text);

        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        16));

        title.setStyle(
                "-fx-text-fill: " + TEXT_DARK + ";");

        return title;
    }
}

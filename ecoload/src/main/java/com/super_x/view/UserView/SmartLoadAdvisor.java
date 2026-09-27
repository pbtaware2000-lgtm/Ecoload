package com.super_x.view.UserView;

import com.super_x.ai.SmartLoadAdvisorService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.concurrent.CompletableFuture;

/** Pre-posting advisor powered by transparent, deterministic project rules. */
public class SmartLoadAdvisor {
    private static final String GREEN = "#087F43";
    private static final String DARK = "#18352B";
    private static final String MUTED = "#71807A";
    private static final String BG = "#F3FAF6";
    private final SmartLoadAdvisorService advisor = new SmartLoadAdvisorService();
    private TextField pickup, destination, weight, offer;
    private ComboBox<String> loadType, unit, truckType;
    private VBox result;
    private Button analyze;

    public Scene getSmartLoadAdvisorScene() {
        BorderPane page = new BorderPane();
        page.setStyle("-fx-background-color: " + BG + ";");
        VBox content = new VBox(18);
        content.setPadding(new Insets(24));
        content.getChildren().addAll(title(), form(), recommendationPlaceholder());
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle(
                "-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        page.setCenter(scroll);
        BorderPane main = new BorderPane(page);
        main.setTop(UserNavigation.createNavbar());
        BorderPane root = new BorderPane(main);
        root.setLeft(UserNavigation.createSidebar("Smart Load Advisor"));
        return new Scene(root, 1536, 750, Color.web(BG));
    }

    private VBox title() {
        VBox box = new VBox(5);
        box.getChildren().addAll(label("🤖  SMART LOAD ADVISOR", DARK, 28, true), label(
                "Deterministic pre-posting guidance using registered EcoLoad vehicle capacities.", MUTED, 14, false));
        return box;
    }

    private VBox form() {
        VBox card = card();
        card.getChildren().add(label("YOUR LOAD", DARK, 16, true));
        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);
        grid.getColumnConstraints().addAll(column(), column());
        pickup = field("Pickup location");
        destination = field("Destination");
        loadType = combo("Select load type", "General", "Perishable", "Fragile", "Electronics", "Furniture",
                "Heavy Goods");
        weight = field("Weight");
        unit = combo("Unit", "Ton", "Quintal");
        HBox weightRow = new HBox(10, weight, unit);
        HBox.setHgrow(weight, Priority.ALWAYS);
        truckType = combo("Optional truck type", "Mini Truck", "Pickup Truck", "LCV", "Medium Truck", "Heavy Truck",
                "Trailer", "Container Truck", "Tanker");
        offer = field("Optional offer price");
        grid.add(labeled("Pickup Location", pickup), 0, 0);
        grid.add(labeled("Destination", destination), 1, 0);
        grid.add(labeled("Load Type", loadType), 0, 1);
        grid.add(labeled("Weight", weightRow), 1, 1);
        grid.add(labeled("Truck Type", truckType), 0, 2);
        grid.add(labeled("Offer Price", offer), 1, 2);
        analyze = new Button("ANALYZE LOAD");
        analyze.setStyle("-fx-background-color: " + GREEN
                + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 9; -fx-padding: 12 22;");
        analyze.setOnAction(event -> analyze());
        card.getChildren().addAll(grid, analyze);
        return card;
    }

    private VBox recommendationPlaceholder() {
        result = card();
        result.getChildren().addAll(label("SMART RECOMMENDATION", DARK, 16, true),
                label("Enter your load details and select Analyze Load. This does not create or post a load.", MUTED,
                        14, false));
        return result;
    }

    private void analyze() {
        double parsedWeight;
        Double price = null;
        try {
            parsedWeight = Double.parseDouble(weight.getText().trim());
            if (!offer.getText().isBlank())
                price = Double.parseDouble(offer.getText().trim());
        } catch (NumberFormatException error) {
            showError("Weight and offer price, if entered, must be valid numbers.");
            return;
        }
        SmartLoadAdvisorService.Request request = new SmartLoadAdvisorService.Request(pickup.getText(),
                destination.getText(), loadType.getValue(), parsedWeight, unit.getValue(), truckType.getValue(), price);
        analyze.setDisable(true);
        result.getChildren().setAll(label("SMART RECOMMENDATION", DARK, 16, true),
                label("Analyzing registered vehicle capacities…", MUTED, 14, false));
        CompletableFuture.supplyAsync(() -> {
            try {
                return advisor.analyze(request);
            } catch (Exception error) {
                throw new java.util.concurrent.CompletionException(error);
            }
        })
                .whenComplete((advice, error) -> Platform.runLater(() -> {
                    analyze.setDisable(false);
                    if (error != null)
                        showError(rootMessage(error));
                    else
                        showAdvice(advice);
                }));
    }

    private void showAdvice(SmartLoadAdvisorService.Advice advice) {
        result.getChildren().clear();
        result.getChildren().add(label("SMART RECOMMENDATION", DARK, 16, true));
        if (advice.vehicleType() == null)
            result.getChildren().add(label("No suitable registered vehicle found", "#B42318", 17, true));
        else
            result.getChildren().addAll(value("🚚 Recommended Vehicle", advice.vehicleType()),
                    value("📦 Recommended Capacity", String.format("%.2f Ton", advice.capacityTons())),
                    value("💡 Load Utilization", String.format("%.0f%%", advice.utilizationPercent())));
        result.getChildren().addAll(
                value("📦 Packaging Recommendation", "• " + String.join("\n• ", advice.packaging())),
                value("⚠ Handling Recommendation", advice.handling()), value("💰 Pricing Guidance", advice.pricing()),
                value("★ FINAL RECOMMENDATION", advice.finalRecommendation()),
                label(advice.rationale(), MUTED, 12, false));
    }

    private void showError(String message) {
        result.getChildren().setAll(label("SMART RECOMMENDATION", DARK, 16, true),
                label(message, "#B42318", 14, false));
    }

    private String rootMessage(Throwable error) {
        Throwable cause = error;
        while (cause.getCause() != null)
            cause = cause.getCause();
        return cause.getMessage() == null ? "Unable to analyze the load. Please try again." : cause.getMessage();
    }

    private ColumnConstraints column() {
        ColumnConstraints c = new ColumnConstraints();
        c.setPercentWidth(50);
        c.setHgrow(Priority.ALWAYS);
        return c;
    }

    private VBox labeled(String title, javafx.scene.Node node) {
        VBox box = new VBox(5, label(title, DARK, 13, true), node);
        return box;
    }

    private TextField field(String prompt) {
        TextField text = new TextField();
        text.setPromptText(prompt);
        text.setPrefHeight(42);
        text.setStyle(
                "-fx-background-color: #F8FAFC; -fx-border-color: #DFE9E4; -fx-border-radius: 7; -fx-background-radius: 7;");
        return text;
    }

    private ComboBox<String> combo(String prompt, String... values) {
        ComboBox<String> box = new ComboBox<>();
        box.getItems().addAll(values);
        box.setPromptText(prompt);
        box.setPrefHeight(42);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setStyle(
                "-fx-background-color: #F8FAFC; -fx-border-color: #DFE9E4; -fx-border-radius: 7; -fx-background-radius: 7;");
        return box;
    }

    private VBox value(String title, String body) {
        VBox box = new VBox(3);
        box.getChildren().addAll(label(title, DARK, 13, true), label(body, MUTED, 14, false));
        return box;
    }

    private VBox card() {
        VBox box = new VBox(14);
        box.setPadding(new Insets(20));
        box.setStyle(
                "-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #DFE9E4; -fx-border-radius: 12;");
        return box;
    }

    private Label label(String value, String color, double size, boolean bold) {
        Label label = new Label(value);
        label.setWrapText(true);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setStyle("-fx-text-fill: " + color + "; -fx-font-size: " + size + "px;"
                + (bold ? " -fx-font-weight: bold;" : ""));
        return label;
    }
}

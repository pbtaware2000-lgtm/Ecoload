package com.super_x.view.DriverView;

import com.super_x.ai.VehicleTripInsight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/** Driver-only presentation of the explainable vehicle and route analysis. */
public final class VehicleTripInsightDialog {
    private VehicleTripInsightDialog() { }

    public static void show(Window owner, VehicleTripInsight insight, Runnable acceptTrip) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("AI Vehicle & Trip Insight");

        Label title = new Label("AI VEHICLE & TRIP INSIGHT");
        title.setStyle("-fx-font-size: 21px; -fx-font-weight: bold; -fx-text-fill: #09691A;");
        Label note = new Label("Rule-based insight from your registered vehicle and this shipment.");
        note.setWrapText(true);
        note.setStyle("-fx-font-size: 12px; -fx-text-fill: #657066;");

        GridPane metrics = new GridPane();
        metrics.setHgap(28); metrics.setVgap(13);
        metrics.getColumnConstraints().addAll(new ColumnConstraints(180), new ColumnConstraints(285));
        add(metrics, 0, "Shipment load", insight.loadDisplay());
        add(metrics, 1, "Vehicle capacity", insight.vehicleCapacityDisplay());
        add(metrics, 2, "Capacity utilization", insight.utilizationDisplay());
        add(metrics, 3, "Vehicle suitability", insight.suitability());
        add(metrics, 4, "Offer price", insight.offerPrice());
        add(metrics, 5, "Vehicle usage score", insight.vehicleUsageScore());
        add(metrics, 6, "Eco score", insight.ecoScore());
        add(metrics, 7, "Trip risk", insight.tripRisk());

        Label decisionTitle = new Label("AI TRIP DECISION");
        decisionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #09691A;");
        GridPane decisionGrid = new GridPane();
        decisionGrid.setHgap(28); decisionGrid.setVgap(10);
        decisionGrid.getColumnConstraints().addAll(new ColumnConstraints(180), new ColumnConstraints(285));
        add(decisionGrid, 0, "Overall score", insight.overallScore());
        add(decisionGrid, 1, "Vehicle match", insight.vehicleMatch());
        add(decisionGrid, 2, "Load suitability", insight.loadSuitability());
        add(decisionGrid, 3, "Risk", insight.tripRisk());
        add(decisionGrid, 4, "Final decision", insight.finalDecision());

        Label recTitle = new Label("AI Recommendation");
        recTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #09691A;");
        Label recommendation = new Label(insight.recommendation());
        recommendation.setWrapText(true);
        recommendation.setStyle("-fx-font-size: 13px; -fx-text-fill: #182219;");
        VBox recommendationBox = new VBox(7, recTitle, recommendation);
        recommendationBox.setPadding(new Insets(12));
        recommendationBox.setStyle("-fx-background-color: #EAF5EC; -fx-background-radius: 10;");

        Button close = new Button("Close");
        close.setOnAction(event -> dialog.close());
        Button accept = new Button("ACCEPT TRIP");
        accept.setDisable(!insight.suitable());
        accept.setStyle("-fx-background-color: #228670; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        accept.setOnAction(event -> { dialog.close(); acceptTrip.run(); });
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox actions = new HBox(10, spacer, close, accept); actions.setAlignment(Pos.CENTER_RIGHT);

        VBox content = new VBox(14, title, note, new Separator(), metrics, new Separator(),
                decisionTitle, decisionGrid, new Separator(), recommendationBox, actions);
        content.setPadding(new Insets(22));
        content.setStyle("-fx-background-color: #F9F9F8;");
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #F9F9F8; -fx-background-color: #F9F9F8;");
        dialog.setScene(new Scene(scroll, 570, 700));
        dialog.showAndWait();
    }

    private static void add(GridPane grid, int row, String name, String value) {
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #657066;");
        Label valueLabel = new Label(value);
        valueLabel.setWrapText(true);
        valueLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #182219;");
        grid.add(nameLabel, 0, row); grid.add(valueLabel, 1, row);
    }
}

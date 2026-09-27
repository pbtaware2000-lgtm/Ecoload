package com.super_x.view.DriverView;

import com.super_x.dao.driverdao.DriverEarningsDAO;
import com.super_x.model.drivermodel.CurrentDriver;
import com.super_x.model.drivermodel.DriverEarningsData;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.drivermodel.EarningsTransaction;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/** Driver-only page for verified wallet credits and current wallet balance. */
public class DriverEarnings {
    private static final String GREEN = "#014B3A";
    private static final String MUTED = "#657066";
    private final DriverEarningsDAO earningsDAO = new DriverEarningsDAO();
    private final Label totalEarnings = valueLabel();
    private final Label thisMonth = valueLabel();
    private final Label verifiedPayments = valueLabel();
    private final Label walletBalance = valueLabel();
    private final TableView<EarningsTransaction> history = new TableView<>();
    private final StackPane chartArea = new StackPane();
    private final Label state = new Label("Loading earnings...");

    public Scene getDriverEarningsScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #e6f1e8;");
        root.setLeft(DriverNavigation.createSidebar("Earnings"));
        VBox page = new VBox(18, DriverNavigation.createNavbar(), content());
        root.setCenter(page);
        loadEarnings();
        return new Scene(root, 1536, 750, Color.web("#F9F9F8"));
    }

    private VBox content() {
        Label heading = new Label("Driver Earnings");
        heading.setFont(Font.font("System", FontWeight.BOLD, 28));
        heading.setTextFill(Color.web("#111111"));
        Label subtitle = new Label("Track your verified earnings and payment history");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: " + MUTED + ";");
        Button refresh = new Button("Refresh");
        refresh.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        refresh.setOnAction(event -> loadEarnings());
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(12, new VBox(5, heading, subtitle), spacer, refresh);
        header.setAlignment(Pos.CENTER_LEFT);

        HBox cards = new HBox(14,
                summaryCard("TOTAL EARNINGS", totalEarnings, "Verified wallet earnings"),
                summaryCard("THIS MONTH", thisMonth, "Verified credits this month"),
                summaryCard("VERIFIED PAYMENTS", verifiedPayments, "Completed credit transactions"),
                summaryCard("WALLET BALANCE", walletBalance, "Current wallet balance"));

        Label chartTitle = sectionTitle("Monthly Earnings");
        chartArea.setMinHeight(230); chartArea.setPrefHeight(250);
        chartArea.setStyle(cardStyle());
        chartArea.getChildren().add(state);

        Label historyTitle = sectionTitle("Payment History");
        configureHistory();
        VBox historyCard = new VBox(10, historyTitle, history);
        historyCard.setPadding(new Insets(16));
        historyCard.setStyle(cardStyle());
        VBox.setVgrow(historyCard, Priority.ALWAYS);
        VBox page = new VBox(18, header, cards, chartTitle, chartArea, historyCard);
        page.setPadding(new Insets(18, 22, 20, 22));
        VBox.setVgrow(history, Priority.ALWAYS);
        return page;
    }

    private void configureHistory() {
        history.setPlaceholder(new Label("No verified payment transactions yet"));
        history.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        history.getColumns().setAll(
                column("Trip ID", "tripId", 135), column("Load ID", "loadId", 135),
                column("Amount", "amountText", 110), column("Status", "status", 105),
                column("Date", "dateDisplay", 175), column("Description", "description", 260));
        history.setPrefHeight(210);
    }

    private TableColumn<EarningsTransaction, String> column(String name, String property, int width) {
        TableColumn<EarningsTransaction, String> column = new TableColumn<>(name);
        column.setCellValueFactory(cell -> {
            EarningsTransaction transaction = cell.getValue();
            String value = switch (property) {
                case "amountText" -> currency(transaction.amount());
                case "tripId" -> transaction.tripId(); case "loadId" -> transaction.loadId();
                case "status" -> transaction.status(); case "dateDisplay" -> transaction.dateDisplay();
                default -> transaction.description();
            };
            return new javafx.beans.property.SimpleStringProperty(value);
        });
        column.setPrefWidth(width);
        return column;
    }

    private void loadEarnings() {
        DriverModel driver = CurrentDriver.getInstance().getDriver();
        if (driver == null || driver.getEmail() == null || driver.getEmail().isBlank()) {
            showUnavailable("Driver information unavailable");
            return;
        }
        state.setText("Loading earnings...");
        chartArea.getChildren().setAll(state);
        Task<DriverEarningsData> task = new Task<>() {
            @Override protected DriverEarningsData call() throws Exception {
                return earningsDAO.loadVerifiedEarnings(driver.getEmail().trim());
            }
        };
        task.setOnSucceeded(event -> showData(task.getValue()));
        task.setOnFailed(event -> {
            System.err.println("Unable to load earnings: " + task.getException());
            showUnavailable("Unable to load earnings. Please try again.");
        });
        Thread thread = new Thread(task, "driver-earnings-loader");
        thread.setDaemon(true); thread.start();
    }

    private void showData(DriverEarningsData data) {
        totalEarnings.setText(currency(data.totalEarnings()));
        thisMonth.setText(currency(data.thisMonthEarnings()));
        verifiedPayments.setText(String.valueOf(data.transactions().size()));
        walletBalance.setText(currency(data.walletBalance()));
        history.getItems().setAll(data.transactions());
        rebuildChart(data);
    }

    private void rebuildChart(DriverEarningsData data) {
        if (data.transactions().isEmpty()) {
            state.setText("No earnings data available"); chartArea.getChildren().setAll(state); return;
        }
        Map<YearMonth, Double> monthly = new LinkedHashMap<>();
        data.transactions().stream().filter(item -> item.timestamp() != null)
                .sorted(java.util.Comparator.comparing(EarningsTransaction::timestamp))
                .forEach(item -> monthly.merge(YearMonth.from(item.timestamp()), item.amount(), Double::sum));
        if (monthly.isEmpty()) { state.setText("No earnings data available"); chartArea.getChildren().setAll(state); return; }
        CategoryAxis xAxis = new CategoryAxis(); xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis(); yAxis.setLabel("Verified earnings (\u20B9)");
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false); chart.setAnimated(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        monthly.forEach((month, amount) -> series.getData().add(new XYChart.Data<>(month.format(DateTimeFormatter.ofPattern("MMM yyyy")), amount)));
        chart.getData().setAll(series); chartArea.getChildren().setAll(chart);
    }

    private void showUnavailable(String message) {
        totalEarnings.setText("—"); thisMonth.setText("—"); verifiedPayments.setText("—"); walletBalance.setText("—");
        history.getItems().clear(); state.setText(message); chartArea.getChildren().setAll(state);
    }

    private VBox summaryCard(String title, Label value, String description) {
        Label titleLabel = new Label(title); titleLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + MUTED + ";");
        Label descriptionLabel = new Label(description); descriptionLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + MUTED + ";");
        VBox card = new VBox(8, titleLabel, value, descriptionLabel); card.setPadding(new Insets(16)); card.setStyle(cardStyle());
        card.setMinWidth(0); HBox.setHgrow(card, Priority.ALWAYS); return card;
    }
    private Label valueLabel() { Label label = new Label("—"); label.setStyle("-fx-font-size: 23px; -fx-font-weight: bold; -fx-text-fill: " + GREEN + ";"); return label; }
    private Label sectionTitle(String text) { Label label = new Label(text); label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #182219;"); return label; }
    private String currency(double amount) { return "\u20B9" + String.format("%,.2f", amount); }
    private String cardStyle() { return "-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #E0E7E2; -fx-border-radius: 14;"; }
}

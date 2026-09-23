package com.super_x.view.DriverView;

import com.super_x.ai.GrokAIService;
import com.super_x.ai.GroqMessage;
import com.super_x.ai.VehicleHealthInsightService;
import com.super_x.config.FirebaseConfig;
import com.super_x.dao.driverdao.VehicleDAO;
import com.super_x.model.drivermodel.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.Instant;
import java.util.List;

/**
 * Health review that supplements, but never replaces, the registered vehicle
 * data.
 */
public class VehicleHealth {
    private final VehicleDAO dao = new VehicleDAO(FirebaseConfig.getFireStore());
    private VehicleModel vehicle;
    private VehicleHealthProfile health;
    private TextField odometer, lastService, serviceMileage, puc;
    private ComboBox<String> engine, brakes, tyres, battery, fluids;
    private TextArea warnings, symptoms, output, question;
    private Label score, urgency, componentStatus;

    public Scene getVehicleHealthScene() {
        load();
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#F4F8F6;");
        root.setLeft(DriverNavigation.createSidebar("AI Vehicle Health"));
        VBox page = new VBox(16);
        page.setPadding(new Insets(28));
        page.getChildren().addAll(title("AI Vehicle Health & Insights", 28), text(
                "Review existing vehicle data and add only current health information. AI guidance is not a mechanical diagnosis."));
        if (vehicle == null)
            page.getChildren().add(text(
                    "No registered vehicle was found. Register your existing vehicle before requesting health insights."));
        else
            build(page);
        ScrollPane scroll = new ScrollPane(page);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:#F4F8F6;-fx-background-color:#F4F8F6;");
        root.setCenter(scroll);
        return new Scene(root, 1536, 750);
    }

    private void load() {
        try {
            DriverModel d = CurrentDriver.getInstance().getDriver();
            vehicle = d == null ? null : dao.getVehicleByDriverEmail(d.getEmail());
            health = vehicle != null && vehicle.getVehicleHealth() != null ? vehicle.getVehicleHealth()
                    : new VehicleHealthProfile();
        } catch (Exception e) {
            vehicle = null;
            health = new VehicleHealthProfile();
        }
    }

    private void build(VBox page) {
        VBox saved = card();
        saved.getChildren().addAll(title("Existing vehicle data", 18),
                text("Vehicle number: " + value(vehicle.getVehiclePlateNumber()) + "     Make/model: "
                        + value(vehicle.getVehicleName()) + "     Year: "
                        + value(vehicle.getManufacturingYear() > 0 ? "" + vehicle.getManufacturingYear() : null)),
                text("Type: " + value(vehicle.getVehicleType()) + "     Fuel: " + value(vehicle.getFuelType())
                        + "     Insurance: " + (blank(vehicle.getVehicleInsuranceUrl()) ? "uploaded" : "uploaded")
                        + "     RC: "
                        + (blank(vehicle.getRegistrationCertificateUrl()) ? "uploaded" : "uploaded")));
        VBox form = card();
        form.getChildren().add(title("Current maintenance information", 18));
        GridPane g = new GridPane();
        g.setHgap(12);
        g.setVgap(12);
        odometer = field("Current odometer (km)", number(health.getOdometerKm()));
        lastService = field("Last service date (YYYY-MM-DD)", input(health.getLastServiceDate()));
        serviceMileage = field("Last service mileage (km)", number(health.getLastServiceMileageKm()));
        puc = field("PUC expiry (YYYY-MM-DD)", input(health.getPucExpiry()));
        engine = condition(health.getEngineCondition(), "Engine");
        brakes = condition(health.getBrakeCondition(), "Brakes");
        tyres = condition(health.getTyreCondition(), "Tyres");
        battery = condition(health.getBatteryCondition(), "Battery");
        fluids = condition(health.getFluidsCondition(), "Fluids");
        add(g, odometer, 0, 0);
        add(g, lastService, 1, 0);
        add(g, serviceMileage, 2, 0);
        add(g, puc, 3, 0);
        add(g, engine, 0, 1);
        add(g, brakes, 1, 1);
        add(g, tyres, 2, 1);
        add(g, battery, 3, 1);
        add(g, fluids, 0, 2);
        warnings = area("Warning lights (write None if none)", input(health.getWarningLights()));
        symptoms = area("Recent problems / symptoms", input(health.getRecentSymptoms()));
        add(g, warnings, 1, 2);
        add(g, symptoms, 2, 2);
        form.getChildren().add(g);
        HBox buttons = new HBox(12);
        Button save = button("Save health details");
        Button analyze = button("Request AI analysis");
        save.setOnAction(e -> save(false));
        analyze.setOnAction(e -> save(true));
        buttons.getChildren().addAll(save, analyze);
        VBox assessment = card();
        score = title("", 34);
        urgency = text("");
        componentStatus = text("");
        output = new TextArea();
        output.setEditable(false);
        output.setWrapText(true);
        output.setPrefRowCount(8);
        output.setPrefHeight(250);
        assessment.getChildren().addAll(title("Assessment", 18), score, urgency, componentStatus, output);
        VBox chat = card();
        question = new TextArea();
        question.setPromptText("Ask AI about my vehicle, for example: What could cause the warning light I reported?");
        question.setPrefRowCount(2);
        Button ask = button("Ask AI about my vehicle");
        ask.setOnAction(e -> ask(ask));
        chat.getChildren().addAll(title("Ask AI about my vehicle", 18), question, ask);
        page.getChildren().addAll(saved, form, buttons, assessment, chat);
        refresh();
    }

    private void save(boolean analyze) {
        try {
            health.setOdometerKm(numberValue(odometer.getText()));
            health.setLastServiceDate(lastService.getText().trim());
            health.setLastServiceMileageKm(numberValue(serviceMileage.getText()));
            health.setPucExpiry(puc.getText().trim());
            health.setEngineCondition(engine.getValue());
            health.setBrakeCondition(brakes.getValue());
            health.setTyreCondition(tyres.getValue());
            health.setBatteryCondition(battery.getValue());
            health.setFluidsCondition(fluids.getValue());
            health.setWarningLights(warnings.getText().trim());
            health.setRecentSymptoms(symptoms.getText().trim());
            health.setUpdatedAt(Instant.now().toString());
            dao.saveVehicleHealth(vehicle.getDriverEmail(), health);
            vehicle.setVehicleHealth(health);
            refresh();
            if (!analyze)
                output.setText("Health details saved. Existing vehicle registration data was not changed.");
        } catch (Exception e) {
            output.setText(
                    "Unable to save: use non-negative numbers for mileage. Existing vehicle data was not changed.");
        }
    }

    private void refresh() {
        VehicleHealthInsightService.Assessment a = new VehicleHealthInsightService().analyze(vehicle, health);

        score.setText("Health score: " + a.score() + " / 100");

        urgency.setText("Urgency level: " + a.urgency());

        componentStatus.setText(
                "Engine: " + state(health.getEngineCondition()) +
                        "  |  Brakes: " + state(health.getBrakeCondition()) +
                        "  |  Tyres: " + state(health.getTyreCondition()) +
                        "  |  Battery: " + state(health.getBatteryCondition()) +
                        "  |  Fluids: " + state(health.getFluidsCondition()) +
                        "  |  Documents: " +
                        (a.reminders().isEmpty() ? "review" : "reminder"));

        String insights = cleanMarkdown(
                String.join("\n• ", a.insights()));

        String actions = cleanMarkdown(
                a.actions().isEmpty()
                        ? "Continue routine checks."
                        : String.join("\n• ", a.actions()));

        String reminders = cleanMarkdown(
                a.reminders().isEmpty()
                        ? "No reminder generated."
                        : String.join("\n• ", a.reminders()));

        output.setText(
                "Based on the information provided\n\n" +
                        "AI insights\n• " + insights +
                        "\n\nRecommended actions\n• " + actions +
                        "\n\nMaintenance reminders\n• " + reminders +
                        "\n\nPossible causes are not a definitive diagnosis. " +
                        "Consult a qualified mechanic for inspection.");
    }

    private void ask(Button ask) {
        String q = question.getText().trim();
        if (q.isEmpty()) {
            question.setPromptText("Enter a question first.");
            return;
        }
        ask.setDisable(true);
        String context = "Vehicle " + value(vehicle.getVehicleName()) + "; warning lights: "
                + input(health.getWarningLights()) + "; symptoms: " + input(health.getRecentSymptoms()) + ". Question: "
                + q;
        String system = "You are a safety-first vehicle health assistant. "
        + "Answer the driver in Marathi. "
        + "Use simple and easy-to-understand Marathi. "
        + "Begin with 'दिलेल्या माहितीनुसार,' and use 'संभाव्य कारणे' instead of giving a definite diagnosis. "
        + "Never give a definitive mechanical diagnosis. "
        + "For safety-critical concerns, recommend inspection by a qualified mechanic.";
        new GrokAIService().askGroqAsync(List.of(new GroqMessage("system", system), new GroqMessage("user", context)))
                .whenComplete((answer, error) -> Platform.runLater(() -> {
                    ask.setDisable(false);
                    output.setText(
        error == null
                ? cleanMarkdown(answer)
                : "Based on the information provided, AI chat is unavailable. " +
                  "For braking, steering, tyre, engine-warning, smoke, or fuel-leak concerns, " +
                  "stop safely and ask a qualified mechanic to inspect the vehicle."
);
                }));
    }

    private VBox card() {
        VBox b = new VBox(10);
        b.setPadding(new Insets(18));
        b.setStyle(
                "-fx-background-color:#FFFFFF;-fx-background-radius:14;-fx-border-color:#DCE8E1;-fx-border-radius:14;");
        return b;
    }

    private Label title(String s, int size) {
        Label l = new Label(s);
        l.setStyle("-fx-font-size:" + size + "px;-fx-font-weight:bold;-fx-text-fill:#014B3A;");
        return l;
    }

    private Label text(String s) {
        Label l = new Label(s);
        l.setWrapText(true);
        l.setStyle("-fx-font-size:13px;-fx-text-fill:#405149;");
        return l;
    }

    private TextField field(String hint, String value) {
        TextField f = new TextField(value);
        f.setPromptText(hint);
        f.setPrefWidth(180);
        return f;
    }

    private ComboBox<String> condition(String value, String hint) {
        ComboBox<String> c = new ComboBox<>();
        c.getItems().addAll("Not checked", "Good", "Fair", "Poor");
        c.setPromptText(hint + " condition");
        c.setValue(blank(value) ? "Not checked" : value);
        c.setPrefWidth(180);
        return c;
    }

    private TextArea area(String hint, String value) {
        TextArea a = new TextArea(value);
        a.setPromptText(hint);
        a.setPrefRowCount(2);
        a.setPrefWidth(240);
        return a;
    }

    private void add(GridPane g, javafx.scene.Node n, int col, int row) {
        g.add(n, col, row);
        GridPane.setHgrow(n, Priority.ALWAYS);
    }

    private Button button(String s) {
        Button b = new Button(s);
        b.setStyle(
                "-fx-background-color:#014B3A;-fx-text-fill:white;-fx-font-weight:bold;-fx-padding:10 16;-fx-background-radius:8;");
        return b;
    }

    private double numberValue(String s) {
        if (blank(s))
            return 0;
        double d = Double.parseDouble(s.trim());
        if (d < 0)
            throw new IllegalArgumentException();
        return d;
    }

    private String number(double d) {
        return d > 0 ? String.valueOf((long) d) : "";
    }

    private String value(String s) {
        return blank(s) ? "Not available" : s;
    }

    private String input(String s) {
        return s == null ? "" : s;
    }

    private boolean blank(String s) {
        return s == null || s.isBlank();
    }

    private String state(String s) {
        return blank(s) ? "Not checked" : s;
    }

    private String cleanMarkdown(String text) {
        if (text == null)
            return "";
        return text
                .replace("**", "")
                .replace("__", "");
    }
}

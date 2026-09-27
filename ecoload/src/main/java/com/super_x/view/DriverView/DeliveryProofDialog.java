package com.super_x.view.DriverView;

import com.cloudinary.utils.ObjectUtils;
import com.super_x.config.CloudinaryConfig;
import com.super_x.dao.delivery.DeliveryProofDAO;
import com.super_x.model.drivermodel.CurrentDriver;
import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.drivermodel.Trip;
import com.super_x.model.usermodel.Load;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.util.Map;

/** Driver modal which collects and stores a real proof of delivery. */
public final class DeliveryProofDialog {
    private final Trip trip;
    private final Load load;
    private final DeliveryProofDAO proofDAO = new DeliveryProofDAO();
    private final Canvas signatureCanvas = new Canvas(520, 170);
    private final Label photoName = new Label("No delivery photo selected");
    private final Label progress = new Label();
    private File deliveryPhoto;
    private boolean hasSignature;

    public DeliveryProofDialog(Trip trip, Load load) { this.trip = trip; this.load = load; }

    public void show(Stage owner) {
        if (trip == null || load == null) { alert(Alert.AlertType.WARNING, "Delivery Proof", "An active trip and load are required."); return; }
        Stage dialog = new Stage();
        dialog.setTitle("Delivery Proof"); dialog.initOwner(owner); dialog.initModality(Modality.APPLICATION_MODAL); dialog.setResizable(false);
        TextField receiver = new TextField(); receiver.setPromptText("Receiver's full name"); receiver.setMaxWidth(Double.MAX_VALUE);
        Label tripValue = new Label(value(trip.getTripId())); Label loadValue = new Label(value(load.getLoadId()));
        Button selectPhoto = new Button("Upload Delivery Photo");
        selectPhoto.setOnAction(event -> selectPhoto(dialog));
        Button clear = new Button("Clear Signature"); clear.setOnAction(event -> clearSignature());
        Button submit = new Button("Submit Delivery Proof"); submit.setStyle(primaryStyle());
        submit.setOnAction(event -> submit(dialog, receiver, submit));
        configureCanvas();
        VBox root = new VBox(12,
                title("Delivery Proof"), pair("Trip ID", tripValue), pair("Load ID", loadValue),
                label("Receiver Name"), receiver, label("Delivery Photo"), new HBox(10, selectPhoto, photoName),
                label("Receiver Signature"), signatureCanvas, clear, progress, submit);
        root.setPadding(new Insets(22)); root.setPrefWidth(570);
        root.setStyle("-fx-background-color: #F3F8F5;");
        dialog.setScene(new Scene(root)); dialog.showAndWait();
    }

    private void configureCanvas() {
        GraphicsContext graphics = signatureCanvas.getGraphicsContext2D();
        graphics.setFill(Color.WHITE); graphics.fillRect(0, 0, signatureCanvas.getWidth(), signatureCanvas.getHeight());
        graphics.setStroke(Color.web("#014B3A")); graphics.setLineWidth(2.5); graphics.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        signatureCanvas.setStyle("-fx-border-color: #B7C8BC; -fx-border-radius: 8; -fx-background-color: white;");
        signatureCanvas.setOnMousePressed(event -> { graphics.beginPath(); graphics.moveTo(event.getX(), event.getY()); graphics.stroke(); hasSignature = true; });
        signatureCanvas.setOnMouseDragged(event -> { graphics.lineTo(event.getX(), event.getY()); graphics.stroke(); });
    }

    private void clearSignature() { GraphicsContext graphics = signatureCanvas.getGraphicsContext2D(); graphics.setFill(Color.WHITE); graphics.fillRect(0, 0, signatureCanvas.getWidth(), signatureCanvas.getHeight()); hasSignature = false; }
    private void selectPhoto(Stage dialog) {
        FileChooser chooser = new FileChooser(); chooser.setTitle("Select Delivery Photo");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg", "*.webp"));
        File selected = chooser.showOpenDialog(dialog);
        if (selected != null) { deliveryPhoto = selected; photoName.setText(selected.getName()); }
    }

    private void submit(Stage dialog, TextField receiver, Button submit) {
        DriverModel driver = CurrentDriver.getInstance().getDriver();
        if (driver == null || blank(driver.getEmail())) { alert(Alert.AlertType.ERROR, "Delivery Proof", "Driver information unavailable."); return; }
        if (blank(receiver.getText())) { alert(Alert.AlertType.WARNING, "Delivery Proof", "Receiver name is required."); return; }
        if (deliveryPhoto == null) { alert(Alert.AlertType.WARNING, "Delivery Proof", "Delivery photo is required."); return; }
        if (!hasSignature) { alert(Alert.AlertType.WARNING, "Delivery Proof", "Receiver signature is required."); return; }
        // Canvas snapshots must be taken on the JavaFX application thread.
        WritableImage signatureImage = signatureCanvas.snapshot(new SnapshotParameters(), null);
        submit.setDisable(true); progress.setText("Validating trip and uploading proof...");
        Task<Void> task = new Task<>() {
            @Override protected Void call() throws Exception {
                DeliveryProofDAO.DeliveryRelationship relation = proofDAO.validateDriverAssignment(trip.getTripId(), load.getLoadId(), driver.getEmail().trim());
                updateMessage("Uploading delivery photo..."); String photoUrl = upload(deliveryPhoto);
                updateMessage("Uploading receiver signature..."); File signature = exportSignature(signatureImage);
                try { String signatureUrl = upload(signature); updateMessage("Saving delivery proof...");
                    proofDAO.saveProof(trip.getTripId(), load.getLoadId(), driver.getEmail().trim(), relation.userId(), receiver.getText(), photoUrl, signatureUrl);
                } finally { Files.deleteIfExists(signature.toPath()); }
                return null;
            }
        };
        progress.textProperty().bind(task.messageProperty());
        task.setOnSucceeded(event -> { progress.textProperty().unbind(); alert(Alert.AlertType.INFORMATION, "Delivery Proof", "Delivery proof submitted successfully."); dialog.close(); });
        task.setOnFailed(event -> { progress.textProperty().unbind(); submit.setDisable(false); System.err.println("Unable to submit delivery proof: " + task.getException()); progress.setText("Unable to submit proof. Please try again."); });
        Thread worker = new Thread(task, "delivery-proof-submit"); worker.setDaemon(true); worker.start();
    }

    @SuppressWarnings("unchecked") private String upload(File file) throws Exception {
        Map<String, Object> result = CloudinaryConfig.getCloudinary().uploader().upload(file, ObjectUtils.asMap("folder", "ecoload/delivery-proofs", "resource_type", "image"));
        Object url = result.get("secure_url"); if (url == null || url.toString().isBlank()) throw new IllegalStateException("Image upload did not return a secure URL."); return url.toString();
    }
    private File exportSignature(WritableImage image) throws Exception {
        File file = Files.createTempFile("ecoload-signature-", ".png").toFile();
        if (!ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file)) throw new IllegalStateException("Unable to create signature image.");
        return file;
    }
    private HBox pair(String name, Label value) { Label key = label(name + ":"); Region gap = new Region(); HBox.setHgrow(gap, Priority.ALWAYS); HBox row = new HBox(8, key, gap, value); row.setAlignment(Pos.CENTER_LEFT); return row; }
    private Label title(String text) { Label label = new Label(text); label.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #014B3A;"); return label; }
    private Label label(String text) { Label label = new Label(text); label.setStyle("-fx-font-weight: bold; -fx-text-fill: #29352D;"); return label; }
    private String primaryStyle() { return "-fx-background-color: #014B3A; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 10 16 10 16;"; }
    private static String value(String text) { return blank(text) ? "N/A" : text; }
    private static boolean blank(String text) { return text == null || text.isBlank(); }
    private static void alert(Alert.AlertType type, String title, String text) { Alert alert = new Alert(type, text, ButtonType.OK); alert.setTitle(title); alert.setHeaderText(null); alert.showAndWait(); }
}


package com.super_x.view.AdminView;

import com.super_x.NavigationService;
import com.super_x.dao.admindao.DriverDAO;
import com.super_x.model.adminmodel.Driver;
import com.google.cloud.firestore.DocumentSnapshot;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.stage.Window;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PendingDrivers {

    private final DriverDAO driverDAO = new DriverDAO();

    private final VBox driverContainer = new VBox(12);

    private TextField searchField;
    

    // =========================================================
    // PENDING DRIVER COUNT LABEL
    // =========================================================
    private Label pendingLabel;

    private NavigationService navigationService;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public PendingDrivers() {
    }

    // =========================================================
    // NAVIGATION SERVICE
    // =========================================================
    public void setNavigationService(
            NavigationService navigationService) {

        this.navigationService = navigationService;
    }

    // =========================================================
    // GET CONTENT
    // =========================================================
    public VBox getContent() {

        BorderPane root = buildPage();

        VBox wrapper = new VBox(root);

        VBox.setVgrow(
                root,
                Priority.ALWAYS
        );

        return wrapper;
    }

    // =========================================================
    // GET VIEW
    // =========================================================
    public Node getView() {
        return buildPage();
    }

    // =========================================================
    // BUILD PAGE
    // =========================================================
    private BorderPane buildPage() {

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: #f0fdf4;"
        );

        // =====================================================
        // TOP SECTION
        // =====================================================

        VBox topSection = new VBox(15);

        topSection.setPadding(
                new Insets(
                        25,
                        30,
                        20,
                        30
                )
        );

        // =====================================================
        // TITLE
        // =====================================================

        Label title = new Label(
                "Pending Drivers"
        );

        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1f2937;"
        );

        // =====================================================
        // SUBTITLE
        // =====================================================

        Label subtitle = new Label(
                "Review and approve driver registration requests"
        );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #6b7280;"
        );

        // =====================================================
        // SEARCH + PENDING COUNT + REFRESH
        // =====================================================

        HBox searchBox = new HBox(10);

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        // -----------------------------------------------------
        // SEARCH FIELD
        // -----------------------------------------------------

        searchField = new TextField();

        searchField.setPromptText(
                "Search by name, email or phone..."
        );

        searchField.setPrefWidth(350);

        searchField.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #d1d5db;" +
                "-fx-border-radius: 8;" +
                "-fx-padding: 10;"
        );

        searchField.textProperty().addListener(
                (obs, oldValue, newValue) -> {

                    loadDrivers(
                            newValue == null
                                    ? ""
                                    : newValue
                    );
                }
        );

        // =====================================================
        // PENDING COUNT LABEL
        // =====================================================

        pendingLabel = new Label(
                "Pending: 0"
        );

        pendingLabel.setStyle(
                "-fx-background-color: #dcfce7;" +
                "-fx-text-fill: #166534;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;" +
                "-fx-padding: 9 14;"
        );

        // =====================================================
        // REFRESH BUTTON
        // =====================================================

        Button refreshButton = new Button(
                "⟳ Refresh"
        );

        refreshButton.setStyle(
                "-fx-background-color: #16a34a;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;" +
                "-fx-padding: 9 16;"
        );

        refreshButton.setOnAction(
                e -> {

                    searchField.clear();

                    loadDrivers("");
                }
        );

        // =====================================================
        // ADD SEARCH + PENDING + REFRESH
        // =====================================================

        searchBox.getChildren().addAll(
                searchField,
                pendingLabel,
                refreshButton
        );

        // =====================================================
        // ADD TOP SECTION
        // =====================================================

        topSection.getChildren().addAll(
                title,
                subtitle,
                searchBox
        );

        root.setTop(topSection);

        // =====================================================
        // CONTENT
        // =====================================================

        VBox content = new VBox(0);

        content.setPadding(
                new Insets(
                        0,
                        30,
                        30,
                        30
                )
        );

        // =====================================================
        // TABLE HEADER
        // =====================================================

        HBox header = createTableHeader();

        // =====================================================
        // DRIVER CONTAINER
        // =====================================================

        driverContainer.setPadding(
                new Insets(10)
        );

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane = new ScrollPane(
                driverContainer
        );

        scrollPane.setFitToWidth(true);

        scrollPane.setPannable(true);

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;"
        );

        // =====================================================
        // TABLE BOX
        // =====================================================

        VBox tableBox = new VBox(0);

        tableBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;"
        );

        tableBox.getChildren().addAll(
                header,
                scrollPane
        );

        content.getChildren().add(
                tableBox
        );

        VBox.setVgrow(
                tableBox,
                Priority.ALWAYS
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        root.setCenter(content);

        // =====================================================
        // LOAD DATA
        // =====================================================

        loadDrivers("");

        return root;
    }

    // =========================================================
    // TABLE HEADER
    // =========================================================

    private HBox createTableHeader() {

        HBox header = new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setPadding(
                new Insets(
                        14,
                        15,
                        14,
                        15
                )
        );

        header.setStyle(
                "-fx-background-color: #dcfce7;" +
                "-fx-background-radius: 8 8 0 0;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8 8 0 0;"
        );

        header.getChildren().addAll(

                createHeaderLabel(
                        "DRIVER",
                        190
                ),

                createHeaderLabel(
                        "EMAIL",
                        210
                ),

                createHeaderLabel(
                        "PHONE",
                        130
                ),

                createHeaderLabel(
                        "LICENSE",
                        170
                ),

                createHeaderLabel(
                        "DOCUMENTS",
                        170
                ),

                createHeaderLabel(
                        "STATUS",
                        120
                ),

                createHeaderLabel(
                        "ACTION",
                        180
                )
        );

        return header;
    }

    // =========================================================
    // HEADER LABEL
    // =========================================================

    private Label createHeaderLabel(
            String text,
            double width) {

        Label label = new Label(text);

        label.setPrefWidth(width);

        label.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #4b5563;"
        );

        return label;
    }

    // =========================================================
    // LOAD PENDING DRIVERS
    // =========================================================

    private void loadDrivers(
            String searchText) {

        // Clear old rows
        driverContainer
                .getChildren()
                .clear();

        // Get pending drivers
        List<Driver> drivers =
                driverDAO.getPendingDrivers();

        // =====================================================
        // UPDATE PENDING COUNT
        // =====================================================

        int pendingCount = drivers.size();

        pendingLabel.setText(
                "Pending: " + pendingCount
        );

        // =====================================================
        // SEARCH
        // =====================================================

        String search =
                searchText == null
                        ? ""
                        : searchText
                                .trim()
                                .toLowerCase();

        // =====================================================
        // DISPLAY DRIVERS
        // =====================================================

        for (Driver driver : drivers) {

            if (!search.isEmpty()) {

                String name =
                        safe(
                                driver.getUsername()
                        ).toLowerCase();

                String email =
                        safe(
                                driver.getEmail()
                        ).toLowerCase();

                String phone =
                        safe(
                                driver.getPhone()
                        ).toLowerCase();

                if (!name.contains(search)
                        && !email.contains(search)
                        && !phone.contains(search)) {

                    continue;
                }
            }

            driverContainer
                    .getChildren()
                    .add(
                            createDriverRow(driver)
                    );
        }

        // =====================================================
        // EMPTY RESULT
        // =====================================================

        if (driverContainer
                .getChildren()
                .isEmpty()) {

            Label empty = new Label(

                    search.isEmpty()
                            ? "No pending drivers found."
                            : "No drivers match your search."
            );

            empty.setStyle(
                    "-fx-font-size: 16px;" +
                    "-fx-text-fill: #6b7280;" +
                    "-fx-padding: 40;"
            );

            driverContainer
                    .getChildren()
                    .add(empty);
        }
    }

    // =========================================================
    // DRIVER ROW
    // =========================================================

    private HBox createDriverRow(
            Driver driver) {

        HBox row = new HBox();

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPadding(
                new Insets(
                        13,
                        15,
                        13,
                        15
                )
        );

        row.setSpacing(0);

        row.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #e5e7eb;" +
                "-fx-border-width: 0 0 1 0;"
        );

        // =====================================================
        // HOVER EFFECT
        // =====================================================

        row.setOnMouseEntered(e -> {

            row.setStyle(
                    "-fx-background-color: #f0fdf4;" +
                    "-fx-border-color: #86efac;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 6;"
            );
        });

        row.setOnMouseExited(e -> {

            row.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #e5e7eb;" +
                    "-fx-border-width: 0 0 1 0;"
            );
        });

        // =====================================================
        // DRIVER
        // =====================================================

        HBox driverBox = new HBox(10);

        driverBox.setAlignment(
                Pos.CENTER_LEFT
        );

        driverBox.setPrefWidth(190);

        Circle avatar = new Circle(
                20,
                Color.web("#dcfce7")
        );

        Label avatarText = new Label(
                getInitials(
                        driver.getUsername()
                )
        );

        avatarText.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #2563eb;"
        );

        StackPane avatarPane = new StackPane(
                avatar,
                avatarText
        );

        Label name = new Label(
                safe(
                        driver.getUsername()
                )
        );

        name.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #111827;"
        );

        driverBox.getChildren().addAll(
                avatarPane,
                name
        );

        // =====================================================
        // EMAIL
        // =====================================================

        Label email = new Label(
                safe(
                        driver.getEmail()
                )
        );

        email.setPrefWidth(210);

        email.setStyle(
                "-fx-text-fill: #374151;"
        );

        // =====================================================
        // PHONE
        // =====================================================

        Label phone = new Label(
                safe(
                        driver.getPhone()
                )
        );

        phone.setPrefWidth(130);

        phone.setStyle(
                "-fx-text-fill: #374151;"
        );

        // =====================================================
        // LICENSE
        // =====================================================

        Label license = new Label(
                safe(
                        driver.getDrivingLicenseNumber()
                )
        );

        license.setPrefWidth(170);

        license.setStyle(
                "-fx-text-fill: #374151;" +
                "-fx-font-weight: bold;"
        );

        // =====================================================
        // DOCUMENTS
        // =====================================================

        HBox documentsBox =
                createDocumentsCell(driver);

        documentsBox.setPrefWidth(170);

        // =====================================================
        // STATUS
        // =====================================================

        Label status = new Label(
                "PENDING"
        );

        status.setPrefWidth(120);

        status.setStyle(
                "-fx-background-color: #fef3c7;" +
                "-fx-text-fill: #92400e;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 5 10;"
        );

        // =====================================================
        // ACTIONS
        // =====================================================

        HBox actions = new HBox(8);

        actions.setAlignment(
                Pos.CENTER_LEFT
        );

        actions.setPrefWidth(180);

        // =====================================================
        // APPROVE
        // =====================================================

        Button approve = new Button(
                "✓ Approve"
        );

        approve.setStyle(
                "-fx-background-color: #16a34a;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;"
        );

        approve.setOnAction(
                e -> {

                    boolean success =
                            driverDAO.approveDriver(
                                    driver.getId()
                            );

                    if (success) {

                        showAlert(
                                Alert.AlertType.INFORMATION,
                                "Success",
                                "Driver approved successfully."
                        );

                        loadDrivers(
                                searchField.getText()
                        );
                    }
                }
        );

        // =====================================================
        // REJECT
        // =====================================================

        Button reject = new Button(
                "✕ Reject"
        );

        reject.setStyle(
                "-fx-background-color: #dc2626;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;"
        );

        reject.setOnAction(
                e -> {

                    boolean success =
                            driverDAO.rejectDriver(
                                    driver.getId()
                            );

                    if (success) {

                        showAlert(
                                Alert.AlertType.INFORMATION,
                                "Rejected",
                                "Driver rejected successfully."
                        );

                        loadDrivers(
                                searchField.getText()
                        );
                    }
                }
        );

        actions.getChildren().addAll(
                approve,
                reject
        );

        // =====================================================
        // ADD ALL CELLS
        // =====================================================

        row.getChildren().addAll(
                driverBox,
                email,
                phone,
                license,
                documentsBox,
                status,
                actions
        );

        return row;
    }

    // =========================================================
    // DOCUMENTS CELL
    // =========================================================

    private HBox createDocumentsCell(
            Driver driver) {

        HBox box = new HBox(8);

        box.setAlignment(
                Pos.CENTER_LEFT
        );

        // =====================================================
        // LICENSE URL
        // =====================================================

        String licenseUrl = null;

        try {

            licenseUrl =
                    driver.getLicenseurl();

        } catch (Exception e) {

            System.err.println(
                    "Unable to get license URL for driver: "
                            + driver.getEmail()
            );
        }

        // =====================================================
        // VEHICLE DOCUMENTS
        // =====================================================

        String rcUrl = null;
        String insuranceUrl = null;

        try {

            DocumentSnapshot vehicleDocument =
                    driverDAO.getDriverVehicleDocument(
                            driver.getEmail()
                    );

            if (vehicleDocument != null
                    && vehicleDocument.exists()) {

                rcUrl =
                        getStringSafely(
                                vehicleDocument,
                                "registrationCertificateUrl"
                        );

                insuranceUrl =
                        getStringSafely(
                                vehicleDocument,
                                "vehicleInsuranceUrl"
                        );
            }

        } catch (Exception e) {

            System.err.println(
                    "Unable to fetch vehicle documents for: "
                            + driver.getEmail()
            );

            e.printStackTrace();
        }

        // =====================================================
        // COUNT DOCUMENTS
        // =====================================================

        int documentCount = 0;

        if (isValidUrl(licenseUrl)) {
            documentCount++;
        }

        if (isValidUrl(rcUrl)) {
            documentCount++;
        }

        if (isValidUrl(insuranceUrl)) {
            documentCount++;
        }

        // =====================================================
        // DOCUMENT COUNT LABEL
        // =====================================================

        String documentText =
                documentCount
                        + " "
                        + (
                        documentCount == 1
                                ? "Document"
                                : "Documents"
                );

        Label count =
                new Label(documentText);

        // =====================================================
        // DOCUMENT COUNT COLOR
        // =====================================================

        if (documentCount == 3) {

            count.setStyle(
                    "-fx-text-fill: #15803d;" +
                    "-fx-font-size: 12px;" +
                    "-fx-font-weight: bold;"
            );

        } else {

            count.setStyle(
                    "-fx-text-fill: #dc2626;" +
                    "-fx-font-size: 12px;" +
                    "-fx-font-weight: bold;"
            );
        }

        // =====================================================
        // VIEW BUTTON
        // =====================================================

        Button viewButton =
                new Button("👁");

        viewButton.setTooltip(
                new Tooltip(
                        "View Documents"
                )
        );

        viewButton.setStyle(
                "-fx-background-color: #eff6ff;" +
                "-fx-text-fill: #2563eb;" +
                "-fx-font-size: 15px;" +
                "-fx-background-radius: 6;"
        );

        viewButton.setOnAction(e -> showDriverDocuments(driver));

        box.getChildren().addAll(
                count,
                viewButton
        );

        return box;
    }

    // =========================================================
    // SHOW DRIVER DOCUMENTS
    // =========================================================
// =========================================================
// SHOW DRIVER DOCUMENTS POPUP
// =========================================================

private void showDriverDocuments(Driver driver) {

    if (driver == null) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Driver Information");
        alert.setHeaderText(null);
        alert.setContentText("Driver information is not available.");
        alert.showAndWait();
        return;
    }

    Dialog<Void> dialog = new Dialog<>();

    dialog.setTitle("Driver Documents");
    dialog.setResizable(true);

    // -----------------------------
    // Driver information
    // -----------------------------
    VBox mainContent = new VBox(15);
    mainContent.setPadding(new Insets(20));
    mainContent.setStyle("-fx-background-color: #f8fafc;");

    Label title = new Label("Driver Documents");
    title.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #111827;"
    );

    Label driverInfo = new Label(
            safe(driver.getUsername()) +
            "  •  " +
            safe(driver.getEmail())
    );

    driverInfo.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #6b7280;"
    );

    mainContent.getChildren().addAll(
            title,
            driverInfo
    );

    // -----------------------------
    // Driving Licence
    // -----------------------------
    String licenseUrl = null;

    try {
        licenseUrl = driver.getLicenseurl();
    } catch (Exception e) {
        System.err.println("Unable to get license URL.");
    }

    mainContent.getChildren().add(
            createDocumentCard(
                    "📄 Driving Licence",
                    licenseUrl
            )
    );

    // -----------------------------
    // Vehicle Documents
    // -----------------------------
    String rcUrl = null;
    String insuranceUrl = null;

    try {

        DocumentSnapshot vehicleDocument =
                driverDAO.getDriverVehicleDocument(
                        driver.getEmail()
                );

        if (vehicleDocument != null &&
                vehicleDocument.exists()) {

            rcUrl = getStringSafely(
                    vehicleDocument,
                    "registrationCertificateUrl"
            );

            insuranceUrl = getStringSafely(
                    vehicleDocument,
                    "vehicleInsuranceUrl"
            );
        }

    } catch (Exception e) {
        System.err.println(
                "Unable to fetch vehicle documents for: "
                        + driver.getEmail()
        );

        e.printStackTrace();
    }

    // -----------------------------
    // Registration Certificate
    // -----------------------------
    mainContent.getChildren().add(
            createDocumentCard(
                    "🚗 Registration Certificate",
                    rcUrl
            )
    );

    // -----------------------------
    // Vehicle Insurance
    // -----------------------------
    mainContent.getChildren().add(
            createDocumentCard(
                    "🛡 Vehicle Insurance",
                    insuranceUrl
            )
    );

    // -----------------------------
    // ScrollPane
    // -----------------------------
    ScrollPane scrollPane =
            new ScrollPane(mainContent);

    scrollPane.setFitToWidth(true);
    scrollPane.setPannable(true);

    scrollPane.setStyle(
            "-fx-background-color: #f8fafc;" +
            "-fx-border-color: transparent;"
    );

    // -----------------------------
    // Dialog
    // -----------------------------
    DialogPane dialogPane =
            dialog.getDialogPane();

    dialogPane.setContent(scrollPane);

    dialogPane.getButtonTypes().clear();

    ButtonType closeButton =
            new ButtonType(
                    "Close",
                    ButtonBar.ButtonData.CANCEL_CLOSE
            );

    dialogPane.getButtonTypes().add(closeButton);

    dialogPane.setPrefWidth(800);
    dialogPane.setPrefHeight(600);

    dialog.showAndWait();
}
private VBox createDocumentCard(String title, String imageUrl) {

    VBox card = new VBox(12);

    card.setPadding(new Insets(18));

    card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #e5e7eb;" +
            "-fx-border-radius: 10;"
    );

    // -----------------------------
    // Document title
    // -----------------------------
    Label titleLabel = new Label(title);

    titleLabel.setStyle(
            "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #111827;"
    );

    card.getChildren().add(titleLabel);

    // -----------------------------
    // Document not uploaded
    // -----------------------------
    if (!isValidUrl(imageUrl)) {

        Label notUploaded =
                new Label("Document not uploaded");

        notUploaded.setStyle(
                "-fx-text-fill: #dc2626;" +
                "-fx-font-size: 14px;"
        );

        card.getChildren().add(notUploaded);

        return card;
    }

    // -----------------------------
    // Image
    // -----------------------------
    ImageView imageView = new ImageView();

    imageView.setPreserveRatio(true);
    imageView.setFitWidth(700);
    imageView.setFitHeight(380);

    // -----------------------------
    // Image container
    // -----------------------------
    StackPane imageContainer = new StackPane();

    imageContainer.setMinHeight(300);

    imageContainer.setStyle(
            "-fx-background-color: #f3f4f6;" +
            "-fx-background-radius: 8;"
    );

    ProgressIndicator progress =
            new ProgressIndicator();

    imageContainer.getChildren().add(progress);

    card.getChildren().add(imageContainer);

    // -----------------------------
    // Load image
    // -----------------------------
    loadImage(
            imageUrl,
            imageView,
            imageContainer
    );

    // -----------------------------
    // Open document button
    // -----------------------------
    Button openButton =
            new Button("Open Document");

    openButton.setStyle(
            "-fx-background-color: #2563eb;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 8 15;"
    );

    openButton.setOnAction(
            e -> openImagePopup(
                    title,
                    imageUrl
            )
    );

    card.getChildren().add(openButton);

    return card;
}

private void loadImage(
        String imageUrl,
        ImageView imageView,
        StackPane imageContainer) {

    Thread thread = new Thread(() -> {

        HttpURLConnection connection = null;

        try {

            URL url = new URL(imageUrl);

            connection =
                    (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(15000);
            connection.setReadTimeout(30000);
            connection.setRequestMethod("GET");

            connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0"
            );

            connection.setInstanceFollowRedirects(true);

            int responseCode =
                    connection.getResponseCode();

            if (responseCode < 200 ||
                    responseCode >= 300) {

                throw new RuntimeException(
                        "HTTP error " + responseCode
                );
            }

            try (InputStream inputStream =
                         connection.getInputStream()) {

                Image image =
                        new Image(inputStream);

                Platform.runLater(() -> {

                    imageContainer
                            .getChildren()
                            .clear();

                    if (!image.isError()) {

                        imageView.setImage(image);

                        imageContainer
                                .getChildren()
                                .add(imageView);

                    } else {

                        showImageError(
                                imageContainer,
                                "Unable to decode document image"
                        );
                    }
                });
            }

        } catch (Exception e) {

            e.printStackTrace();

            Platform.runLater(() ->
                    showImageError(
                            imageContainer,
                            "Unable to load document"
                    )
            );

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

    });

    thread.setDaemon(true);
    thread.start();
}
private void openImagePopup(
        String title,
        String imageUrl) {

    if (!isValidUrl(imageUrl)) {

        showAlert(
                Alert.AlertType.WARNING,
                "Document Missing",
                "This document has not been uploaded."
        );

        return;
    }

    Dialog<Void> dialog = new Dialog<>();

    dialog.setTitle(title);
    dialog.setResizable(true);

    // -----------------------------
    // Image View
    // -----------------------------
    ImageView imageView = new ImageView();

    imageView.setPreserveRatio(true);
    imageView.setFitWidth(900);
    imageView.setFitHeight(650);

    // -----------------------------
    // Image Pane
    // -----------------------------
    StackPane imagePane = new StackPane();

    imagePane.setStyle(
            "-fx-background-color: #111827;"
    );

    ProgressIndicator progress =
            new ProgressIndicator();

    imagePane.getChildren().add(progress);

    // Load image
    loadImage(
            imageUrl,
            imageView,
            imagePane
    );

    // -----------------------------
    // Close Button
    // -----------------------------
    Button closeButton = new Button("Close");

closeButton.setStyle(
        "-fx-background-color: #374151;" +
        "-fx-text-fill: white;" +
        "-fx-font-weight: bold;" +
        "-fx-background-radius: 7;" +
        "-fx-padding: 9 18;"
);

closeButton.setOnAction(e -> {
    dialog.setResult(null);
    dialog.close();
});
    HBox bottom =
            new HBox(closeButton);

    bottom.setAlignment(
            Pos.CENTER_RIGHT
    );

    // -----------------------------
    // Content
    // -----------------------------
    VBox content =
            new VBox(
                    10,
                    imagePane,
                    bottom
            );

    content.setPadding(
            new Insets(15)
    );

    content.setStyle(
            "-fx-background-color: #111827;"
    );

    VBox.setVgrow(
            imagePane,
            Priority.ALWAYS
    );

    // -----------------------------
    // Dialog Pane
    // -----------------------------
    DialogPane dialogPane =
            dialog.getDialogPane();

    dialogPane.setContent(content);

    dialogPane.getButtonTypes().clear();
    dialogPane.setOnKeyPressed(event -> {
    if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
        dialog.close();
    }
});

    dialogPane.setPrefWidth(700);
    dialogPane.setPrefHeight(600);

    dialog.showAndWait();
}
private void showImageError(
        StackPane container,
        String message) {

    container.getChildren().clear();

    Label error = new Label(message);

    error.setStyle(
            "-fx-text-fill: #dc2626;" +
            "-fx-font-size: 14px;"
    );

    container.getChildren().add(error);
}
private String getStringSafely(
        DocumentSnapshot document,
        String field) {

    try {

        Object value = document.get(field);

        if (value == null) {
            return null;
        }

        String result =
                String.valueOf(value).trim();

        return result.isEmpty()
                ? null
                : result;

    } catch (Exception e) {

        System.err.println(
                "Unable to read field: " + field
        );

        return null;
    }
}
private boolean isValidUrl(String url) {

    if (url == null) {
        return false;
    }

    String value = url.trim();

    if (value.isEmpty()) {
        return false;
    }

    if (value.equalsIgnoreCase("null")
            || value.equalsIgnoreCase("undefined")
            || value.equalsIgnoreCase("-")) {

        return false;
    }

    return value.startsWith("http://")
            || value.startsWith("https://");
}
private String safe(String value) {

    if (value == null
            || value.trim().isEmpty()) {

        return "-";
    }

    return value;
}
// =========================================================
// GET INITIALS
// =========================================================
private String getInitials(String name) {
    if (name == null || name.trim().isEmpty()) {
        return "?";
    }

    String[] parts = name.trim().split("\\s+");

    if (parts.length == 1) {
        return parts[0].substring(0, 1).toUpperCase();
    }

    return (
            parts[0].substring(0, 1)
            + parts[parts.length - 1].substring(0, 1)
    ).toUpperCase();
}
    // =========================================================
    // ALERT
    // =========================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}
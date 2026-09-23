// package com.super_x.view.DriverView;

// import javafx.application.Platform;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.ProgressIndicator;
// import javafx.scene.control.ScrollPane;
// import javafx.scene.layout.BorderPane;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.Priority;
// import javafx.scene.layout.Region;
// import javafx.scene.layout.VBox;
// import javafx.stage.Modality;
// import javafx.stage.Stage;

// import java.awt.Desktop;
// import java.net.URI;
// import java.net.URLEncoder;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.nio.charset.StandardCharsets;
// import java.time.Duration;
// import java.util.Locale;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;

// public class MapView {

//     // =========================================================
//     // OPENSTREETMAP TEST PLACES
//     // =========================================================


//     // =========================================================
//     // OPENSTREETMAP NOMINATIM
//     // =========================================================

//     private static final String NOMINATIM_URL =
//             "https://nominatim.openstreetmap.org/search";


//     // =========================================================
//     // OSRM ROUTING
//     // =========================================================

//     private static final String OSRM_URL =
//             "https://router.project-osrm.org/route/v1/driving";


//     // =========================================================
//     // HTTP CLIENT
//     // =========================================================

//     private final HttpClient httpClient =
//             HttpClient.newBuilder()
//                     .connectTimeout(Duration.ofSeconds(20))
//                     .build();


//     // =========================================================
//     // SHOW MAP
//     // =========================================================
// public void show(
//         Stage owner,
//         String pickupPlace,
//         String destinationPlace) {

//         Stage mapStage = new Stage();

//         mapStage.setTitle("EcoLoad - OpenStreetMap Route");


//         if (owner != null) {

//             mapStage.initOwner(owner);

//             mapStage.initModality(
//                     Modality.WINDOW_MODAL
//             );
//         }


//         // =====================================================
//         // BACK BUTTON
//         // =====================================================

//         Button backButton =
//                 new Button("← Back");

//         backButton.setStyle(
//                 "-fx-background-color: #0B6B2A;" +
//                 "-fx-text-fill: white;" +
//                 "-fx-font-size: 14px;" +
//                 "-fx-font-weight: bold;" +
//                 "-fx-padding: 9 18 9 18;" +
//                 "-fx-background-radius: 8;" +
//                 "-fx-cursor: hand;"
//         );

//         backButton.setOnAction(
//                 event -> mapStage.close()
//         );


//         // =====================================================
//         // TITLE
//         // =====================================================

//         Label title =
//                 new Label("EcoLoad Trip Route");

//         title.setStyle(
//                 "-fx-font-size: 20px;" +
//                 "-fx-font-weight: bold;" +
//                 "-fx-text-fill: #17233C;"
//         );


//         // =====================================================
//         // SPACER
//         // =====================================================

//         Region spacer =
//                 new Region();

//         HBox.setHgrow(
//                 spacer,
//                 Priority.ALWAYS
//         );


//         // =====================================================
//         // HEADER
//         // =====================================================

//         HBox header =
//                 new HBox(
//                         15,
//                         backButton,
//                         title,
//                         spacer
//                 );

//         header.setAlignment(
//                 Pos.CENTER_LEFT
//         );

//         header.setPadding(
//                 new Insets(
//                         12,
//                         15,
//                         12,
//                         15
//                 )
//         );

//         header.setStyle(
//                 "-fx-background-color: white;" +
//                 "-fx-border-color: #E5E5E5;" +
//                 "-fx-border-width: 0 0 1 0;"
//         );


//         // =====================================================
//         // LOADING INDICATOR
//         // =====================================================

//         ProgressIndicator progress =
//                 new ProgressIndicator();

//         progress.setPrefSize(
//                 50,
//                 50
//         );


//         Label loadingLabel =
//                 new Label(
//                         "Finding locations and calculating route..."
//                 );

//         loadingLabel.setStyle(
//                 "-fx-font-size: 15px;" +
//                 "-fx-font-weight: bold;" +
//                 "-fx-text-fill: #0B6B2A;"
//         );


//         VBox loadingBox =
//                 new VBox(
//                         12,
//                         progress,
//                         loadingLabel
//                 );

//         loadingBox.setAlignment(
//                 Pos.CENTER
//         );


//         // =====================================================
//         // MAP / INFORMATION CONTAINER
//         // =====================================================

//         VBox mapContainer =
//                 new VBox();

//         mapContainer.setAlignment(
//                 Pos.CENTER
//         );

//         mapContainer.setStyle(
//                 "-fx-background-color: #F1F3F1;"
//         );

//         mapContainer.getChildren().add(
//                 loadingBox
//         );


//         // =====================================================
//         // DISTANCE
//         // =====================================================

//         Label distanceLabel =
//                 new Label(
//                         "Distance: --"
//                 );

//         distanceLabel.setStyle(
//                 "-fx-font-size: 14px;" +
//                 "-fx-font-weight: bold;" +
//                 "-fx-text-fill: #0B6B2A;"
//         );


//         // =====================================================
//         // DURATION
//         // =====================================================

//         Label durationLabel =
//                 new Label(
//                         "Estimated time: --"
//                 );

//         durationLabel.setStyle(
//                 "-fx-font-size: 14px;" +
//                 "-fx-font-weight: bold;" +
//                 "-fx-text-fill: #0B6B2A;"
//         );


//         // =====================================================
//         // OPEN MAP BUTTON
//         // =====================================================

//         Button openMapButton =
//                 new Button("🌍 Open Route in OpenStreetMap");

//         openMapButton.setDisable(true);

//         openMapButton.setStyle(
//                 "-fx-background-color: #0B6B2A;" +
//                 "-fx-text-fill: white;" +
//                 "-fx-font-size: 14px;" +
//                 "-fx-font-weight: bold;" +
//                 "-fx-padding: 10 18 10 18;" +
//                 "-fx-background-radius: 8;" +
//                 "-fx-cursor: hand;"
//         );


//         // =====================================================
//         // ROUTE INFORMATION
//         // =====================================================

//         HBox routeInfo =
//                 new HBox(
//                         25,
//                         distanceLabel,
//                         durationLabel,
//                         openMapButton
//                 );

//         routeInfo.setAlignment(
//                 Pos.CENTER_LEFT
//         );

//         routeInfo.setPadding(
//                 new Insets(
//                         12,
//                         20,
//                         12,
//                         20
//                 )
//         );

//         routeInfo.setStyle(
//                 "-fx-background-color: white;" +
//                 "-fx-border-color: #E5E5E5;" +
//                 "-fx-border-width: 1 0 0 0;"
//         );


//         // =====================================================
//         // ROOT
//         // =====================================================

//         BorderPane root =
//                 new BorderPane();

//         root.setTop(header);

//         root.setCenter(mapContainer);

//         root.setBottom(routeInfo);


//         // =====================================================
//         // SCENE
//         // =====================================================

//         Scene scene =
//                 new Scene(
//                         root,
//                         600,
//                         500
//                 );

//         mapStage.setScene(scene);

//         mapStage.setWidth(600);

//         mapStage.setHeight(500);


//         // =====================================================
//         // CENTER WINDOW
//         // =====================================================

//         if (owner != null) {

//             mapStage.setX(
//                     owner.getX()
//                             +
//                             (
//                                     owner.getWidth()
//                                             - 600
//                             ) / 2
//             );

//             mapStage.setY(
//                     owner.getY()
//                             +
//                             (
//                                     owner.getHeight()
//                                             - 500
//                             ) / 2
//             );
//         }


//         // =====================================================
//         // SHOW WINDOW
//         // =====================================================

//         mapStage.show();


//         // =====================================================
//         // CALCULATE ROUTE
//         // =====================================================

//         Thread routeThread =
//                 new Thread(
//                         () -> {

//                             try {

//                                 System.out.println(
//                                         "======================================"
//                                 );

//                                 System.out.println(
//                                         "EcoLoad OpenStreetMap"
//                                 );

//                                 System.out.println(
//                                         "======================================"
//                                 );

//                                 System.out.println(
//                                         "Pickup: "
//                                                 +
//                                                 pickupPlace
//                                 );

//                                 System.out.println(
//                                         "Destination: "
//                                                 +
//                                                 destinationPlace
//                                 );


//                                 // =================================
//                                 // STEP 1
//                                 // GEOCODE PICKUP
//                                 // =================================

//                                 System.out.println(
//                                         "Geocoding pickup using Nominatim..."
//                                 );

//                                 Location pickup =
//                                         geocodePlace(
//                                                pickupPlace
//                                         );


//                                 System.out.println(
//                                         "Pickup coordinates: "
//                                                 +
//                                                 pickup.latitude
//                                                 +
//                                                 ", "
//                                                 +
//                                                 pickup.longitude
//                                 );


//                                 // =================================
//                                 // STEP 2
//                                 // GEOCODE DESTINATION
//                                 // =================================

//                                 System.out.println(
//                                         "Geocoding destination using Nominatim..."
//                                 );

//                                 Location destination =
//                                         geocodePlace(
//                                                 destinationPlace
//                                         );


//                                 System.out.println(
//                                         "Destination coordinates: "
//                                                 +
//                                                 destination.latitude
//                                                 +
//                                                 ", "
//                                                 +
//                                                 destination.longitude
//                                 );


//                                 // =================================
//                                 // STEP 3
//                                 // OSRM ROUTING
//                                 // =================================

//                                 System.out.println(
//                                         "Calculating route using OSRM..."
//                                 );

//                                 RouteResult result =
//                                         calculateRoute(
//                                                 pickup.latitude,
//                                                 pickup.longitude,
//                                                 destination.latitude,
//                                                 destination.longitude
//                                         );


//                                 // =================================
//                                 // CREATE OSM URL
//                                 // =================================

//                                 String osmUrl =
//                                         createOpenStreetMapUrl(
//                                                 pickup.latitude,
//                                                 pickup.longitude,
//                                                 destination.latitude,
//                                                 destination.longitude
//                                         );


//                                 System.out.println(
//                                         "OpenStreetMap URL:"
//                                 );

//                                 System.out.println(
//                                         osmUrl
//                                 );


//                                 // =================================
//                                 // UPDATE UI
//                                 // =================================

//                                 Platform.runLater(
//                                         () -> {

//                                             distanceLabel.setText(
//                                                     "Distance: "
//                                                             +
//                                                             formatDistance(
//                                                                     result.distanceMeters
//                                                             )
//                                             );


//                                             durationLabel.setText(
//                                                     "Estimated time: "
//                                                             +
//                                                             formatDuration(
//                                                                     result.durationSeconds
//                                                             )
//                                             );


//                                             mapContainer
//                                                     .getChildren()
//                                                     .clear();


//                                             Label successTitle =
//                                                     new Label(
//                                                             "Route calculated successfully"
//                                                     );

//                                             successTitle.setStyle(
//                                                     "-fx-font-size: 22px;" +
//                                                     "-fx-font-weight: bold;" +
//                                                     "-fx-text-fill: #0B6B2A;"
//                                             );


//                                             Label routeText =
//                                                     new Label(
//                                                              pickupPlace
                
//                                                                     +
//                                                                     "\n\n↓\n\n"
//                                                                     +
//                                                                    destinationPlace
//                                                     );

//                                             routeText.setWrapText(true);

//                                             routeText.setMaxWidth(750);

//                                             routeText.setStyle(
//                                                     "-fx-font-size: 16px;" +
//                                                     "-fx-text-fill: #444444;" +
//                                                     "-fx-alignment: center;"
//                                             );


//                                             Label mapText =
//                                                     new Label(
//                                                             "The route will open in your default browser using OpenStreetMap."
//                                                     );

//                                             mapText.setStyle(
//                                                     "-fx-font-size: 14px;" +
//                                                     "-fx-text-fill: #666666;"
//                                             );


//                                             VBox successBox =
//                                                     new VBox(
//                                                             18,
//                                                             successTitle,
//                                                             routeText,
//                                                             mapText
//                                                     );

//                                             successBox.setAlignment(
//                                                     Pos.CENTER
//                                             );

//                                             successBox.setPadding(
//                                                     new Insets(40)
//                                             );


//                                             mapContainer
//                                                     .getChildren()
//                                                     .add(successBox);


//                                             openMapButton.setDisable(
//                                                     false
//                                             );


//                                             openMapButton.setOnAction(
//                                                     event ->
//                                                             openInBrowser(
//                                                                     osmUrl
//                                                             )
//                                             );
//                                         }
//                                 );


//                             } catch (Exception ex) {

//                                 ex.printStackTrace();

//                                 Platform.runLater(
//                                         () ->
//                                                 showError(
//                                                         ex.getMessage(),
//                                                         mapContainer,
//                                                         distanceLabel,
//                                                         durationLabel
//                                                 )
//                                 );
//                             }

//                         }
//                 );


//         routeThread.setDaemon(true);

//         routeThread.start();
//     }


//     // =========================================================
//     // BACKWARD COMPATIBILITY METHOD
//     // =========================================================

   


//     // =========================================================
//     // NOMINATIM GEOCODING
//     // =========================================================

//     private Location geocodePlace(
//             String placeName)
//             throws Exception {


//         if (placeName == null
//                 || placeName.isBlank()) {

//             throw new Exception(
//                     "Place name cannot be empty."
//             );
//         }
        

//         if (placeName.equalsIgnoreCase("malegon")) {
//     placeName = "Malegaon, Nashik, Maharashtra, India";
// }

// String encodedPlace =
//         URLEncoder.encode(
//                 placeName,
//                 StandardCharsets.UTF_8
//         );

//         String url =
//                 NOMINATIM_URL
//                         +
//                         "?q="
//                         +
//                         encodedPlace
//                         +
//                         "&format=json"
//                         +
//                         "&limit=1";


//         HttpRequest request =
//                 HttpRequest.newBuilder()
//                         .uri(
//                                 URI.create(url)
//                         )
//                         .timeout(
//                                 Duration.ofSeconds(20)
//                         )
//                         .header(
//                                 "User-Agent",
//                                 "EcoLoad-AI/1.0"
//                         )
//                         .GET()
//                         .build();


//         HttpResponse<String> response =
//                 httpClient.send(
//                         request,
//                         HttpResponse.BodyHandlers.ofString()
//                 );


//         System.out.println(
//                 "Nominatim status: "
//                         +
//                         response.statusCode()
//         );


//         if (response.statusCode() != 200) {

//             throw new Exception(
//                     "OpenStreetMap Nominatim error: "
//                             +
//                             response.statusCode()
//                             +
//                             "\n"
//                             +
//                             response.body()
//             );
//         }


//         String json =
//                 response.body();


//         if (json == null
//                 || json.equals("[]")
//                 || json.isBlank()) {

//             throw new Exception(
//                     "No location found for:\n"
//                             +
//                             placeName
//             );
//         }


//         String latitudeString =
//                 extractString(
//                         json,
//                         "\"lat\"\\s*:\\s*\"(-?[0-9.]+)\""
//                 );


//         String longitudeString =
//                 extractString(
//                         json,
//                         "\"lon\"\\s*:\\s*\"(-?[0-9.]+)\""
//                 );


//         if (latitudeString == null
//                 || longitudeString == null) {

//             throw new Exception(
//                     "Unable to read coordinates for:\n"
//                             +
//                             placeName
//             );
//         }


//         double latitude =
//                 Double.parseDouble(
//                         latitudeString
//                 );


//         double longitude =
//                 Double.parseDouble(
//                         longitudeString
//                 );


//         return new Location(
//                 latitude,
//                 longitude
//         );
//     }


//     // =========================================================
//     // OSRM ROUTING
//     // =========================================================

//     private RouteResult calculateRoute(
//             double pickupLat,
//             double pickupLng,
//             double destinationLat,
//             double destinationLng)
//             throws Exception {


//         String url =
//                 OSRM_URL
//                         +
//                         "/"
//                         +
//                         pickupLng
//                         +
//                         ","
//                         +
//                         pickupLat
//                         +
//                         ";"
//                         +
//                         destinationLng
//                         +
//                         ","
//                         +
//                         destinationLat
//                         +
//                         "?overview=false";


//         HttpRequest request =
//                 HttpRequest.newBuilder()
//                         .uri(
//                                 URI.create(url)
//                         )
//                         .timeout(
//                                 Duration.ofSeconds(30)
//                         )
//                         .header(
//                                 "User-Agent",
//                                 "EcoLoad-AI/1.0"
//                         )
//                         .GET()
//                         .build();


//         HttpResponse<String> response =
//                 httpClient.send(
//                         request,
//                         HttpResponse.BodyHandlers.ofString()
//                 );


//         System.out.println(
//                 "OSRM status: "
//                         +
//                         response.statusCode()
//         );


//         if (response.statusCode() != 200) {

//             throw new Exception(
//                     "OSRM routing error "
//                             +
//                             response.statusCode()
//                             +
//                             "\n"
//                             +
//                             response.body()
//             );
//         }


//         String json =
//                 response.body();


//         String routeCode =
//                 extractString(
//                         json,
//                         "\"code\"\\s*:\\s*\"([^\"]+)\""
//                 );


//         if (!"Ok".equals(routeCode)) {

//             throw new Exception(
//                     "OSRM could not calculate the route."
//             );
//         }


//         String distanceString =
//                 extractString(
//                         json,
//                         "\"distance\"\\s*:\\s*([0-9.]+)"
//                 );


//         String durationString =
//                 extractString(
//                         json,
//                         "\"duration\"\\s*:\\s*([0-9.]+)"
//                 );


//         if (distanceString == null
//                 || durationString == null) {

//             throw new Exception(
//                     "OSRM did not return distance or duration."
//             );
//         }


//         double distanceMeters =
//                 Double.parseDouble(
//                         distanceString
//                 );


//         double durationSeconds =
//                 Double.parseDouble(
//                         durationString
//                 );


//         return new RouteResult(
//                 distanceMeters,
//                 durationSeconds
//         );
//     }


//     // =========================================================
//     // CREATE OPENSTREETMAP ROUTE URL
//     // =========================================================

//     private String createOpenStreetMapUrl(
//             double pickupLat,
//             double pickupLng,
//             double destinationLat,
//             double destinationLng) {


//         return String.format(
//                 Locale.US,

//                 "https://www.openstreetmap.org/directions" +
//                         "?engine=fossgis_osrm_car" +
//                         "&route=%.8f,%.8f;%.8f,%.8f",

//                 pickupLat,
//                 pickupLng,

//                 destinationLat,
//                 destinationLng
//         );
//     }


//     // =========================================================
//     // OPEN DEFAULT BROWSER
//     // =========================================================

//     private void openInBrowser(
//             String url) {

//         try {

//             if (!Desktop.isDesktopSupported()) {

//                 throw new Exception(
//                         "Desktop browser is not supported."
//                 );
//             }


//             Desktop desktop =
//                     Desktop.getDesktop();


//             if (!desktop.isSupported(
//                     Desktop.Action.BROWSE)) {

//                 throw new Exception(
//                         "Browser opening is not supported."
//                 );
//             }


//             desktop.browse(
//                     URI.create(url)
//             );


//         } catch (Exception ex) {

//             ex.printStackTrace();

//             System.out.println(
//                     "Unable to open browser:"
//             );

//             System.out.println(
//                     url
//             );
//         }
//     }


//     // =========================================================
//     // REGEX JSON VALUE EXTRACTION
//     // =========================================================

//     private String extractString(
//             String json,
//             String regex) {

//         if (json == null) {

//             return null;
//         }


//         Pattern pattern =
//                 Pattern.compile(
//                         regex
//                 );


//         Matcher matcher =
//                 pattern.matcher(json);


//         if (matcher.find()) {

//             return matcher.group(1);
//         }


//         return null;
//     }


//     // =========================================================
//     // FORMAT DISTANCE
//     // =========================================================

//     private String formatDistance(
//             double meters) {

//         if (meters < 1000) {

//             return String.format(
//                     Locale.US,
//                     "%.0f m",
//                     meters
//             );
//         }


//         return String.format(
//                 Locale.US,
//                 "%.1f km",
//                 meters / 1000.0
//         );
//     }


//     // =========================================================
//     // FORMAT DURATION
//     // =========================================================

//     private String formatDuration(
//             double seconds) {

//         long totalSeconds =
//                 Math.round(seconds);


//         long hours =
//                 totalSeconds / 3600;


//         long minutes =
//                 (
//                         totalSeconds % 3600
//                 ) / 60;


//         if (hours > 0) {

//             return hours
//                     + " hr "
//                     + minutes
//                     + " min";
//         }


//         return minutes
//                 + " min";
//     }


//     // =========================================================
//     // SHOW ERROR
//     // =========================================================

//     private void showError(
//             String message,
//             VBox mapContainer,
//             Label distanceLabel,
//             Label durationLabel) {


//         if (message == null
//                 || message.isBlank()) {

//             message =
//                     "Unable to calculate route.";
//         }


//         Label errorTitle =
//                 new Label(
//                         "Unable to calculate route"
//                 );

//         errorTitle.setStyle(
//                 "-fx-font-size: 18px;" +
//                 "-fx-font-weight: bold;" +
//                 "-fx-text-fill: #D32F2F;"
//         );


//         Label errorMessage =
//                 new Label(
//                         message
//                 );

//         errorMessage.setWrapText(true);

//         errorMessage.setMaxWidth(850);

//         errorMessage.setStyle(
//                 "-fx-font-size: 13px;" +
//                 "-fx-text-fill: #555555;"
//         );


//         VBox errorBox =
//                 new VBox(
//                         10,
//                         errorTitle,
//                         errorMessage
//                 );

//         errorBox.setAlignment(
//                 Pos.CENTER
//         );

//         errorBox.setPadding(
//                 new Insets(30)
//         );


//         ScrollPane scrollPane =
//                 new ScrollPane(
//                         errorBox
//                 );

//         scrollPane.setFitToWidth(true);

//         scrollPane.setFitToHeight(true);

//         scrollPane.setStyle(
//                 "-fx-background-color: transparent;"
//         );


//         mapContainer
//                 .getChildren()
//                 .clear();

//         mapContainer
//                 .getChildren()
//                 .add(scrollPane);


//         if (distanceLabel != null) {

//             distanceLabel.setText(
//                     "Distance: --"
//             );
//         }


//         if (durationLabel != null) {

//             durationLabel.setText(
//                     "Estimated time: --"
//             );
//         }
//     }


//     // =========================================================
//     // LOCATION MODEL
//     // =========================================================

//     private static class Location {

//         private final double latitude;

//         private final double longitude;


//         Location(
//                 double latitude,
//                 double longitude) {

//             this.latitude =
//                     latitude;

//             this.longitude =
//                     longitude;
//         }
//     }


//     // =========================================================
//     // ROUTE RESULT
//     // =========================================================

//     private static class RouteResult {

//         private final double distanceMeters;

//         private final double durationSeconds;


//         RouteResult(
//                 double distanceMeters,
//                 double durationSeconds) {

//             this.distanceMeters =
//                     distanceMeters;

//             this.durationSeconds =
//                     durationSeconds;
//         }
//     }
//     public void show(Stage owner) {

//     show(
//         owner,
//         "Hingoli, Hingoli, Maharashtra, India",
//         "Pune Railway Station, Pune, Maharashtra, India"
//     );
// }
// }

package com.super_x.view.DriverView;

import com.super_x.config.ApiKeyConfig;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MapView {

    // =========================================================
    // GOOGLE MAPS API KEY
    // =========================================================

    private static final String GOOGLE_MAPS_API_KEY =
            ApiKeyConfig.required("GOOGLE_MAPS_API_KEY");


    // =========================================================
    // SHOW MAP
    // =========================================================

    public void show(
            Stage owner,
            String pickupPlace,
            String destinationPlace) {

        // =====================================================
        // VALIDATION
        // =====================================================

        if (pickupPlace == null || pickupPlace.isBlank()) {
            showSimpleError(owner, "Pickup location is empty.");
            return;
        }

        if (destinationPlace == null || destinationPlace.isBlank()) {
            showSimpleError(owner, "Destination location is empty.");
            return;
        }


        // =====================================================
        // CREATE STAGE
        // =====================================================

        Stage mapStage = new Stage();

        mapStage.setTitle("EcoLoad - Trip Route");


        if (owner != null) {

            mapStage.initOwner(owner);

            mapStage.initModality(
                    Modality.WINDOW_MODAL
            );
        }


        // =====================================================
        // BACK BUTTON
        // =====================================================

        Button backButton =
                new Button("← Back");

        backButton.setStyle(
                "-fx-background-color: #0B6B2A;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 9 18 9 18;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        backButton.setOnAction(
                event -> mapStage.close()
        );


        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label("EcoLoad Trip Route");

        title.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #17233C;"
        );


        // =====================================================
        // SPACER
        // =====================================================

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        // =====================================================
        // HEADER
        // =====================================================

        HBox header =
                new HBox(
                        15,
                        backButton,
                        title,
                        spacer
                );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setPadding(
                new Insets(
                        12,
                        15,
                        12,
                        15
                )
        );

        header.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #E5E5E5;" +
                "-fx-border-width: 0 0 1 0;"
        );


        // =====================================================
        // DISTANCE LABEL
        // =====================================================

        Label distanceLabel =
                new Label("Distance: --");

        distanceLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #0B6B2A;"
        );


        // =====================================================
        // DURATION LABEL
        // =====================================================

        Label durationLabel =
                new Label("Estimated time: --");

        durationLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #0B6B2A;"
        );


        // =====================================================
        // PICKUP LABEL
        // =====================================================

        Label pickupLabel =
                new Label(
                        "Pickup: " + pickupPlace
                );

        pickupLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #444444;"
        );


        // =====================================================
        // DESTINATION LABEL
        // =====================================================

        Label destinationLabel =
                new Label(
                        "Destination: " + destinationPlace
                );

        destinationLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #444444;"
        );


        // =====================================================
        // ROUTE INFORMATION
        // =====================================================

        VBox locationBox =
                new VBox(
                        4,
                        pickupLabel,
                        destinationLabel
                );


        Region infoSpacer =
                new Region();

        HBox.setHgrow(
                infoSpacer,
                Priority.ALWAYS
        );


        HBox routeInfo =
                new HBox(
                        20,
                        locationBox,
                        infoSpacer,
                        distanceLabel,
                        durationLabel
                );

        routeInfo.setAlignment(
                Pos.CENTER_LEFT
        );

        routeInfo.setPadding(
                new Insets(
                        10,
                        20,
                        10,
                        20
                )
        );

        routeInfo.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #E5E5E5;" +
                "-fx-border-width: 1 0 0 0;"
        );


        // =====================================================
        // WEBVIEW
        // =====================================================

        // WebView webView =
        //         new WebView();

        // WebEngine webEngine =
        //         webView.getEngine();

        // webView.setContextMenuEnabled(false);

        // webEngine.setJavaScriptEnabled(true);
        WebView webView =
        new WebView();

WebEngine webEngine =
        webView.getEngine();

webView.setContextMenuEnabled(false);

webEngine.setJavaScriptEnabled(true);

webView.setPrefSize(
        1000,
        550
);

webView.setMinSize(
        0,
        0
);

webView.setMaxSize(
        Double.MAX_VALUE,
        Double.MAX_VALUE
);


        // =====================================================
        // LOADING OVERLAY
        // =====================================================

        ProgressIndicator progress =
                new ProgressIndicator();

        progress.setPrefSize(
                50,
                50
        );


        Label loadingLabel =
                new Label(
                        "Loading Google Maps..."
                );

        loadingLabel.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #0B6B2A;"
        );


        VBox loadingBox =
                new VBox(
                        12,
                        progress,
                        loadingLabel
                );

        loadingBox.setAlignment(
                Pos.CENTER
        );


        // =====================================================
        // MAP CONTAINER
        // =====================================================

        VBox mapContainer =
                new VBox();

        mapContainer.setAlignment(
                Pos.CENTER
        );

        mapContainer.getChildren().add(
                loadingBox
        );

        VBox.setVgrow(
                webView,
                Priority.ALWAYS
        );


        // =====================================================
        // ROOT
        // =====================================================

        BorderPane root =
                new BorderPane();

        root.setTop(header);

        VBox.setVgrow(webView, Priority.ALWAYS);

        root.setCenter(webView);

        root.setBottom(routeInfo);


        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        root,
                        1000,
                        700
                );

        mapStage.setScene(scene);

        mapStage.setWidth(1000);

        mapStage.setHeight(700);


        // =====================================================
        // CENTER WINDOW
        // =====================================================

        if (owner != null) {

            mapStage.setX(
                    owner.getX()
                            +
                            (
                                    owner.getWidth()
                                            - 1000
                            ) / 2
            );

            mapStage.setY(
                    owner.getY()
                            +
                            (
                                    owner.getHeight()
                                            - 700
                            ) / 2
            );
        }


        // =====================================================
        // HTML FOR GOOGLE MAPS
        // =====================================================

        String html =
                createGoogleMapsHTML(
                        pickupPlace,
                        destinationPlace
                );


        // =====================================================
        // WEB ENGINE LOAD LISTENER
        // =====================================================

        webEngine
                .getLoadWorker()
                .stateProperty()
                .addListener(
                        (observable,
                         oldState,
                         newState) -> {

                            if (newState ==
                                    Worker.State.SUCCEEDED) {

                                System.out.println(
                                        "Google Maps HTML loaded."
                                );

                                System.out.println(
                                        "Pickup: "
                                                +
                                                pickupPlace
                                );

                                System.out.println(
                                        "Destination: "
                                                +
                                                destinationPlace
                                );
                            }


                            if (newState ==
                                    Worker.State.FAILED) {

                                Platform.runLater(
                                        () -> {

                                            distanceLabel.setText(
                                                    "Distance: --"
                                            );

                                            durationLabel.setText(
                                                    "Estimated time: --"
                                            );
                                        }
                                );
                            }
                        }
                );


        // =====================================================
        // SHOW WINDOW
        // =====================================================

        mapStage.show();


        // =====================================================
        // LOAD GOOGLE MAPS
        // =====================================================

        webEngine.loadContent(
                html
        );
    }


    // =========================================================
    // CREATE GOOGLE MAPS HTML
    // =========================================================

  private String createGoogleMapsHTML(
        String pickupPlace,
        String destinationPlace) {

    String pickup =
            URLEncoder.encode(
                    pickupPlace,
                    StandardCharsets.UTF_8
            );

    String destination =
            URLEncoder.encode(
                    destinationPlace,
                    StandardCharsets.UTF_8
            );

    return """
            <!DOCTYPE html>

            <html>

            <head>

                <meta charset="UTF-8">

                <meta
                    name="viewport"
                    content="width=device-width,
                             initial-scale=1.0">

                <style>

                    html,
                    body {

                        margin: 0;

                        padding: 0;

                        width: 100%%;

                        height: 100%%;

                        overflow: hidden;

                        background: #ffffff;
                    }


                    iframe {

                        position: absolute;

                        top: 0;

                        left: 0;

                        width: 100%%;

                        height: 100%%;

                        border: 0;
                    }

                </style>

            </head>


            <body>


                <iframe

                    src="https://www.google.com/maps/embed/v1/directions?key=%s&origin=%s&destination=%s&mode=driving&units=metric"

                    allowfullscreen

                    loading="eager"

                    referrerpolicy="strict-origin-when-cross-origin">

                </iframe>


            </body>

            </html>
            """.formatted(
                    GOOGLE_MAPS_API_KEY,
                    pickup,
                    destination
            );
}

    // =========================================================
    // ERROR WINDOW
    // =========================================================

    private void showSimpleError(
            Stage owner,
            String message) {


        Stage errorStage =
                new Stage();

        errorStage.setTitle(
                "EcoLoad - Map Error"
        );


        if (owner != null) {

            errorStage.initOwner(owner);

            errorStage.initModality(
                    Modality.WINDOW_MODAL
            );
        }


        Label title =
                new Label(
                        "Map Error"
                );

        title.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #D32F2F;"
        );


        Label messageLabel =
                new Label(
                        message
                );

        messageLabel.setWrapText(true);

        messageLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #444444;"
        );


        Button closeButton =
                new Button(
                        "Close"
                );

        closeButton.setOnAction(
                event ->
                        errorStage.close()
        );


        VBox box =
                new VBox(
                        15,
                        title,
                        messageLabel,
                        closeButton
                );

        box.setAlignment(
                Pos.CENTER
        );

        box.setPadding(
                new Insets(30)
        );


        Scene scene =
                new Scene(
                        box,
                        400,
                        220
                );


        errorStage.setScene(scene);

        errorStage.show();
    }


    // =========================================================
    // DEFAULT SHOW METHOD
    // =========================================================

    public void show(Stage owner) {

        show(
                owner,
                "Hingoli, Hingoli, Maharashtra, India",
                "Pune Railway Station, Pune, Maharashtra, India"
        );
    }
}
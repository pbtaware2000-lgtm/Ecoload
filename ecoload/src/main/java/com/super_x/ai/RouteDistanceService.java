package com.super_x.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Independent, driver-side Google Routes distance lookup. This service never
 * accesses MapView or its WebView/embed configuration.
 */
public class RouteDistanceService {
    private static final String ROUTES_URL = "https://routes.googleapis.com/directions/v2:computeRoutes";
    private static final Pattern DISTANCE_METERS = Pattern.compile("\\\"distanceMeters\\\"\\s*:\\s*([0-9.]+)");
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15)).build();

    /** Returns route kilometres, or null when the route cannot be calculated. */
    public Double calculateDistanceKm(String pickup, String destination) {
        if (isBlank(pickup) || isBlank(destination)) return null;

        String apiKey = configuredRoutesApiKey();
        if (isBlank(apiKey)) {
            System.err.println("AI route distance unavailable: GOOGLE_ROUTES_API_KEY is not configured.");
            return null;
        }

        try {
            String requestBody = "{"
                    + "\"origin\":{\"address\":\"" + escapeJson(pickup.trim()) + "\"},"
                    + "\"destination\":{\"address\":\"" + escapeJson(destination.trim()) + "\"},"
                    + "\"travelMode\":\"DRIVE\",\"units\":\"METRIC\"}";
            HttpRequest request = HttpRequest.newBuilder(URI.create(ROUTES_URL))
                    .timeout(Duration.ofSeconds(25))
                    .header("Content-Type", "application/json")
                    .header("X-Goog-Api-Key", apiKey)
                    .header("X-Goog-FieldMask", "routes.distanceMeters")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println("AI route distance unavailable: Google Routes returned HTTP " + response.statusCode());
                return null;
            }
            Matcher matcher = DISTANCE_METERS.matcher(response.body());
            if (!matcher.find()) return null;
            double kilometres = Double.parseDouble(matcher.group(1)) / 1000.0;
            return kilometres > 0 ? kilometres : null;
        } catch (Exception exception) {
            System.err.println("AI route distance unavailable: " + exception.getMessage());
            return null;
        }
    }

    private String configuredRoutesApiKey() {
        String key = System.getProperty("GOOGLE_ROUTES_API_KEY");
        return isBlank(key) ? System.getenv("GOOGLE_ROUTES_API_KEY") : key.trim();
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

    private boolean isBlank(String value) { return value == null || value.isBlank(); }
}

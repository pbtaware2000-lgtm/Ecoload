package com.super_x.controller.routecontroller;

import com.super_x.model.routemodel.RouteModel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RouteController {

    /*
     * IMPORTANT:
     * येथे तुझी Google Routes API key ठेव.
     *
     * Key source code मध्ये expose करू नकोस.
     * Testing साठी temporary वापरू शकतोस.
     */
    private static final String API_KEY =
            "YOUR_GOOGLE_ROUTES_API_KEY";

    // =========================================================
    // TEST ROUTE
    // =========================================================

    public void testRoute() throws Exception {

        RouteModel route =
                calculateRoute(
                        "Pune, Maharashtra",
                        "Hingoli, Maharashtra"
                );

        System.out.println(
                "Route test completed."
        );
    }

    // =========================================================
    // TEST ROUTE 1
    // =========================================================

    public void testRoute1() {

        try {

            RouteModel route =
                    calculateRoute(
                            "Pune, Maharashtra",
                            "Hingoli, Maharashtra"
                    );

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "ROUTE TEST"
            );

            System.out.println(
                    "Origin: "
                            + route.getOrigin()
            );

            System.out.println(
                    "Destination: "
                            + route.getDestination()
            );

            System.out.println(
                    "Distance: "
                            + route.getDistanceKm()
                            + " km"
            );

            System.out.println(
                    "Duration: "
                            + route.getDuration()
            );

            System.out.println(
                    "================================="
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // CALCULATE ROUTE
    // =========================================================

    public RouteModel calculateRoute(
            String origin,
            String destination
    ) throws Exception {

        // =====================================================
        // GOOGLE ROUTES API URL
        // =====================================================

        String url =
                "https://routes.googleapis.com/"
                        + "directions/v2:computeRoutes";

        // =====================================================
        // REQUEST BODY
        // =====================================================

        String requestBody =
                "{"
                        + "\"origin\":{"
                        + "\"address\":\""
                        + escapeJson(origin)
                        + "\""
                        + "},"

                        + "\"destination\":{"
                        + "\"address\":\""
                        + escapeJson(destination)
                        + "\""
                        + "},"

                        + "\"travelMode\":\"DRIVE\","

                        + "\"routingPreference\":"
                        + "\"TRAFFIC_AWARE\","

                        + "\"computeAlternativeRoutes\":false,"

                        + "\"languageCode\":\"en-US\","

                        + "\"units\":\"METRIC\""

                        + "}";

        // =====================================================
        // HTTP REQUEST
        // =====================================================

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(url)
                        )

                        .header(
                                "Content-Type",
                                "application/json"
                        )

                        .header(
                                "X-Goog-Api-Key",
                                API_KEY
                        )

                        .header(
                                "X-Goog-FieldMask",
                                "routes.distanceMeters,"
                                        + "routes.duration,"
                                        + "routes.polyline.encodedPolyline"
                        )

                        .POST(
                                HttpRequest
                                        .BodyPublishers
                                        .ofString(
                                                requestBody
                                        )
                        )

                        .build();

        // =====================================================
        // HTTP CLIENT
        // =====================================================

        HttpClient client =
                HttpClient.newHttpClient();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse
                                .BodyHandlers
                                .ofString()
                );

        // =====================================================
        // STATUS
        // =====================================================

        System.out.println(
                "Routes API Status: "
                        + response.statusCode()
        );

        // =====================================================
        // ERROR
        // =====================================================

        if (response.statusCode() != 200) {

            throw new Exception(
                    "Routes API Error: "
                            + response.body()
            );
        }

        // =====================================================
        // RESPONSE
        // =====================================================

        String responseBody =
                response.body();

        System.out.println(
                "Routes API Response:"
        );

        System.out.println(
                responseBody
        );

        // =====================================================
        // ROUTE MODEL
        // =====================================================

        RouteModel route =
                new RouteModel();

        route.setOrigin(
                origin
        );

        route.setDestination(
                destination
        );

        // =====================================================
        // DISTANCE
        // =====================================================

        int distanceStart =
                responseBody.indexOf(
                        "\"distanceMeters\":"
                );

        if (distanceStart != -1) {

            int valueStart =
                    distanceStart
                            + "\"distanceMeters\":"
                            .length();

            int valueEnd =
                    responseBody.indexOf(
                            ",",
                            valueStart
                    );

            if (valueEnd == -1) {

                valueEnd =
                        responseBody.indexOf(
                                "}",
                                valueStart
                        );
            }

            String distanceText =
                    responseBody
                            .substring(
                                    valueStart,
                                    valueEnd
                            )
                            .trim();

            try {

                double meters =
                        Double.parseDouble(
                                distanceText
                        );

                double kilometers =
                        meters / 1000.0;

                route.setDistanceKm(
                        kilometers
                );

            } catch (
                    NumberFormatException ex
            ) {

                route.setDistanceKm(
                        0
                );
            }
        }

        // =====================================================
        // DURATION
        // =====================================================

        int durationStart =
                responseBody.indexOf(
                        "\"duration\":"
                );

        if (durationStart != -1) {

            int valueStart =
                    durationStart
                            + "\"duration\":"
                            .length();

            int valueEnd =
                    responseBody.indexOf(
                            ",",
                            valueStart
                    );

            if (valueEnd == -1) {

                valueEnd =
                        responseBody.indexOf(
                                "}",
                                valueStart
                        );
            }

            String durationText =
                    responseBody
                            .substring(
                                    valueStart,
                                    valueEnd
                            )
                            .replace(
                                    "\"",
                                    ""
                            )
                            .replace(
                                    "s",
                                    ""
                            )
                            .trim();

            try {

                double seconds =
                        Double.parseDouble(
                                durationText
                        );

                long totalMinutes =
                        Math.round(
                                seconds / 60.0
                        );

                long hours =
                        totalMinutes / 60;

                long minutes =
                        totalMinutes % 60;

                String formattedDuration;

                if (hours > 0) {

                    formattedDuration =
                            hours
                                    + "h "
                                    + minutes
                                    + "m";

                } else {

                    formattedDuration =
                            minutes
                                    + "m";
                }

                route.setDuration(
                        formattedDuration
                );

            } catch (
                    NumberFormatException ex
            ) {

                route.setDuration(
                        "Unknown"
                );
            }
        }

        // =====================================================
        // ENCODED POLYLINE
        // =====================================================

        int polylineStart =
                responseBody.indexOf(
                        "\"encodedPolyline\":\""
                );

        if (polylineStart != -1) {

            int valueStart =
                    polylineStart
                            + "\"encodedPolyline\":\""
                            .length();

            int valueEnd =
                    responseBody.indexOf(
                            "\"",
                            valueStart
                    );

            if (valueEnd != -1) {

                String polyline =
                        responseBody.substring(
                                valueStart,
                                valueEnd
                        );

                route.setEncodedPolyline(
                        polyline
                );
            }
        }

        // =====================================================
        // CONSOLE
        // =====================================================

        System.out.println(
                "Distance: "
                        + route.getDistanceKm()
                        + " km"
        );

        System.out.println(
                "Duration: "
                        + route.getDuration()
        );

        return route;
    }

    // =========================================================
    // JSON ESCAPE
    // =========================================================

    private String escapeJson(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value
                .replace(
                        "\\",
                        "\\\\"
                )
                .replace(
                        "\"",
                        "\\\""
                )
                .replace(
                        "\n",
                        "\\n"
                )
                .replace(
                        "\r",
                        "\\r"
                );
    }

    // =========================================================
    // MAIN - TEST ONLY
    // =========================================================

    public static void main(
            String[] args
    ) {

        RouteController controller =
                new RouteController();

        controller.testRoute1();
    }
}
package com.super_x.config;

import com.super_x.model.drivermodel.DriverModel;
import com.super_x.model.usermodel.UserModel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class N8nEmailService {

    private static final String N8N_WEBHOOK_URL =
            "N8N Webbok url";

    private final HttpClient client = HttpClient.newHttpClient();

    public void sendWelcomeEmail(UserModel user) {

        String json = String.format("""
                {
                    "username": "%s",
                    "email": "%s",
                    "role": "%s"
                }
                """,
                escapeJson1(user.getUsername()),
                escapeJson1(user.getEmail()),
                escapeJson1(user.getRole())
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(N8N_WEBHOOK_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "n8n status: " + response.statusCode()
            );

            System.out.println(
                    "n8n response: " + response.body()
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to trigger n8n welcome email"
            );

            e.printStackTrace();
        }
    }

    /** Uses the existing EcoLoad mail webhook for a short-lived password recovery OTP. */
    public boolean sendPasswordResetOtp(String email, String otp) {
        String json = String.format("""
                {
                    "type": "PASSWORD_RESET_OTP",
                    "email": "%s",
                    "otp": "%s"
                }
                """, escapeJson1(email), escapeJson1(otp));
        try {
            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder().uri(URI.create(N8N_WEBHOOK_URL))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(json)).build(),
                    HttpResponse.BodyHandlers.ofString());
            return response.statusCode() >= 200 && response.statusCode() < 300;
        } catch (Exception exception) {
            System.err.println("Failed to send password reset OTP");
            return false;
        }
    }

    private String escapeJson1(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    // For Driver email

    public void sendWelcomeEmail(DriverModel driver) {

        String json = String.format("""
                {
                    "username": "%s",
                    "email": "%s",
                    "role": "%s"
                }
                """,
                escapeJson(driver.getUsername()),
                escapeJson(driver.getEmail()),
                escapeJson(driver.getRole())
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(N8N_WEBHOOK_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "n8n status: " + response.statusCode()
            );

            System.out.println(
                    "n8n response: " + response.body()
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to trigger n8n welcome email"
            );

            e.printStackTrace();
        }
    }

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}

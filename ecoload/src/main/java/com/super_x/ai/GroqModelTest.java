package com.super_x.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GroqModelTest {

    public static void main(String[] args) {

        try {

            String apiKey = System.getenv("GROQ_API_KEY");

            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException(
                        "GROQ_API_KEY is not set."
                );
            }

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "https://api.groq.com/openai/v1/models"
                    ))
                    .header(
                            "Authorization",
                            "Bearer " + apiKey
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "HTTP STATUS: " + response.statusCode()
            );

            if (response.statusCode() != 200) {
                System.out.println(response.body());
                return;
            }

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root =
                    mapper.readTree(response.body());

            System.out.println();
            System.out.println("===== AVAILABLE GROQ MODELS =====");

            for (JsonNode model : root.path("data")) {
                System.out.println(
                        model.path("id").asText()
                );
            }

            System.out.println(
                    "================================="
            );

        } catch (Exception e) {

            System.err.println(
                    "Could not retrieve Groq models."
            );

            e.printStackTrace();
        }
    }
}
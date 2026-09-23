package com.super_x.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class GrokAIService {

    private static final String API_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private static final String MODEL =
            "openai/gpt-oss-120b";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GrokAIService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String askGrok(String prompt)
            throws IOException, InterruptedException {
        return askGroq(List.of(new GroqMessage("user", prompt)));
    }

    /**
     * Sends a multi-message Groq conversation without blocking the JavaFX
     * application thread. The API key is always read from GROQ_API_KEY.
     */
    public CompletableFuture<String> askGroqAsync(List<GroqMessage> messages) {
        try {
            HttpRequest request = buildRequest(messages);
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            return parseResponse(response);
                        } catch (IOException exception) {
                            throw new CompletionException(exception);
                        }
                    });
        } catch (Exception exception) {
            return CompletableFuture.failedFuture(exception);
        }
    }

    private String askGroq(List<GroqMessage> messages)
            throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(
                buildRequest(messages), HttpResponse.BodyHandlers.ofString());
        return parseResponse(response);
    }

    private HttpRequest buildRequest(List<GroqMessage> messages) throws IOException {
        if (messages == null || messages.isEmpty()) {
            throw new IllegalArgumentException("At least one Groq message is required.");
        }

        var requestJson = objectMapper.createObjectNode();
        requestJson.put("model", MODEL);
        requestJson.put("temperature", 0.2);

        var requestMessages = objectMapper.createArrayNode();
        for (GroqMessage message : messages) {
            if (message == null || message.role() == null || message.content() == null) {
                continue;
            }
            var requestMessage = objectMapper.createObjectNode();
            requestMessage.put("role", message.role());
            requestMessage.put("content", message.content());
            requestMessages.add(requestMessage);
        }

        if (requestMessages.isEmpty()) {
            throw new IllegalArgumentException("At least one valid Groq message is required.");
        }

        return HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofSeconds(45))
                .header("Authorization", "Bearer " + requireApiKey())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        objectMapper.writeValueAsString(requestJson.set("messages", requestMessages))))
                .build();
    }

    private String parseResponse(HttpResponse<String> response) throws IOException {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Groq API Error: HTTP " + response.statusCode());
        }

        JsonNode choices = objectMapper.readTree(response.body()).path("choices");
        if (!choices.isArray() || choices.isEmpty()) {
            throw new IOException("Groq returned an invalid response.");
        }

        String content = choices.get(0).path("message").path("content").asText();
        if (content == null || content.isBlank()) {
            throw new IOException("Groq returned an empty response.");
        }
        return content.trim();
    }

    private String requireApiKey() {
        String apiKey = System.getenv("GROQ_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GROQ_API_KEY environment variable is not set.");
        }
        return apiKey;
    }
}

package com.super_x.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Coordinates Ecoload-only knowledge, conversation memory, and Groq. */
public final class EcoloadChatService {

    private static final String SYSTEM_PROMPT = """
            You are the AI assistant for the Ecoload application.
            Answer questions only about the Ecoload application and its implemented features.
            Use only the application information provided in the context.
            Never invent features, screens, policies, prices, data, or functionality.
            If information about a feature is unavailable, clearly say that the feature information is not available in the current Ecoload application.
            For unrelated questions, politely explain that you can only help with the Ecoload application.
            Be concise, practical, and distinguish between implemented functionality and unavailable functionality.
            """;

    private final GrokAIService groqAIService;
    private final ChatConversation conversation;
    private final String audience;

    public EcoloadChatService(String audience) {
        this(new GrokAIService(), new ChatConversation(), audience);
    }

    EcoloadChatService(GrokAIService groqAIService,
                       ChatConversation conversation,
                       String audience) {
        this.groqAIService = groqAIService;
        this.conversation = conversation;
        this.audience = audience;
    }

    public CompletableFuture<String> sendMessage(String question) {
        if (question == null || question.isBlank()) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Please enter a question about Ecoload."));
        }

        String trimmedQuestion = question.trim();
        conversation.addUserMessage(trimmedQuestion);

        List<GroqMessage> requestMessages = new ArrayList<>();
        requestMessages.add(new GroqMessage("system", SYSTEM_PROMPT + "\n\n"
                + EcoloadKnowledgeBase.relevantTo(trimmedQuestion, audience)));
        requestMessages.addAll(conversation.recentMessages());

        return groqAIService.askGroqAsync(requestMessages)
                .thenApply(response -> {
                    conversation.addAssistantMessage(response);
                    return response;
                });
    }
}

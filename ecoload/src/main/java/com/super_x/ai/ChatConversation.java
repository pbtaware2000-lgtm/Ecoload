package com.super_x.ai;

import java.util.ArrayList;
import java.util.List;

/** In-memory, bounded conversation context for one chatbot screen session. */
public final class ChatConversation {

    private static final int MAX_MESSAGES = 12;
    private final List<GroqMessage> messages = new ArrayList<>();

    public synchronized void addUserMessage(String content) {
        add("user", content);
    }

    public synchronized void addAssistantMessage(String content) {
        add("assistant", content);
    }

    public synchronized List<GroqMessage> recentMessages() {
        return List.copyOf(messages);
    }

    private void add(String role, String content) {
        messages.add(new GroqMessage(role, content));
        while (messages.size() > MAX_MESSAGES) {
            messages.remove(0);
        }
    }
}

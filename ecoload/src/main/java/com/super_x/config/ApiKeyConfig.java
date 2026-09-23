package com.super_x.config;

public final class ApiKeyConfig {

    private ApiKeyConfig() {
    }

    public static String required(String name) {
        String value = System.getProperty(name);
        if (value == null || value.isBlank()) {
            value = System.getenv(name);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable is not configured: " + name);
        }
        return value.trim();
    }
}

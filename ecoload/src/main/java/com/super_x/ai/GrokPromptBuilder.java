package com.super_x.ai;

public class GrokPromptBuilder {

    public String buildDriverRecommendationPrompt(
            String loadId,
            String cargoType,
            double loadWeight,
            String source,
            String destination,
            String driverData
    ) {

        return """
                You are the AI assistant for EcoLoad,
                an intelligent truck transportation platform.

                Analyze the following load and available driver information.

                LOAD INFORMATION:
                Load ID: %s
                Cargo Type: %s
                Load Weight: %.2f tons
                Source: %s
                Destination: %s

                AVAILABLE DRIVER INFORMATION:
                %s

                TASK:
                Recommend the most suitable driver for this load.

                Consider:
                1. Vehicle capacity
                2. Driver availability
                3. Driver location
                4. Driver rating
                5. Route suitability
                6. Load requirements

                Return:
                - Recommended Driver
                - Reason
                - Short explanation

                Do not invent driver information.
                Use only the information provided above.
                """.formatted(
                loadId,
                cargoType,
                loadWeight,
                source,
                destination,
                driverData
        );
    }
}

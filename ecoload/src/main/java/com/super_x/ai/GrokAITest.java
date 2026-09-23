package com.super_x.ai;

public class GrokAITest {

    public static void main(String[] args) {

        try {

            GrokAIService grokAIService =
                    new GrokAIService();

           String response =
        grokAIService.askGrok(
                """
                You are the AI assistant for EcoLoad,
                an intelligent truck transportation platform.

                Load Information:
                Load ID: L001
                Cargo Type: Fruits
                Load Weight: 8 tons
                Source: Pune
                Destination: Mumbai

                Available Drivers:

                Driver 1:
                Name: Rahul
                Vehicle Capacity: 10 tons
                Current Location: Pune
                Driver Rating: 4.8
                Availability: Available

                Driver 2:
                Name: Amit
                Vehicle Capacity: 7 tons
                Current Location: Pune
                Driver Rating: 4.9
                Availability: Available

                Driver 3:
                Name: Suresh
                Vehicle Capacity: 12 tons
                Current Location: Nashik
                Driver Rating: 4.5
                Availability: Available

                Task:
                Recommend the most suitable driver for this load.

                Consider:
                1. Vehicle capacity
                2. Driver availability
                3. Current location
                4. Driver rating
                5. Load weight

                Give:
                - Recommended Driver
                - Reason
                - Short explanation

                Do not invent information.
                Use only the information provided.
                """
        );

            System.out.println();
            System.out.println("==============================");
            System.out.println("       ECOLOAD AI RESPONSE");
            System.out.println("==============================");
            System.out.println(response);
            System.out.println("==============================");

        } catch (Exception e) {

            System.err.println(
                    "Groq AI connection failed."
            );

            e.printStackTrace();
        }
    }
}
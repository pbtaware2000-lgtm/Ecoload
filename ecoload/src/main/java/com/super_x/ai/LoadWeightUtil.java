package com.super_x.ai;

public class LoadWeightUtil {

    public static double convertToTons(
            double weight,
            String weightUnit) {

        if (weightUnit == null) {
            throw new IllegalArgumentException(
                    "Weight unit cannot be null."
            );
        }

        String unit = weightUnit.trim().toLowerCase();

        switch (unit) {

            case "kg":
            case "kgs":
            case "kilogram":
            case "kilograms":
                return weight / 1000.0;

            case "ton":
            case "tons":
            case "tonne":
            case "tonnes":
                return weight;

            default:
                throw new IllegalArgumentException(
                        "Unsupported weight unit: " + weightUnit
                );
        }
    }
}
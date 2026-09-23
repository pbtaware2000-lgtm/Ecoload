package com.super_x.ai;

/** Immutable, explainable result of the driver-side vehicle/trip analysis. */
public record VehicleTripInsight(
        String loadDisplay,
        String vehicleCapacityDisplay,
        String utilizationDisplay,
        String suitability,
        String distanceDisplay,
        String estimatedFuel,
        String estimatedFuelCost,
        String offerPrice,
        String tripCostAnalysis,
        String estimatedRemainingAmount,
        String vehicleUsageScore,
        String ecoScore,
        String tripRisk,
        String overallScore,
        String vehicleMatch,
        String loadSuitability,
        String tripValue,
        String finalDecision,
        String recommendation,
        boolean suitable) {
}

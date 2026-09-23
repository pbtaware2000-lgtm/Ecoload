package com.super_x.ai;

import com.super_x.model.drivermodel.VehicleModel;
import com.super_x.model.usermodel.Load;
import java.time.Year;
import java.util.Locale;
import java.util.Map;

/** Explainable, deterministic driver-side trip analysis using configurable estimates. */
public class VehicleTripInsightService {
    /* Centralized AI assumptions. Update these when fleet-specific data becomes available. */
    private static final Map<String, Double> ESTIMATED_KM_PER_LITRE = Map.ofEntries(
            Map.entry("diesel:mini truck", 12.0), Map.entry("diesel:pickup truck", 11.0),
            Map.entry("diesel:lcv", 9.0), Map.entry("diesel:medium truck", 6.0),
            Map.entry("diesel:heavy truck", 4.0), Map.entry("diesel:truck", 5.0),
            Map.entry("petrol:mini truck", 10.0), Map.entry("petrol:pickup truck", 9.0),
            Map.entry("petrol:truck", 5.0), Map.entry("cng:mini truck", 14.0),
            Map.entry("cng:pickup truck", 12.0), Map.entry("electric:mini truck", 10.0));
    private static final Map<String, Double> ESTIMATED_FUEL_PRICE = Map.of(
            "diesel", 94.0, "petrol", 104.0, "cng", 80.0, "electric", 8.0);
    private static final double DEFAULT_ESTIMATED_KM_PER_LITRE = 6.0;
    private static final double DEFAULT_ESTIMATED_FUEL_PRICE = 95.0;

    public VehicleTripInsight analyze(Load load, VehicleModel vehicle, Double routeDistanceKm) {
        if (load == null) throw new IllegalArgumentException("Shipment data is unavailable.");
        if (vehicle == null) throw new IllegalArgumentException("Your registered vehicle was not found.");

        double loadTons = toTons(load.getWeight(), load.getWeightUnit());
        double capacity = vehicle.getVehicleCapacity();
        boolean hasCapacity = capacity > 0;
        double utilization = hasCapacity ? loadTons / capacity * 100 : -1;
        int vehicleMatchScore = vehicleMatchScore(vehicle.getVehicleType(), load.getTruckType());
        boolean capacitySuitable = hasCapacity && loadTons >= 0 && loadTons <= capacity;
        boolean typeSuitable = vehicleMatchScore >= 70;
        boolean suitable = capacitySuitable && typeSuitable;
        int loadSuitabilityScore = loadSuitabilityScore(utilization, hasCapacity);
        int usageScore = suitable ? loadSuitabilityScore : 0;
        int ecoScore = suitable ? ecoScoreFor(utilization) : 0;
        String risk = riskFor(vehicle, utilization, hasCapacity, routeDistanceKm);

        Double fuelLitres = estimateFuel(routeDistanceKm, vehicle, utilization);
        Double fuelPricePerLitre = estimatedFuelPrice(vehicle.getFuelType());
        Double fuelCost = fuelLitres == null ? null : fuelLitres * fuelPricePerLitre;
        Double remaining = fuelCost == null ? null : load.getOfferPrice() - fuelCost;
        int tripValueScore = fuelCost == null ? 0 : tripValueScore(remaining, load.getOfferPrice());
        String tripValue = fuelCost == null ? "Route unavailable" : tripValueScore + "% — " + tripValueLabel(tripValueScore) + " (fuel only)";
        Integer overall = overallScore(vehicleMatchScore, loadSuitabilityScore, risk,
                fuelCost == null ? null : tripValueScore);
        String decision = decisionFor(suitable, risk, overall);

        return new VehicleTripInsight(
                format(load.getWeight()) + " " + safe(load.getWeightUnit(), "unit unavailable"),
                hasCapacity ? format(capacity) + " Ton" : "Data unavailable",
                hasCapacity ? format(utilization) + "%" : "Data unavailable",
                suitabilityFor(hasCapacity, typeSuitable, capacitySuitable),
                routeDistanceKm == null ? "Route unavailable" : format(routeDistanceKm) + " KM (route distance)",
                fuelLitres == null ? "Route unavailable" : "~" + format(fuelLitres) + " L (AI estimated)",
                fuelCost == null ? "Route unavailable" : "~₹" + format(fuelCost) + " (AI estimated)",
                "₹" + String.format(Locale.US, "%,.0f", load.getOfferPrice()),
                fuelCost == null ? "Route unavailable" : tripValueLabel(tripValueScore) + " — based on estimated fuel expense only",
                remaining == null ? "Route unavailable" : "₹" + format(remaining) + " (estimated)",
                suitable ? usageScore + "/100 — " + rating(usageScore) : "Data unavailable",
                suitable ? ecoScore + "/100 — " + rating(ecoScore) : "Data unavailable",
                risk,
                overall == null ? "Data limited" : overall + "/100",
                vehicleMatchScore + "%",
                hasCapacity ? loadSuitabilityScore + "%" : "Data unavailable",
                tripValue,
                decision,
                recommendation(suitable, utilization, routeDistanceKm, fuelLitres, fuelCost, decision),
                suitable);
    }

    private Double estimateFuel(Double distance, VehicleModel vehicle, double utilization) {
        if (distance == null || distance <= 0) return null;
        Double mileage = ESTIMATED_KM_PER_LITRE.get(normalize(vehicle.getFuelType()) + ":" + mileageVehicleCategory(vehicle.getVehicleType()));
        if (mileage == null) mileage = DEFAULT_ESTIMATED_KM_PER_LITRE;
        // A fuller load modestly reduces estimated efficiency; this is an AI estimate only.
        double loadAdjustment = utilization > 0 ? 1.0 - Math.min(0.15, utilization * 0.0015) : 1.0;
        return distance / (mileage * loadAdjustment);
    }

    private double estimatedFuelPrice(String fuelType) {
        return ESTIMATED_FUEL_PRICE.getOrDefault(normalize(fuelType), DEFAULT_ESTIMATED_FUEL_PRICE);
    }

    private int vehicleMatchScore(String vehicleType, String requiredType) {
        String vehicle = normalize(vehicleType), required = normalize(requiredType);
        if (vehicle.isEmpty() || required.isEmpty()) return 0;
        if (vehicle.equals(required)) return 100;
        if (vehicle.equals("pickup") && required.equals("pickup truck")) return 100;
        if (vehicle.equals("truck") && (required.contains("truck") || required.equals("lcv") || required.equals("trailer") || required.equals("tanker"))) return 75;
        return 0;
    }

    private int loadSuitabilityScore(double utilization, boolean hasCapacity) {
        if (!hasCapacity || utilization < 0 || utilization > 100) return 0;
        if (utilization >= 70 && utilization <= 90) return 95;
        if (utilization >= 50) return 82;
        if (utilization >= 25) return 65;
        return 45;
    }

    private int ecoScoreFor(double utilization) {
        if (utilization >= 70 && utilization <= 90) return 85;
        if (utilization >= 50) return 72;
        if (utilization >= 25) return 58;
        return 42;
    }

    private int tripValueScore(double remaining, double offer) {
        if (offer <= 0) return 0;
        return Math.max(0, Math.min(100, (int) Math.round(remaining / offer * 100)));
    }

    private String tripValueLabel(int score) {
        if (score >= 70) return "GOOD";
        if (score >= 40) return "MODERATE";
        return "LOW";
    }

    private Integer overallScore(int vehicleMatch, int loadSuitability, String risk, Integer tripValue) {
        int riskScore = "LOW".equals(risk) ? 100 : "MEDIUM".equals(risk) ? 65 : 20;
        if (tripValue == null) return Math.round((vehicleMatch + loadSuitability + riskScore) / 3.0f);
        return Math.round((vehicleMatch + loadSuitability + riskScore + tripValue) / 4.0f);
    }

    private String riskFor(VehicleModel vehicle, double utilization, boolean hasCapacity, Double distance) {
        if (!hasCapacity || utilization > 100) return "HIGH";
        int age = vehicle.getManufacturingYear() > 0 ? Year.now().getValue() - vehicle.getManufacturingYear() : -1;
        if (utilization > 95 || age >= 15 || (distance != null && distance > 1000)) return "HIGH";
        if (utilization > 85 || age >= 10 || (distance != null && distance > 600)) return "MEDIUM";
        return "LOW";
    }

    private String suitabilityFor(boolean hasCapacity, boolean typeSuitable, boolean capacitySuitable) {
        if (!hasCapacity) return "CAPACITY DATA UNAVAILABLE";
        if (!typeSuitable) return "TYPE MISMATCH";
        if (!capacitySuitable) return "CAPACITY EXCEEDED";
        return "GOOD — VEHICLE IS SUITABLE";
    }

    private String decisionFor(boolean suitable, String risk, Integer score) {
        if (!suitable || "HIGH".equals(risk) || score == null || score < 50) return "NOT RECOMMENDED";
        return score >= 75 && "LOW".equals(risk) ? "RECOMMENDED" : "CONSIDER";
    }

    private String recommendation(boolean suitable, double utilization, Double distance, Double fuel, Double fuelCost, String decision) {
        if (!suitable) return "This trip is not recommended because the registered vehicle type or capacity does not meet the shipment requirements.";
        String text = "Your vehicle is suitable and the shipment uses " + format(utilization) + "% of its registered capacity.";
        if (distance != null) text += " The route distance is " + format(distance) + " KM.";
        if (fuel != null) text += " AI estimated fuel is ~" + format(fuel) + " L.";
        if (fuelCost != null) text += " Based on estimated fuel expense only, the trip value is favorable.";
        return text + " Final decision: " + decision + ".";
    }

    private double toTons(double weight, String unit) { try { return unit == null || unit.isBlank() ? weight : LoadWeightUtil.convertToTons(weight, unit); } catch (IllegalArgumentException ignored) { return weight; } }
    private String normalize(String value) { return value == null ? "" : value.trim().toLowerCase(Locale.ROOT); }
    private String mileageVehicleCategory(String vehicleType) {
        String type = normalize(vehicleType);
        if (type.equals("pickup")) return "pickup truck";
        if (type.equals("van")) return "mini truck";
        return type;
    }
    private String safe(String value, String fallback) { return value == null || value.isBlank() ? fallback : value.trim(); }
    private String format(double value) { return String.format(Locale.US, "%.2f", value).replaceAll("\\.00$", ""); }
    private String rating(int score) { return score >= 80 ? "GOOD" : score >= 60 ? "FAIR" : "LOW"; }
}

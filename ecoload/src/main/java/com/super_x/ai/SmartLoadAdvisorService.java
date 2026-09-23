package com.super_x.ai;

import com.super_x.config.FirebaseConfig;
import com.super_x.dao.driverdao.VehicleDAO;
import com.super_x.model.drivermodel.VehicleModel;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Deterministic pre-posting advice. It uses a registered vehicle capacity when
 * available, otherwise a fixed demonstration capacity; it does not call, or
 * claim to call, an external AI model.
 */
public class SmartLoadAdvisorService {

    private static final String CONTAINER_TRUCK = "Container Truck";
    private static final double DEFAULT_CONTAINER_TRUCK_CAPACITY_TONS = 20.0;

    private final VehicleDAO vehicleDAO = new VehicleDAO(FirebaseConfig.getFireStore());

    public Advice analyze(Request request) throws Exception {
        validate(request);
        double weightTons = toTons(request.weight(), request.weightUnit());
        List<VehicleModel> vehicles = vehicleDAO.getAllVehicles().stream()
                .filter(vehicle -> vehicle.getVehicleCapacity() > 0)
                .filter(vehicle -> vehicle.getVehicleCapacity() >= weightTons)
                .toList();

        double recommendedCapacity = vehicles.stream()
                .sorted(Comparator.comparingDouble(VehicleModel::getVehicleCapacity))
                .findFirst()
                .map(VehicleModel::getVehicleCapacity)
                .orElse(DEFAULT_CONTAINER_TRUCK_CAPACITY_TONS);
        double utilization = weightTons * 100.0 / recommendedCapacity;
        String finalRecommendation = "Suitable for Posting";
        String rationale = vehicles.isEmpty()
                ? "The recommendation uses the standard 20-ton Container Truck capacity for this demonstration."
                : "The recommendation uses the smallest suitable registered capacity while standardizing the vehicle category as a Container Truck.";
        return new Advice(CONTAINER_TRUCK, recommendedCapacity, utilization,
                packagingFor(request.loadType()), handlingFor(request.loadType()), pricingFor(request, true),
                finalRecommendation, rationale);
    }

    private void validate(Request request) {
        if (request == null || !hasText(request.pickup()) || !hasText(request.destination()) || !hasText(request.loadType())) {
            throw new IllegalArgumentException("Pickup, destination and load type are required.");
        }
        if (request.weight() <= 0 || !hasText(request.weightUnit())) {
            throw new IllegalArgumentException("Enter a weight greater than zero and select its unit.");
        }
    }

    private double toTons(double weight, String unit) {
        return switch (unit.trim().toLowerCase(Locale.ROOT)) {
            case "ton", "tons", "tonne", "tonnes" -> weight;
            case "quintal", "quintals" -> weight / 10.0;
            case "kg", "kgs", "kilogram", "kilograms" -> weight / 1000.0;
            default -> throw new IllegalArgumentException("Unsupported weight unit: " + unit);
        };
    }

    private List<String> packagingFor(String loadType) {
        return switch (loadType.trim().toLowerCase(Locale.ROOT)) {
            case "perishable" -> List.of("Use insulated packaging where temperature protection is required.", "Protect against moisture and secure the load for transit.");
            case "fragile" -> List.of("Use cushioning and protective wrapping.", "Use strong outer packaging and secure placement.");
            case "electronics" -> List.of("Use cushioning and moisture protection.", "Use anti-static protection where appropriate.");
            case "heavy goods" -> List.of("Use reinforced, heavy-duty packaging.", "Secure the load with suitable restraints.");
            case "furniture" -> List.of("Use protective covers and corner protection.", "Secure items to prevent movement and surface damage.");
            default -> List.of("Use packaging appropriate to the cargo.", "Secure the load to prevent movement during transit.");
        };
    }

    private String handlingFor(String loadType) {
        return switch (loadType.trim().toLowerCase(Locale.ROOT)) {
            case "perishable" -> "Use temperature-sensitive handling where required.";
            case "fragile" -> "Handle carefully; avoid shocks and stacking pressure.";
            case "electronics" -> "Protect from shock and moisture while loading and unloading.";
            case "heavy goods" -> "Distribute weight evenly and use proper securing equipment.";
            case "furniture" -> "Avoid abrasion and secure items to prevent shifting.";
            default -> "Use safe loading, securing and unloading practices.";
        };
    }

    private String pricingFor(Request request, boolean vehicleFound) {
        if (request.offerPrice() != null && request.offerPrice() > 0) {
            return "Your entered offer is ₹" + String.format(Locale.US, "%,.0f", request.offerPrice())
                    + ". Review it against the route, load weight and vehicle requirement.";
        }
        return vehicleFound
                ? "Enter a competitive offer based on load weight, route and the recommended vehicle requirement. Live market pricing is not available."
                : "Set an offer only after a vehicle with sufficient recorded capacity is available.";
    }

    private boolean hasText(String value) { return value != null && !value.isBlank(); }

    public record Request(String pickup, String destination, String loadType, double weight, String weightUnit,
                          String truckType, Double offerPrice) { }

    public record Advice(String vehicleType, double capacityTons, double utilizationPercent, List<String> packaging,
                         String handling, String pricing, String finalRecommendation, String rationale) { }
}

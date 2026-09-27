package com.super_x.ai;

import com.super_x.model.drivermodel.VehicleHealthProfile;
import com.super_x.model.drivermodel.VehicleModel;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Conservative, explainable health assessment; it is not a mechanical diagnosis. */
public final class VehicleHealthInsightService {
    public record Assessment(int score, String urgency, List<String> insights,
                             List<String> actions, List<String> reminders) { }

    public Assessment analyze(VehicleModel vehicle, VehicleHealthProfile health) {
        List<String> insights = new ArrayList<>(), actions = new ArrayList<>(), reminders = new ArrayList<>();
        int score = 100;
        score -= conditionPenalty("Engine", value(health, VehicleHealthProfile::getEngineCondition), insights, actions);
        score -= conditionPenalty("Brakes", value(health, VehicleHealthProfile::getBrakeCondition), insights, actions);
        score -= conditionPenalty("Tyres", value(health, VehicleHealthProfile::getTyreCondition), insights, actions);
        score -= conditionPenalty("Battery", value(health, VehicleHealthProfile::getBatteryCondition), insights, actions);
        score -= conditionPenalty("Fluids", value(health, VehicleHealthProfile::getFluidsCondition), insights, actions);
        if (health == null || blank(health.getWarningLights())) {
            insights.add("Warning lights: no warning-light issue reported.");
        } else if (!health.getWarningLights().equalsIgnoreCase("none")) {
            score -= 18; insights.add("Possible cause: reported warning lights may need a fault-code check.");
            actions.add("Arrange a qualified inspection of the reported warning lights soon.");
        }
        if (health != null && !blank(health.getRecentSymptoms())) {
            score -= 10; insights.add("Possible causes for the reported symptoms should be checked by a qualified mechanic.");
            actions.add("Share the symptom details with a service centre; stop driving if safety or braking is affected.");
        }
        score -= servicePenalty(health, reminders, actions);
        score -= documentPenalty(vehicle, health, reminders, actions);
        if (vehicle != null && vehicle.getManufacturingYear() > 0
                && Year.now().getValue() - vehicle.getManufacturingYear() >= 12) {
            insights.add("Based on the manufacturing year, periodic preventive checks are especially useful.");
        }
        if (insights.isEmpty()) insights.add("Based on the information provided, no concern was reported. Keep up routine inspections.");
        score = Math.max(0, Math.min(100, score));
        String urgency = score < 50 || containsCritical(actions) ? "HIGH" : score < 75 ? "MEDIUM" : "LOW";
        return new Assessment(score, urgency, List.copyOf(insights), List.copyOf(actions), List.copyOf(reminders));
    }

    private int conditionPenalty(String name, String condition, List<String> insights, List<String> actions) {
        String c = normalize(condition);
        if (c.isEmpty() || c.equals("not checked")) { insights.add(name + ": not checked; assessment is limited."); return 4; }
        if (c.equals("good")) return 0;
        if (c.equals("fair")) { insights.add(name + ": fair condition reported."); actions.add("Check " + name.toLowerCase() + " at the next service."); return 7; }
        insights.add("Possible concern: " + name.toLowerCase() + " condition was reported as poor.");
        actions.add("Have the " + name.toLowerCase() + " inspected before a long trip."); return 18;
    }
    private int servicePenalty(VehicleHealthProfile h, List<String> reminders, List<String> actions) {
        if (h == null) { reminders.add("Add last-service details for a service reminder."); return 0; }
        int penalty = 0;
        if (h.getOdometerKm() <= 0 || h.getLastServiceMileageKm() <= 0) {
            reminders.add("Add odometer and last-service mileage for a mileage-based service reminder.");
        } else {
            double since = h.getOdometerKm() - h.getLastServiceMileageKm();
            if (since >= 10000) { reminders.add("Service reminder: " + Math.round(since) + " km since the last recorded service."); actions.add("Book a routine service soon."); penalty += 12; }
            else if (since >= 7500) { reminders.add("Service is approaching based on " + Math.round(since) + " km since the recorded service."); penalty += 5; }
        }
        LocalDate lastService = parse(h.getLastServiceDate());
        if (lastService == null) reminders.add("Add last-service date for a time-based service reminder.");
        else if (ChronoUnit.DAYS.between(lastService, LocalDate.now()) >= 365) { reminders.add("Last recorded service was over a year ago (" + lastService + ")."); actions.add("Arrange a routine service soon."); penalty += 10; }
        return penalty;
    }
    private int documentPenalty(VehicleModel v, VehicleHealthProfile h, List<String> reminders, List<String> actions) {
        int penalty = 0;
        if (v == null || blank(v.getVehicleInsuranceUrl())) { reminders.add("Insurance document is not available in the existing vehicle record."); penalty += 5; }
        else reminders.add("Insurance expiry is not stored in the existing vehicle record; review the uploaded document.");
        LocalDate puc = parse(h == null ? null : h.getPucExpiry());
        if (puc == null) reminders.add("Add PUC expiry to receive a PUC reminder.");
        else if (!puc.isAfter(LocalDate.now())) { reminders.add("PUC is expired or expires today."); actions.add("Renew PUC before operating the vehicle."); penalty += 20; }
        else if (ChronoUnit.DAYS.between(LocalDate.now(), puc) <= 30) { reminders.add("PUC expires on " + puc + "."); penalty += 5; }
        return penalty;
    }
    private boolean containsCritical(List<String> actions) { return actions.stream().anyMatch(a -> a.contains("before a long trip") || a.contains("Renew PUC")); }
    private LocalDate parse(String text) { try { return blank(text) ? null : LocalDate.parse(text); } catch (Exception ignored) { return null; } }
    private String value(VehicleHealthProfile h, java.util.function.Function<VehicleHealthProfile, String> f) { return h == null ? "" : f.apply(h); }
    private boolean blank(String s) { return s == null || s.isBlank(); }
    private String normalize(String s) { return blank(s) ? "" : s.trim().toLowerCase(Locale.ROOT); }
}

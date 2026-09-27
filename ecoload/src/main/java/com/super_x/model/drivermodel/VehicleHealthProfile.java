package com.super_x.model.drivermodel;

/**
 * Optional maintenance information that does not exist in the registered
 * vehicle fields. It is embedded as vehicleHealth in the existing vehicle
 * document; vehicle identity/specification fields remain on VehicleModel.
 */
public class VehicleHealthProfile {
    private double odometerKm;
    private String lastServiceDate;
    private double lastServiceMileageKm;
    private String pucExpiry;
    private String tyreCondition;
    private String brakeCondition;
    private String engineCondition;
    private String batteryCondition;
    private String fluidsCondition;
    private String warningLights;
    private String recentSymptoms;
    private String updatedAt;

    public VehicleHealthProfile() { }

    public double getOdometerKm() { return odometerKm; }
    public void setOdometerKm(double odometerKm) { this.odometerKm = odometerKm; }
    public String getLastServiceDate() { return lastServiceDate; }
    public void setLastServiceDate(String lastServiceDate) { this.lastServiceDate = lastServiceDate; }
    public double getLastServiceMileageKm() { return lastServiceMileageKm; }
    public void setLastServiceMileageKm(double lastServiceMileageKm) { this.lastServiceMileageKm = lastServiceMileageKm; }
    public String getPucExpiry() { return pucExpiry; }
    public void setPucExpiry(String pucExpiry) { this.pucExpiry = pucExpiry; }
    public String getTyreCondition() { return tyreCondition; }
    public void setTyreCondition(String tyreCondition) { this.tyreCondition = tyreCondition; }
    public String getBrakeCondition() { return brakeCondition; }
    public void setBrakeCondition(String brakeCondition) { this.brakeCondition = brakeCondition; }
    public String getEngineCondition() { return engineCondition; }
    public void setEngineCondition(String engineCondition) { this.engineCondition = engineCondition; }
    public String getBatteryCondition() { return batteryCondition; }
    public void setBatteryCondition(String batteryCondition) { this.batteryCondition = batteryCondition; }
    public String getFluidsCondition() { return fluidsCondition; }
    public void setFluidsCondition(String fluidsCondition) { this.fluidsCondition = fluidsCondition; }
    public String getWarningLights() { return warningLights; }
    public void setWarningLights(String warningLights) { this.warningLights = warningLights; }
    public String getRecentSymptoms() { return recentSymptoms; }
    public void setRecentSymptoms(String recentSymptoms) { this.recentSymptoms = recentSymptoms; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}

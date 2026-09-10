package com.emergency.model;

public class Ambulance {

    private String ambulanceId;
    private AmbulanceType type;
    private AmbulanceStatus status;
    private String driverName;
    private String driverPhone;
    private double currentLocationX;
    private double currentLocationY;

    public Ambulance(String ambulanceId,
                     AmbulanceType type,
                     String driverName,
                     String driverPhone,
                     double x,
                     double y) {

        this.ambulanceId = ambulanceId;
        this.type = type;
        this.status = AmbulanceStatus.AVAILABLE;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.currentLocationX = x;
        this.currentLocationY = y;
    }

    public String getAmbulanceId() {
        return ambulanceId;
    }

    public AmbulanceType getType() {
        return type;
    }

    public AmbulanceStatus getStatus() {
        return status;
    }

    public void setStatus(AmbulanceStatus status) {
        this.status = status;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public double getCurrentLocationX() {
        return currentLocationX;
    }

    public double getCurrentLocationY() {
        return currentLocationY;
    }

    public double calculateDistance(double x, double y) {

        return Math.sqrt(
                Math.pow(currentLocationX - x, 2)
                        + Math.pow(currentLocationY - y, 2)
        );
    }
}

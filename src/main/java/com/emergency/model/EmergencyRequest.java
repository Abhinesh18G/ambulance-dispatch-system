package com.emergency.model;

public class EmergencyRequest {

    private String requestId;
    private String patientId;
    private String emergencyType;
    private EmergencyPriority priority;

    private double pickupX;
    private double pickupY;

    private String destinationHospital;

    private String assignedAmbulanceId;

    private double estimatedDistance;
    private double estimatedArrivalTime;

    private EmergencyStatus status;

    public EmergencyRequest(String requestId,
                            String patientId,
                            String emergencyType,
                            EmergencyPriority priority,
                            double pickupX,
                            double pickupY,
                            String destinationHospital) {

        this.requestId = requestId;
        this.patientId = patientId;
        this.emergencyType = emergencyType;
        this.priority = priority;
        this.pickupX = pickupX;
        this.pickupY = pickupY;
        this.destinationHospital = destinationHospital;
        this.status = EmergencyStatus.WAITING;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public EmergencyPriority getPriority() {
        return priority;
    }

    public double getPickupX() {
        return pickupX;
    }

    public double getPickupY() {
        return pickupY;
    }

    public String getDestinationHospital() {
        return destinationHospital;
    }

    public String getAssignedAmbulanceId() {
        return assignedAmbulanceId;
    }

    public void setAssignedAmbulanceId(String assignedAmbulanceId) {
        this.assignedAmbulanceId = assignedAmbulanceId;
    }

    public double getEstimatedDistance() {
        return estimatedDistance;
    }

    public void setEstimatedDistance(double estimatedDistance) {
        this.estimatedDistance = estimatedDistance;
    }

    public double getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(double estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public EmergencyStatus getStatus() {
        return status;
    }

    public void setStatus(EmergencyStatus status) {
        this.status = status;
    }
}

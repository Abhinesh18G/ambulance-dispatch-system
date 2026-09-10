package com.emergency.service;

import com.emergency.exception.InvalidRequestException;
import com.emergency.model.*;

import java.util.*;

public class DispatchService {

    private final Map<String, Ambulance> ambulances = new HashMap<>();

    private final Map<String, EmergencyRequest> history = new HashMap<>();

    private final PriorityQueue<EmergencyRequest> waitingQueue =
            new PriorityQueue<>(
                    Comparator.comparingInt(
                            r -> r.getPriority().getRank()
                    )
            );

    public void registerAmbulance(Ambulance ambulance) {

        if (ambulance == null ||
                ambulance.getAmbulanceId() == null ||
                ambulance.getAmbulanceId().isEmpty()) {

            throw new InvalidRequestException(
                    "Invalid ambulance information."
            );
        }

        ambulances.put(
                ambulance.getAmbulanceId(),
                ambulance
        );

        System.out.println(
                "Ambulance registered: "
                        + ambulance.getAmbulanceId()
        );
    }

    public synchronized void submitEmergencyRequest(
            EmergencyRequest request) {

        validateRequest(request);

        if (history.containsKey(request.getRequestId())) {
            throw new InvalidRequestException(
                    "Duplicate emergency request ID."
            );
        }

        history.put(
                request.getRequestId(),
                request
        );

        Ambulance ambulance =
                findBestAvailableAmbulance(request);

        if (ambulance != null) {

            dispatch(request, ambulance);

        } else {

            request.setStatus(EmergencyStatus.WAITING);

            waitingQueue.add(request);

            System.out.println(
                    "No ambulance available."
            );

            System.out.println(
                    "Request added to waiting queue: "
                            + request.getRequestId()
            );
        }
    }

    private void validateRequest(EmergencyRequest request) {

        if (request == null) {
            throw new InvalidRequestException(
                    "Emergency request cannot be null."
            );
        }

        if (request.getRequestId() == null ||
                request.getRequestId().isEmpty()) {

            throw new InvalidRequestException(
                    "Request ID is required."
            );
        }

        if (request.getPatientId() == null ||
                request.getPatientId().isEmpty()) {

            throw new InvalidRequestException(
                    "Patient ID is required."
            );
        }

        if (request.getEmergencyType() == null ||
                request.getEmergencyType().isEmpty()) {

            throw new InvalidRequestException(
                    "Emergency type is required."
            );
        }

        if (request.getPriority() == null) {

            throw new InvalidRequestException(
                    "Emergency priority is required."
            );
        }

        if (request.getDestinationHospital() == null ||
                request.getDestinationHospital().isEmpty()) {

            throw new InvalidRequestException(
                    "Destination hospital is required."
            );
        }
    }

    private Ambulance findBestAvailableAmbulance(
            EmergencyRequest request) {

        Ambulance bestAmbulance = null;

        double shortestDistance = Double.MAX_VALUE;

        for (Ambulance ambulance : ambulances.values()) {

            if (ambulance.getStatus()
                    == AmbulanceStatus.AVAILABLE) {

                if (matchesRequirement(
                        ambulance.getType(),
                        request.getPriority())) {

                    double distance =
                            ambulance.calculateDistance(
                                    request.getPickupX(),
                                    request.getPickupY()
                            );

                    if (distance < shortestDistance) {

                        shortestDistance = distance;
                        bestAmbulance = ambulance;
                    }
                }
            }
        }

        return bestAmbulance;
    }

    private boolean matchesRequirement(
            AmbulanceType type,
            EmergencyPriority priority) {

        if (priority == EmergencyPriority.CRITICAL) {

            return type == AmbulanceType.ICU
                    || type == AmbulanceType.ADVANCED_LIFE_SUPPORT;
        }

        if (priority == EmergencyPriority.HIGH) {

            return type == AmbulanceType.ICU
                    || type == AmbulanceType.ADVANCED_LIFE_SUPPORT
                    || type == AmbulanceType.BASIC;
        }

        return true;
    }

    private void dispatch(
            EmergencyRequest request,
            Ambulance ambulance) {

        ambulance.setStatus(
                AmbulanceStatus.DISPATCHED
        );

        request.setAssignedAmbulanceId(
                ambulance.getAmbulanceId()
        );

        double distance =
                ambulance.calculateDistance(
                        request.getPickupX(),
                        request.getPickupY()
                );

        request.setEstimatedDistance(distance);

        // 1 distance unit = 2 minutes
        request.setEstimatedArrivalTime(
                distance * 2
        );

        request.setStatus(
                EmergencyStatus.DISPATCHED
        );

        System.out.println(
                "Emergency " + request.getRequestId()
                        + " dispatched."
        );

        System.out.println(
                "Ambulance: "
                        + ambulance.getAmbulanceId()
        );

        System.out.println(
                "Estimated distance: "
                        + distance
        );

        System.out.println(
                "Estimated arrival time: "
                        + request.getEstimatedArrivalTime()
                        + " minutes"
        );
    }

    public synchronized void updateAmbulanceStatus(
            String ambulanceId,
            AmbulanceStatus newStatus) {

        Ambulance ambulance =
                ambulances.get(ambulanceId);

        if (ambulance == null) {

            throw new InvalidRequestException(
                    "Ambulance ID not found."
            );
        }

        ambulance.setStatus(newStatus);

        updateEmergencyStatus(
                ambulanceId,
                newStatus
        );

        System.out.println(
                "Ambulance "
                        + ambulanceId
                        + " status changed to "
                        + newStatus
        );

        if (newStatus == AmbulanceStatus.AVAILABLE) {

            allocateWaitingEmergency();
        }
    }

    private void updateEmergencyStatus(
            String ambulanceId,
            AmbulanceStatus ambulanceStatus) {

        for (EmergencyRequest request :
                history.values()) {

            if (ambulanceId.equals(
                    request.getAssignedAmbulanceId())) {

                if (ambulanceStatus
                        == AmbulanceStatus.EN_ROUTE) {

                    request.setStatus(
                            EmergencyStatus.EN_ROUTE
                    );

                } else if (ambulanceStatus
                        == AmbulanceStatus.PATIENT_PICKED_UP) {

                    request.setStatus(
                            EmergencyStatus.PATIENT_PICKED_UP
                    );

                } else if (ambulanceStatus
                        == AmbulanceStatus.HOSPITAL_ARRIVED) {

                    request.setStatus(
                            EmergencyStatus.HOSPITAL_ARRIVED
                    );
                }
            }
        }
    }

    private void allocateWaitingEmergency() {

        if (waitingQueue.isEmpty()) {
            return;
        }

        List<EmergencyRequest> requests =
                new ArrayList<>(waitingQueue);

        waitingQueue.clear();

        for (EmergencyRequest request : requests) {

            Ambulance ambulance =
                    findBestAvailableAmbulance(request);

            if (ambulance != null) {

                dispatch(request, ambulance);

            } else {

                waitingQueue.add(request);
            }
        }
    }

    public List<Ambulance> getAvailableAmbulances() {

        List<Ambulance> result =
                new ArrayList<>();

        for (Ambulance ambulance :
                ambulances.values()) {

            if (ambulance.getStatus()
                    == AmbulanceStatus.AVAILABLE) {

                result.add(ambulance);
            }
        }

        return result;
    }

    public EmergencyRequest getRequestHistory(
            String requestId) {

        return history.get(requestId);
    }

    public int getWaitingQueueSize() {

        return waitingQueue.size();
    }

    public int getHistorySize() {

        return history.size();
    }
}

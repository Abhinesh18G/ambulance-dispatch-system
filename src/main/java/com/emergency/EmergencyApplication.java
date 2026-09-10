package com.emergency;

import com.emergency.model.*;
import com.emergency.service.DispatchService;

public class EmergencyApplication {

    public static void main(String[] args) {

        DispatchService service =
                new DispatchService();

        System.out.println(
                "===== AMBULANCE DISPATCH SYSTEM ====="
        );

        Ambulance a1 = new Ambulance(
                "AMB-101",
                AmbulanceType.BASIC,
                "Ravi",
                "9876543210",
                0,
                0
        );

        Ambulance a2 = new Ambulance(
                "AMB-102",
                AmbulanceType.ADVANCED_LIFE_SUPPORT,
                "Kumar",
                "9876543211",
                5,
                5
        );

        Ambulance a3 = new Ambulance(
                "AMB-103",
                AmbulanceType.ICU,
                "Arun",
                "9876543212",
                10,
                10
        );

        service.registerAmbulance(a1);
        service.registerAmbulance(a2);
        service.registerAmbulance(a3);

        EmergencyRequest r1 =
                new EmergencyRequest(
                        "REQ-001",
                        "PAT-101",
                        "Heart Attack",
                        EmergencyPriority.CRITICAL,
                        9,
                        9,
                        "City Hospital"
                );

        EmergencyRequest r2 =
                new EmergencyRequest(
                        "REQ-002",
                        "PAT-102",
                        "Fracture",
                        EmergencyPriority.MODERATE,
                        1,
                        1,
                        "Government Hospital"
                );

        EmergencyRequest r3 =
                new EmergencyRequest(
                        "REQ-003",
                        "PAT-103",
                        "Accident",
                        EmergencyPriority.HIGH,
                        3,
                        3,
                        "Apollo Hospital"
                );

        service.submitEmergencyRequest(r1);
        service.submitEmergencyRequest(r2);
        service.submitEmergencyRequest(r3);

        System.out.println();
        System.out.println("===== UPDATE AMBULANCE STATUS =====");

        service.updateAmbulanceStatus(
                "AMB-103",
                AmbulanceStatus.EN_ROUTE
        );

        service.updateAmbulanceStatus(
                "AMB-103",
                AmbulanceStatus.PATIENT_PICKED_UP
        );

        service.updateAmbulanceStatus(
                "AMB-103",
                AmbulanceStatus.HOSPITAL_ARRIVED
        );

        service.updateAmbulanceStatus(
                "AMB-103",
                AmbulanceStatus.AVAILABLE
        );

        System.out.println();
        System.out.println(
                "Waiting requests: "
                        + service.getWaitingQueueSize()
        );

        System.out.println(
                "Emergency history: "
                        + service.getHistorySize()
        );
    }
}

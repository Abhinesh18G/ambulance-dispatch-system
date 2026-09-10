package com.emergency.service;

import com.emergency.exception.InvalidRequestException;
import com.emergency.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DispatchServiceTest {

    private DispatchService service;

    @BeforeEach
    public void setup() {

        service = new DispatchService();
    }

    @Test
    public void testCriticalEmergencyGetsSuitableAmbulance() {

        Ambulance basic =
                new Ambulance(
                        "AMB-1",
                        AmbulanceType.BASIC,
                        "Driver 1",
                        "9000000001",
                        0,
                        0
                );

        Ambulance icu =
                new Ambulance(
                        "AMB-2",
                        AmbulanceType.ICU,
                        "Driver 2",
                        "9000000002",
                        10,
                        10
                );

        service.registerAmbulance(basic);
        service.registerAmbulance(icu);

        EmergencyRequest request =
                new EmergencyRequest(
                        "REQ-1",
                        "PAT-1",
                        "Heart Attack",
                        EmergencyPriority.CRITICAL,
                        9,
                        9,
                        "City Hospital"
                );

        service.submitEmergencyRequest(request);

        assertEquals(
                "AMB-2",
                request.getAssignedAmbulanceId()
        );

        assertEquals(
                AmbulanceStatus.DISPATCHED,
                icu.getStatus()
        );
    }

    @Test
    public void testNearestAmbulanceIsSelected() {

        Ambulance a1 =
                new Ambulance(
                        "AMB-1",
                        AmbulanceType.BASIC,
                        "Driver 1",
                        "9000000001",
                        0,
                        0
                );

        Ambulance a2 =
                new Ambulance(
                        "AMB-2",
                        AmbulanceType.BASIC,
                        "Driver 2",
                        "9000000002",
                        10,
                        10
                );

        service.registerAmbulance(a1);
        service.registerAmbulance(a2);

        EmergencyRequest request =
                new EmergencyRequest(
                        "REQ-2",
                        "PAT-2",
                        "Fever",
                        EmergencyPriority.NORMAL,
                        1,
                        1,
                        "Hospital A"
                );

        service.submitEmergencyRequest(request);

        assertEquals(
                "AMB-1",
                request.getAssignedAmbulanceId()
        );
    }

    @Test
    public void testWaitingQueue() {

        Ambulance ambulance =
                new Ambulance(
                        "AMB-1",
                        AmbulanceType.BASIC,
                        "Driver",
                        "9000000000",
                        0,
                        0
                );

        service.registerAmbulance(ambulance);

        EmergencyRequest request1 =
                new EmergencyRequest(
                        "REQ-1",
                        "PAT-1",
                        "Accident",
                        EmergencyPriority.NORMAL,
                        1,
                        1,
                        "Hospital A"
                );

        EmergencyRequest request2 =
                new EmergencyRequest(
                        "REQ-2",
                        "PAT-2",
                        "Accident",
                        EmergencyPriority.CRITICAL,
                        2,
                        2,
                        "Hospital B"
                );

        service.submitEmergencyRequest(request1);
        service.submitEmergencyRequest(request2);

        assertNull(
                request2.getAssignedAmbulanceId()
        );

        assertEquals(
                1,
                service.getWaitingQueueSize()
        );
    }

    @Test
    public void testAutomaticAllocationAfterAmbulanceAvailable() {

        Ambulance ambulance =
                new Ambulance(
                        "AMB-1",
                        AmbulanceType.BASIC,
                        "Driver",
                        "9000000000",
                        0,
                        0
                );

        service.registerAmbulance(ambulance);

        EmergencyRequest request1 =
                new EmergencyRequest(
                        "REQ-1",
                        "PAT-1",
                        "Accident",
                        EmergencyPriority.NORMAL,
                        1,
                        1,
                        "Hospital A"
                );

        EmergencyRequest request2 =
                new EmergencyRequest(
                        "REQ-2",
                        "PAT-2",
                        "Accident",
                        EmergencyPriority.NORMAL,
                        2,
                        2,
                        "Hospital B"
                );

        service.submitEmergencyRequest(request1);
        service.submitEmergencyRequest(request2);

        assertEquals(
                1,
                service.getWaitingQueueSize()
        );

        service.updateAmbulanceStatus(
                "AMB-1",
                AmbulanceStatus.AVAILABLE
        );

        assertEquals(
                "AMB-1",
                request2.getAssignedAmbulanceId()
        );
    }

    @Test
    public void testInvalidRequest() {

        assertThrows(
                InvalidRequestException.class,
                () -> service.submitEmergencyRequest(null)
        );
    }

    @Test
    public void testAmbulanceCannotBeAssignedTwice() {

        Ambulance ambulance =
                new Ambulance(
                        "AMB-1",
                        AmbulanceType.BASIC,
                        "Driver",
                        "9000000000",
                        0,
                        0
                );

        service.registerAmbulance(ambulance);

        EmergencyRequest request1 =
                new EmergencyRequest(
                        "REQ-1",
                        "PAT-1",
                        "Accident",
                        EmergencyPriority.NORMAL,
                        1,
                        1,
                        "Hospital A"
                );

        EmergencyRequest request2 =
                new EmergencyRequest(
                        "REQ-2",
                        "PAT-2",
                        "Accident",
                        EmergencyPriority.NORMAL,
                        2,
                        2,
                        "Hospital B"
                );

        service.submitEmergencyRequest(request1);
        service.submitEmergencyRequest(request2);

        assertEquals(
                "AMB-1",
                request1.getAssignedAmbulanceId()
        );

        assertNull(
                request2.getAssignedAmbulanceId()
        );
    }
}

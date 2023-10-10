package it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.RemovalRequestSendDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.RemovalRequestViewDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.SmartBinDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {RemovalRequestMapper.class})
@ExtendWith(MockitoExtension.class)
public class RemovalRequestMapperTest {

    private RemovalRequestMapper removalRequestMapper;

    @BeforeEach
    public void setUp() {
        removalRequestMapper = new RemovalRequestMapper();
    }

    @Test
    public void testToRemovalRequest() {

        RemovalRequestSendDTO removalRequestSendDTO = new RemovalRequestSendDTO();
        removalRequestSendDTO.setSmartBinID("TestSmartBinID");

        RemovalRequest result = removalRequestMapper.toRemovalRequest(removalRequestSendDTO);

        assertEquals("TestSmartBinID", result.getSmartBinID());
    }

    @Test
    public void testToRemovalRequestDTO() {

        RemovalRequest removalRequest = new RemovalRequest();
        Date requested =new Date() ;
        Date decision = new Date(System.currentTimeMillis() + 3600_000);

        removalRequest.setId("TestID");
        removalRequest.setStatus(RemovalRequest.Status.APPROVED);
        removalRequest.setSmartBinID("TestSmartBinID");
        removalRequest.setRequestedDate(requested);
        removalRequest.setDecisionDate(decision);

        RemovalRequestViewDTO result = removalRequestMapper.toRemovalRequestDTO(removalRequest);

        assertEquals("TestID", result.getId());
        assertEquals(RemovalRequest.Status.APPROVED.toString(), result.getStatus());
        assertEquals("TestSmartBinID", result.getSmartBinID());
        assertEquals(requested, result.getRequestedDate());
        assertEquals(decision, result.getDecisionDate());

    }
}

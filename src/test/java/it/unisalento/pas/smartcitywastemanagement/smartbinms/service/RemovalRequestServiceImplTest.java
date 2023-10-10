package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.RequestInvalidStatusException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.RemovalRequestRepository;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.SmartBinRepository;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.TypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {RemovalRequestServiceImpl.class})
public class RemovalRequestServiceImplTest {


    @InjectMocks
    private RemovalRequestServiceImpl removalRequestService;

    @MockBean
    private SmartBinRepository smartBinRepository;

    @MockBean
    private RemovalRequestRepository removalRequestRepository;


    @Test
    public void testSaveRemovalRequest() throws Exception {

        RemovalRequest removalRequest = new RemovalRequest();
        removalRequest.setId("TestRequestID");
        removalRequest.setSmartBinID("TestSmartBinID");

        SmartBin smartBin = new SmartBin();
        smartBin.setId("TestID");

        // Configurazione del comportamento del mock RemovalRequestRepository
        when(removalRequestRepository.existsBySmartBinIDAndStatus(removalRequest.getSmartBinID(), RemovalRequest.Status.PENDING)).thenReturn(false);
        when(smartBinRepository.findById(any(String.class))).thenReturn(Optional.of(smartBin));
        when(removalRequestRepository.save(any(RemovalRequest.class))).thenReturn(removalRequest);


        String requestId = removalRequestService.saveRemovalRequest(removalRequest);

        assertNotNull(requestId);
        assertEquals(removalRequest.getId(), requestId);


        verify(removalRequestRepository, times(1)).existsBySmartBinIDAndStatus(removalRequest.getSmartBinID(), RemovalRequest.Status.PENDING);
        verify(smartBinRepository, times(1)).findById(removalRequest.getSmartBinID());
        verify(removalRequestRepository, times(1)).save(removalRequest);
    }


    @Test
    public void testManageRemovalRequestWhenDisapprove() throws Exception {

        String requestID = "TestID";

        RemovalRequest removalRequest = new RemovalRequest();
        removalRequest.setId(requestID);
        removalRequest.setRequestedDate(new Date());
        removalRequest.setStatus(RemovalRequest.Status.PENDING);

        when(removalRequestRepository.save(any(RemovalRequest.class))).thenReturn(removalRequest);
        when(removalRequestRepository.findById(any(String.class))).thenReturn(Optional.of(removalRequest));

        removalRequestService.manageRemovalRequest(removalRequest.getId(),"Disapprove");

        assertNotNull(removalRequest.getDecisionDate());
        assertEquals(RemovalRequest.Status.REJECTED, removalRequest.getStatus());

        verify(removalRequestRepository, times(1)).findById(requestID);
        verify(removalRequestRepository, times(1)).save(removalRequest);
        verify(smartBinRepository, never()).findById(anyString());
        verify(smartBinRepository, never()).save(any(SmartBin.class));
    }

    @Test
    public void testManageRemovalRequestWhenApprove() throws Exception {

        String requestID = "TestID";

        RemovalRequest removalRequest = new RemovalRequest();
        removalRequest.setId(requestID);
        removalRequest.setRequestedDate(new Date());
        removalRequest.setStatus(RemovalRequest.Status.PENDING);
        removalRequest.setSmartBinID("TestSmartBinID");

        SmartBin smartBin = new SmartBin();
        smartBin.setId("TestSmartBinID");
        smartBin.setState(SmartBin.State.ALLOCATED);


        when(removalRequestRepository.save(any(RemovalRequest.class))).thenReturn(removalRequest);
        when(removalRequestRepository.findById(any(String.class))).thenReturn(Optional.of(removalRequest));
        when(smartBinRepository.findById(removalRequest.getSmartBinID())).thenReturn(Optional.of(smartBin));

        removalRequestService.manageRemovalRequest(removalRequest.getId(),"Approve");

        assertNotNull(removalRequest.getDecisionDate());
        assertEquals(RemovalRequest.Status.APPROVED, removalRequest.getStatus());
        assertEquals(SmartBin.State.DEALLOCATED, smartBin.getState());


        verify(removalRequestRepository, times(1)).findById(requestID);
        verify(removalRequestRepository, times(1)).save(removalRequest);
        verify(smartBinRepository, times(1)).findById(removalRequest.getSmartBinID());
        verify(smartBinRepository, times(1)).save(smartBin);
    }

    @Test
    public void testGetRequestByStatusWhenStatusIsInvalid() {
        String invalidStatus = "INVALID";
        assertThrows(RequestInvalidStatusException.class, () -> removalRequestService.getRequestByStatus(invalidStatus));

        verify(removalRequestRepository, never()).findByStatus(invalidStatus);
    }

    @Test
    public void testGetRequestByStatus() throws Exception{

        String validStatus = "PENDING";

        List<RemovalRequest> removalRequests = new ArrayList<>();
        RemovalRequest rm1 = new RemovalRequest();
        rm1.setStatus(RemovalRequest.Status.PENDING);

        RemovalRequest rm2 = new RemovalRequest();
        rm2.setStatus(RemovalRequest.Status.PENDING);

        removalRequests.add(rm1);
        removalRequests.add(rm2);

        when(removalRequestRepository.findByStatus(validStatus)).thenReturn(removalRequests);

        List<RemovalRequest> result = removalRequestService.getRequestByStatus(validStatus);

        assertNotNull(result);
        assertEquals(removalRequests, result);


        verify(removalRequestRepository, times(1)).findByStatus(validStatus);
    }

    @Test
    public void testGetRequestById() throws Exception{

        String requestID = "TestID";
        RemovalRequest removalRequest = new RemovalRequest();
        removalRequest.setId(requestID);

        when(removalRequestRepository.findById(requestID)).thenReturn(Optional.of(removalRequest));

        RemovalRequest result = removalRequestService.getRequestByID(requestID);

        assertNotNull(result);
        assertEquals(removalRequest, result);

        verify(removalRequestRepository, times(1)).findById(requestID);
    }

    @Test
    public void testGetAll_requests() {

        List<RemovalRequest> removalRequests = new ArrayList<>();
        RemovalRequest rm1 = new RemovalRequest();
        rm1.setId("Rm1");

        RemovalRequest rm2 = new RemovalRequest();
        rm2.setId("Rm2");

        removalRequests.add(rm1);
        removalRequests.add(rm2);

        when(removalRequestRepository.findAll()).thenReturn(removalRequests);

        List<RemovalRequest> result = removalRequestService.getAllRequests();

        assertNotNull(result);
        assertEquals(removalRequests, result);

    }
}

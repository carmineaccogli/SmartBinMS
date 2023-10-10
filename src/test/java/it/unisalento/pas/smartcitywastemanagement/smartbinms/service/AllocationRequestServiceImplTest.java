package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;


import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.RequestInvalidStatusException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinAlreadyAllocatedException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.AllocationRequestRepository;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.SmartBinRepository;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.TypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {AllocationRequestServiceImpl.class})
public class AllocationRequestServiceImplTest {

    @InjectMocks
    private AllocationRequestServiceImpl allocationRequestService;

    @MockBean
    private SmartBinRepository smartBinRepository;

    @MockBean
    private AllocationRequestRepository allocationRequestRepository;

    @MockBean
    private TypeRepository typeRepository;


    @Test
    public void testGetRequestByStatusWhenStatusIsInvalid() {
        String invalidStatus = "INVALID";
        assertThrows(RequestInvalidStatusException.class, () -> allocationRequestService.getRequestByStatus(invalidStatus));

        verify(allocationRequestRepository, never()).findByStatus(invalidStatus);
    }

    @Test
    public void testGetRequestByStatus() throws Exception{

        String validStatus = "PENDING";

        List<AllocationRequest> allocationRequests = new ArrayList<>();
        AllocationRequest rm1 = new AllocationRequest();
        rm1.setStatus(AllocationRequest.Status.PENDING);

        AllocationRequest rm2 = new AllocationRequest();
        rm2.setStatus(AllocationRequest.Status.PENDING);

        allocationRequests.add(rm1);
        allocationRequests.add(rm2);

        when(allocationRequestRepository.findByStatus(any(String.class))).thenReturn(allocationRequests);

        List<AllocationRequest> result = allocationRequestService.getRequestByStatus(validStatus);

        assertNotNull(result);
        assertEquals(allocationRequests, result);


        verify(allocationRequestRepository, times(1)).findByStatus(validStatus);
    }

    @Test
    public void testGetRequestById() throws Exception{

        String requestID = "TestID";
        AllocationRequest allocationRequest = new AllocationRequest();
        allocationRequest.setId(requestID);

        when(allocationRequestRepository.findById(any(String.class))).thenReturn(Optional.of(allocationRequest));

        AllocationRequest result = allocationRequestService.getRequestByID(requestID);

        assertNotNull(result);
        assertEquals(allocationRequest, result);

        verify(allocationRequestRepository, times(1)).findById(requestID);
    }

    @Test
    public void testGetAll_requests() {

        List<AllocationRequest> allocationRequests = new ArrayList<>();
        AllocationRequest rm1 = new AllocationRequest();
        rm1.setId("Rm1");

        AllocationRequest rm2 = new AllocationRequest();
        rm2.setId("Rm2");

        allocationRequests.add(rm1);
        allocationRequests.add(rm2);

        when(allocationRequestRepository.findAll()).thenReturn(allocationRequests);

        List<AllocationRequest> result = allocationRequestService.getAllRequests();

        assertNotNull(result);
        assertEquals(allocationRequests, result);
    }


    @Test
    public void testSaveAllocationRequest() throws Exception {

        Type type = new Type();
        type.setName("TestType");

        AllocationRequest allocationRequest = new AllocationRequest();
        allocationRequest.setId("TestID");
        allocationRequest.setType(type);
        allocationRequest.setPosition(new GeoJsonPoint(18,42));

        when(allocationRequestRepository.existsAllocationRequestByTypeAndPositionAndStatus(allocationRequest.getType(),allocationRequest.getPosition(), AllocationRequest.Status.PENDING)).thenReturn(false);
        when(allocationRequestRepository.save(any(AllocationRequest.class))).thenReturn(allocationRequest);

        String result = allocationRequestService.saveAllocationRequest(allocationRequest);
        assertNotNull(result);
        assertEquals(AllocationRequest.Status.PENDING,allocationRequest.getStatus());
        assertNotNull(allocationRequest.getRequestedDate());

        verify(allocationRequestRepository, times(1)).existsAllocationRequestByTypeAndPositionAndStatus(allocationRequest.getType(), allocationRequest.getPosition(), AllocationRequest.Status.PENDING);
        verify(allocationRequestRepository, times(1)).save(allocationRequest);
    }

    @Test
    public void testDenyAllocationRequest() throws Exception{

        String requestId = "TestID";

        AllocationRequest allocationRequest = new AllocationRequest();
        allocationRequest.setId(requestId);
        allocationRequest.setStatus(AllocationRequest.Status.PENDING);


        when(allocationRequestRepository.findById(any(String.class))).thenReturn(Optional.of(allocationRequest));

        allocationRequestService.denyAllocationRequest(requestId);

        assertNotNull(allocationRequest.getDecisionDate());
        assertEquals(AllocationRequest.Status.REJECTED, allocationRequest.getStatus());

        verify(allocationRequestRepository, times(1)).save(allocationRequest);
    }

    @Test
    public void testApproveAllocationRequest() throws Exception {
        String requestId = "TestID";
        Type type = new Type();
        type.setName("TypeTest");

        AllocationRequest allocationRequest = new AllocationRequest();
        allocationRequest.setId(requestId);
        allocationRequest.setStatus(AllocationRequest.Status.PENDING);
        allocationRequest.setSmartBinName("SmartBinName");
        allocationRequest.setType(type);
        allocationRequest.setPosition(new GeoJsonPoint(18,42));

        SmartBin smartBin = new SmartBin();
        smartBin.setId(allocationRequest.getSmartBinName());


        when(allocationRequestRepository.findById(any(String.class))).thenReturn(Optional.of(allocationRequest));
        when(smartBinRepository.existsByTypeAndPositionAndState(allocationRequest.getType(), allocationRequest.getPosition(), SmartBin.State.ALLOCATED)).thenReturn(false);
        when(allocationRequestRepository.save(any(AllocationRequest.class))).thenReturn(allocationRequest);
        when(smartBinRepository.save(any(SmartBin.class))).thenReturn(smartBin);

        allocationRequestService.approveAllocationRequest(requestId);

        assertEquals(AllocationRequest.Status.ACCEPTED, allocationRequest.getStatus());
        assertNotNull(allocationRequest.getDecisionDate());

        verify(allocationRequestRepository, times(1)).findById(requestId);
        verify(allocationRequestRepository, times(1)).save(allocationRequest);
        verify(smartBinRepository, times(1)).existsByTypeAndPositionAndState(allocationRequest.getType(), allocationRequest.getPosition(), SmartBin.State.ALLOCATED);
        verify(smartBinRepository, times(1)).save(any(SmartBin.class));

    }

    @Test
    public void testApproveAllocationRequestWhenSmartBinAlreadyAllocated() throws Exception {

        String requestId = "TestID";
        Type type = new Type();
        type.setName("TypeTest");



        AllocationRequest allocationRequest = new AllocationRequest();
        allocationRequest.setId(requestId);
        allocationRequest.setStatus(AllocationRequest.Status.PENDING);
        allocationRequest.setSmartBinName("SmartBinName");
        allocationRequest.setType(type);
        allocationRequest.setPosition(new GeoJsonPoint(18,42));

        SmartBin smartBin = new SmartBin();
        smartBin.setId(allocationRequest.getSmartBinName());
        smartBin.setState(SmartBin.State.ALLOCATED);


        when(allocationRequestRepository.findById(any(String.class))).thenReturn(Optional.of(allocationRequest));
        when(smartBinRepository.existsByTypeAndPositionAndState(allocationRequest.getType(),allocationRequest.getPosition(),SmartBin.State.ALLOCATED)).thenReturn(true);
        when(allocationRequestRepository.save(any(AllocationRequest.class))).thenReturn(allocationRequest);

        assertThrows(SmartBinAlreadyAllocatedException.class, () -> allocationRequestService.approveAllocationRequest(requestId));
        assertEquals(AllocationRequest.Status.REJECTED, allocationRequest.getStatus());
        assertNotNull(allocationRequest.getDecisionDate());

        verify(allocationRequestRepository, times(1)).findById(requestId);
        verify(allocationRequestRepository, times(1)).save(allocationRequest);
        verify(smartBinRepository, times(1)).existsByTypeAndPositionAndState(allocationRequest.getType(), allocationRequest.getPosition(), SmartBin.State.ALLOCATED);
        verify(smartBinRepository, never()).save(any(SmartBin.class));



    }
}

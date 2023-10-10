package it.unisalento.pas.smartcitywastemanagement.smartbinms.restcontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.*;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.*;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.AllocationRequestMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.RemovalRequestMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.service.AllocationRequestService;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.service.RemovalRequestService;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.utility.MunicipalityBoundaryUtils;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.validators.ValidGeoJSONPointValidator;
import org.bson.types.Decimal128;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class RequestRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AllocationRequestService allocationRequestService;

    @MockBean
    private AllocationRequestMapper allocationRequestMapper;

    @MockBean
    private RemovalRequestMapper removalRequestMapper;

    @MockBean
    private RemovalRequestService removalRequestService;

    @MockBean
    private LocalValidatorFactoryBean validator;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testSendAllocationRequest() throws Exception {

        AllocationRequestSendDTO requestDTO = new AllocationRequestSendDTO();
        requestDTO.setType("TypeTest");
        requestDTO.setSmartBinName("NameTest");
        requestDTO.setTotalCapacity(new BigDecimal(50));
        requestDTO.setPosition(new GeoJsonPoint(18.3,42.9));

        Type type = new Type();
        type.setId("1");
        type.setName("TypeTest");

        AllocationRequest allocationRequest = new AllocationRequest();
        allocationRequest.setType(type);
        allocationRequest.setSmartBinName(requestDTO.getSmartBinName());
        allocationRequest.setTotalCapacity(new Decimal128(requestDTO.getTotalCapacity()));
        allocationRequest.setPosition(requestDTO.getPosition());

        // Mockare il comportamento del servizio per restituire un ID creato
        when(allocationRequestMapper.toAllocationRequest(any(AllocationRequestSendDTO.class))).thenReturn(allocationRequest);
        when(allocationRequestService.saveAllocationRequest(any(AllocationRequest.class)))
                .thenReturn("TestAllocationID");

        // Eseguire la richiesta POST
        mockMvc.perform(MockMvcRequestBuilders.post("/api/smartbin/request/allocation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Allocation Request created successfully"))
                .andExpect(jsonPath("$.data").value("TestAllocationID"));
    }

    @Test
    public void testSendAllocationRequestWhenRequestAlreadyExists() throws Exception {

        AllocationRequestSendDTO requestDTO = new AllocationRequestSendDTO();
        requestDTO.setType("TypeTest");
        requestDTO.setSmartBinName("NameTest");
        requestDTO.setTotalCapacity(new BigDecimal(50));
        requestDTO.setPosition(new GeoJsonPoint(18.3,42.9));

        Type type = new Type();
        type.setId("1");
        type.setName("TypeTest");

        AllocationRequest allocationRequest = new AllocationRequest();
        allocationRequest.setType(type);
        allocationRequest.setSmartBinName(requestDTO.getSmartBinName());
        allocationRequest.setTotalCapacity(new Decimal128(requestDTO.getTotalCapacity()));
        allocationRequest.setPosition(requestDTO.getPosition());

        when(allocationRequestMapper.toAllocationRequest(any(AllocationRequestSendDTO.class))).thenReturn(allocationRequest);
        when(allocationRequestService.saveAllocationRequest(any(AllocationRequest.class)))
                .thenThrow(RequestAlreadyExistsException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/smartbin/request/allocation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict());


    }


    @Test
    public void testApproveAllocationWhenRequestNotFound() throws Exception {

        String requestedID = "TestRequestID";

       doThrow(RequestNotFoundException.class).when(allocationRequestService).approveAllocationRequest(any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/smartbin/request/allocation/approve/"+requestedID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testApproveAllocationWhenSmartBinIsAlreadyAllocated() throws Exception {

        String requestedID = "TestRequestID";

        doThrow(SmartBinAlreadyAllocatedException.class).when(allocationRequestService).approveAllocationRequest(any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/smartbin/request/allocation/approve/"+requestedID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void testDisapproveAllocation()throws Exception {
        String requestedID = "TestRequestID";

        doThrow(RequestAlreadyConfirmedException.class).when(allocationRequestService).denyAllocationRequest(any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/smartbin/request/allocation/disapprove/"+requestedID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void testApproveRemoval() throws Exception {
        String requestedID = "TestRequestID";

        doThrow(SmartBinAlreadyRemovedException.class).when(removalRequestService).manageRemovalRequest(any(String.class),any(String.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/smartbin/request/removal/approve/"+requestedID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void testSendRemovalRequest() throws Exception {

        RemovalRequestSendDTO requestDTO = new RemovalRequestSendDTO();
        requestDTO.setSmartBinID("SmartBinTest");

        RemovalRequest removalRequest = new RemovalRequest();
        removalRequest.setSmartBinID(requestDTO.getSmartBinID());


        // Mockare il comportamento del servizio per restituire un ID creato
        when(removalRequestMapper.toRemovalRequest(any(RemovalRequestSendDTO.class))).thenReturn(removalRequest);
        when(removalRequestService.saveRemovalRequest(any(RemovalRequest.class)))
                .thenReturn("TestRemovalID");

        // Eseguire la richiesta POST
        mockMvc.perform(MockMvcRequestBuilders.post("/api/smartbin/request/removal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Removal Request created successfully"))
                .andExpect(jsonPath("$.data").value("TestRemovalID"));
    }

    @Test
    public void testGetAll_AllocationRequest() throws Exception {

        // Lista mock di allocation requests
        List<AllocationRequest> mockRequests = new ArrayList<>();

        AllocationRequest pendingRequest = new AllocationRequest();
        pendingRequest.setId("1");
        pendingRequest.setStatus(AllocationRequest.Status.PENDING);

        AllocationRequest acceptedRequest = new AllocationRequest();
        acceptedRequest.setId("2");
        acceptedRequest.setStatus(AllocationRequest.Status.ACCEPTED);

        mockRequests.add(pendingRequest);
        mockRequests.add(acceptedRequest);

        // Crea una lista  di AllocationRequestDTO corrispondente
        List<AllocationRequestViewDTO> mockRequestsDTO = new ArrayList<>();

        AllocationRequestViewDTO pendingRequestDTO = new AllocationRequestViewDTO();
        pendingRequestDTO.setId(pendingRequest.getId());
        pendingRequestDTO.setStatus(pendingRequest.getStatus().toString());

        AllocationRequestViewDTO acceptedRequestDTO = new AllocationRequestViewDTO();
        acceptedRequestDTO.setId(acceptedRequest.getId());
        acceptedRequestDTO.setStatus(acceptedRequest.getStatus().toString());

        mockRequestsDTO.add(pendingRequestDTO);
        mockRequestsDTO.add(acceptedRequestDTO);

        // Configura il comportamento del mock del service
        when(allocationRequestService.getAllRequests()).thenReturn(mockRequests);
        when(allocationRequestMapper.toAllocationRequestDTO(pendingRequest)).thenReturn(pendingRequestDTO);
        when(allocationRequestMapper.toAllocationRequestDTO(acceptedRequest)).thenReturn(acceptedRequestDTO);

        // Esegue la richiesta GET
        mockMvc.perform(MockMvcRequestBuilders.get("/api/smartbin/request/allocation/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(mockRequestsDTO.get(0).getId()))
                .andExpect(jsonPath("$.[0].status").value(mockRequestsDTO.get(0).getStatus()))
                .andExpect(jsonPath("$.[1].id").value(mockRequestsDTO.get(1).getId()))
                .andExpect(jsonPath("$.[1].status").value(mockRequestsDTO.get(1).getStatus()));
    }

    @Test
    public void testGetAll_RemovalRequest() throws Exception {

        // Lista mock di allocation requests
        List<RemovalRequest> mockRequests = new ArrayList<>();

        RemovalRequest pendingRequest = new RemovalRequest();
        pendingRequest.setId("1");
        pendingRequest.setStatus(RemovalRequest.Status.PENDING);

        RemovalRequest acceptedRequest = new RemovalRequest();
        acceptedRequest.setId("2");
        acceptedRequest.setStatus(RemovalRequest.Status.APPROVED);

        mockRequests.add(pendingRequest);
        mockRequests.add(acceptedRequest);

        // Crea una lista  di AllocationRequestDTO corrispondente
        List<RemovalRequestViewDTO> mockRequestsDTO = new ArrayList<>();

        RemovalRequestViewDTO pendingRequestDTO = new RemovalRequestViewDTO();
        pendingRequestDTO.setId(pendingRequest.getId());
        pendingRequestDTO.setStatus(pendingRequest.getStatus().toString());

        RemovalRequestViewDTO acceptedRequestDTO = new RemovalRequestViewDTO();
        acceptedRequestDTO.setId(acceptedRequest.getId());
        acceptedRequestDTO.setStatus(acceptedRequest.getStatus().toString());

        mockRequestsDTO.add(pendingRequestDTO);
        mockRequestsDTO.add(acceptedRequestDTO);

        // Configura il comportamento del mock del service
        when(removalRequestService.getAllRequests()).thenReturn(mockRequests);
        when(removalRequestMapper.toRemovalRequestDTO(pendingRequest)).thenReturn(pendingRequestDTO);
        when(removalRequestMapper.toRemovalRequestDTO(acceptedRequest)).thenReturn(acceptedRequestDTO);

        // Esegue la richiesta GET
        mockMvc.perform(MockMvcRequestBuilders.get("/api/smartbin/request/removal/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(mockRequestsDTO.get(0).getId()))
                .andExpect(jsonPath("$.[0].status").value(mockRequestsDTO.get(0).getStatus()))
                .andExpect(jsonPath("$.[1].id").value(mockRequestsDTO.get(1).getId()))
                .andExpect(jsonPath("$.[1].status").value(mockRequestsDTO.get(1).getStatus()));
    }

    @Test
    public void testGetAllocationRequest() throws Exception {

        AllocationRequest request = new AllocationRequest();
        request.setId("1");

        AllocationRequestViewDTO requestViewDTO = new AllocationRequestViewDTO();
        requestViewDTO.setId(request.getId());


        when(allocationRequestService.getRequestByID(any(String.class))).thenReturn(request);
        when(allocationRequestMapper.toAllocationRequestDTO(any(AllocationRequest.class))).thenReturn(requestViewDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/smartbin/request/allocation/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(requestViewDTO.getId()))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetRemovalRequest() throws Exception {

        RemovalRequest request = new RemovalRequest();
        request.setId("1");

        RemovalRequestViewDTO requestViewDTO = new RemovalRequestViewDTO();
        requestViewDTO.setId(request.getId());


        when(removalRequestService.getRequestByID(any(String.class))).thenReturn(request);
        when(removalRequestMapper.toRemovalRequestDTO(any(RemovalRequest.class))).thenReturn(requestViewDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/smartbin/request/removal/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(requestViewDTO.getId()))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetAllocationRequestByStatus() throws Exception {

        // Lista mock di allocation requests
        List<AllocationRequest> mockRequests = new ArrayList<>();

        AllocationRequest pendingRequest1 = new AllocationRequest();
        pendingRequest1.setId("1");
        pendingRequest1.setStatus(AllocationRequest.Status.PENDING);

        AllocationRequest pendingRequest2 = new AllocationRequest();
        pendingRequest2.setId("2");
        pendingRequest2.setStatus(AllocationRequest.Status.PENDING);

        mockRequests.add(pendingRequest1);
        mockRequests.add(pendingRequest2);

        // Crea una lista  di AllocationRequestDTO corrispondente
        List<AllocationRequestViewDTO> mockRequestsDTO = new ArrayList<>();

        AllocationRequestViewDTO pendingRequestDTO1 = new AllocationRequestViewDTO();
        pendingRequestDTO1.setId(pendingRequest1.getId());
        pendingRequestDTO1.setStatus(pendingRequest1.getStatus().toString());

        AllocationRequestViewDTO pendingRequestDTO2 = new AllocationRequestViewDTO();
        pendingRequestDTO2.setId(pendingRequest2.getId());
        pendingRequestDTO2.setStatus(pendingRequest2.getStatus().toString());

        mockRequestsDTO.add(pendingRequestDTO1);
        mockRequestsDTO.add(pendingRequestDTO2);

        // Configura il comportamento del mock del service
        when(allocationRequestService.getRequestByStatus(any(String.class))).thenReturn(mockRequests);
        when(allocationRequestMapper.toAllocationRequestDTO(pendingRequest1)).thenReturn(pendingRequestDTO1);
        when(allocationRequestMapper.toAllocationRequestDTO(pendingRequest2)).thenReturn(pendingRequestDTO2);

        // Esegue la richiesta GET
        mockMvc.perform(MockMvcRequestBuilders.get("/api/smartbin/request/allocation")
                        .param("status","Pending"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(mockRequestsDTO.get(0).getId()))
                .andExpect(jsonPath("$.[0].status").value(mockRequestsDTO.get(0).getStatus()))
                .andExpect(jsonPath("$.[1].id").value(mockRequestsDTO.get(1).getId()))
                .andExpect(jsonPath("$.[1].status").value(mockRequestsDTO.get(1).getStatus()));
    }

    @Test
    public void testGetRemovalRequestByStatus() throws Exception {

        List<RemovalRequest> mockRequests = new ArrayList<>();

        RemovalRequest pendingRequest1 = new RemovalRequest();
        pendingRequest1.setId("1");
        pendingRequest1.setStatus(RemovalRequest.Status.REJECTED);

        RemovalRequest pendingRequest2 = new RemovalRequest();
        pendingRequest2.setId("2");
        pendingRequest2.setStatus(RemovalRequest.Status.REJECTED);

        mockRequests.add(pendingRequest1);
        mockRequests.add(pendingRequest2);

        // Crea una lista  di AllocationRequestDTO corrispondente
        List<RemovalRequestViewDTO> mockRequestsDTO = new ArrayList<>();

        RemovalRequestViewDTO pendingRequestDTO1 = new RemovalRequestViewDTO();
        pendingRequestDTO1.setId(pendingRequest1.getId());
        pendingRequestDTO1.setStatus(pendingRequest1.getStatus().toString());

        RemovalRequestViewDTO pendingRequestDTO2 = new RemovalRequestViewDTO();
        pendingRequestDTO2.setId(pendingRequest2.getId());
        pendingRequestDTO2.setStatus(pendingRequest2.getStatus().toString());

        mockRequestsDTO.add(pendingRequestDTO1);
        mockRequestsDTO.add(pendingRequestDTO2);

        // Configura il comportamento del mock del service
        when(removalRequestService.getRequestByStatus(any(String.class))).thenReturn(mockRequests);
        when(removalRequestMapper.toRemovalRequestDTO(pendingRequest1)).thenReturn(pendingRequestDTO1);
        when(removalRequestMapper.toRemovalRequestDTO(pendingRequest2)).thenReturn(pendingRequestDTO2);

        // Esegue la richiesta GET
        mockMvc.perform(MockMvcRequestBuilders.get("/api/smartbin/request/removal")
                        .param("status","rejected"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(mockRequestsDTO.get(0).getId()))
                .andExpect(jsonPath("$.[0].status").value(mockRequestsDTO.get(0).getStatus()))
                .andExpect(jsonPath("$.[1].id").value(mockRequestsDTO.get(1).getId()))
                .andExpect(jsonPath("$.[1].status").value(mockRequestsDTO.get(1).getStatus()));

    }
}

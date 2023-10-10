package it.unisalento.pas.smartcitywastemanagement.smartbinms.restcontrollers;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.AllocationRequestViewDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.SmartBinDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinStateInvalidException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.SmartBinMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.TypeMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.service.ManageSmartBinsService;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.service.TypeService;
import org.bson.types.Decimal128;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SmartBinRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class SmartBinRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManageSmartBinsService manageSmartBinsService;

    @MockBean
    private SmartBinMapper smartBinMapper;


    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testGetAllBins() throws Exception {

        List<SmartBin> mockBins = new ArrayList<>();

        Type mixedType = new Type();
        mixedType.setName("Indifferenziata");
        Type sortedType = new Type();
        sortedType.setName("GenericSorted");

        SmartBin mixed = new SmartBin();
        mixed.setId("1");
        mixed.setType(mixedType);

        SmartBin sortedGeneric = new SmartBin();
        sortedGeneric.setId("2");
        sortedGeneric.setType(sortedType);

        mockBins.add(mixed);
        mockBins.add(sortedGeneric);

        List<SmartBinDTO> mockBinsDTO = new ArrayList<>();
        SmartBinDTO mixedDTO = new SmartBinDTO();
        mixedDTO.setId(mixed.getId());
        mixedDTO.setType(mixed.getType().getName());

        SmartBinDTO sortedDTO = new SmartBinDTO();
        sortedDTO.setId(sortedGeneric.getId());
        sortedDTO.setType(sortedGeneric.getType().getName());

        mockBinsDTO.add(mixedDTO);
        mockBinsDTO.add(sortedDTO);


        // Mock del servizio
        when(manageSmartBinsService.getAllSmartBins()).thenReturn(mockBins);
        when(smartBinMapper.toSmartBinDTO(mixed)).thenReturn(mixedDTO);
        when(smartBinMapper.toSmartBinDTO(sortedGeneric)).thenReturn(sortedDTO);

        // Esegui la richiesta GET
       mockMvc.perform(get("/api/smartbin/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetAllBinsWithEmptyList() throws Exception {
        List<SmartBin> emptyBins = new ArrayList<>();

        when(manageSmartBinsService.getAllSmartBins()).thenReturn(emptyBins);


        mockMvc.perform(get("/api/smartbin/"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    public void testGetSmartBin() throws Exception {

        Type mixedType = new Type();
        mixedType.setName("Indifferenziata");

        SmartBin mixed = new SmartBin();
        mixed.setId("1");
        mixed.setType(mixedType);

        SmartBinDTO mixedDTO = new SmartBinDTO();
        mixedDTO.setId(mixed.getId());
        mixedDTO.setType(mixed.getType().getName());

        when(manageSmartBinsService.getSmartBinByID(any(String.class))).thenReturn(mixed);
        when(smartBinMapper.toSmartBinDTO(any(SmartBin.class))).thenReturn(mixedDTO);

        // Esegui la richiesta GET
        mockMvc.perform(get("/api/smartbin/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mixedDTO.getId()))
                .andExpect(jsonPath("$.type").value(mixedDTO.getType()));
    }

    @Test
    public void testGetSmartBinWithNotFoundException() throws Exception {


        when(manageSmartBinsService.getSmartBinByID(any(String.class))).thenThrow(SmartBinNotFoundException.class);

        // Esegui la richiesta GET
        mockMvc.perform(get("/api/smartbin/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetSmartBinByState() throws Exception {

        List<SmartBin> binsRequested = new ArrayList<>();
        SmartBin bin1 = new SmartBin();
        bin1.setId("1");
        bin1.setState(SmartBin.State.ALLOCATED);

        SmartBin bin2 = new SmartBin();
        bin2.setId("2");
        bin2.setState(SmartBin.State.ALLOCATED);

        binsRequested.add(bin1);
        binsRequested.add(bin2);

        List<SmartBinDTO> binsRequestedDTO = new ArrayList<>();

        SmartBinDTO bin1DTO = new SmartBinDTO();
        bin1DTO.setId(bin1.getId());
        bin1DTO.setState(bin1.getState().toString());

        SmartBinDTO bin2DTO = new SmartBinDTO();
        bin2DTO.setId(bin2.getId());
        bin2DTO.setState(bin2.getState().toString());

        binsRequestedDTO.add(bin1DTO);
        binsRequestedDTO.add(bin2DTO);

        // Configura il comportamento del mock del service
        when(manageSmartBinsService.filterSmartBinByState(any(String.class))).thenReturn(binsRequested);
        when(smartBinMapper.toSmartBinDTO(bin1)).thenReturn(bin1DTO);
        when(smartBinMapper.toSmartBinDTO(bin2)).thenReturn(bin2DTO);

        // Esegue la richiesta GET
        mockMvc.perform(MockMvcRequestBuilders.get("/api/smartbin/state")
                        .param("state","Allocated"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(binsRequestedDTO.get(0).getId()))
                .andExpect(jsonPath("$.[0].state").value(binsRequestedDTO.get(0).getState()))
                .andExpect(jsonPath("$.[1].id").value(binsRequestedDTO.get(1).getId()))
                .andExpect(jsonPath("$.[1].state").value(binsRequestedDTO.get(1).getState()));
    }

    @Test
    public void testGetSmartBinByStateWhenStateIsInvalid() throws Exception {
        when(manageSmartBinsService.filterSmartBinByState(any(String.class))).thenThrow(SmartBinStateInvalidException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/smartbin/state")
                        .param("state","invalidState"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetSmartBinByType() throws Exception {

        List<SmartBin> binsRequested = new ArrayList<>();

        Type mixedType = new Type();
        mixedType.setName("Indifferenziata");

        SmartBin bin1 = new SmartBin();
        bin1.setId("1");
        bin1.setType(mixedType);

        SmartBin bin2 = new SmartBin();
        bin2.setId("2");
        bin2.setType(mixedType);

        binsRequested.add(bin1);
        binsRequested.add(bin2);

        List<SmartBinDTO> binsRequestedDTO = new ArrayList<>();

        SmartBinDTO bin1DTO = new SmartBinDTO();
        bin1DTO.setId(bin1.getId());
        bin1DTO.setType(bin1.getType().getName());

        SmartBinDTO bin2DTO = new SmartBinDTO();
        bin2DTO.setId(bin2.getId());
        bin2DTO.setType(bin2.getType().getName());

        binsRequestedDTO.add(bin1DTO);
        binsRequestedDTO.add(bin2DTO);

        // Configura il comportamento del mock del service
        when(manageSmartBinsService.filterSmartBinByType(any(String.class))).thenReturn(binsRequested);

        when(smartBinMapper.toSmartBinDTO(bin1)).thenReturn(bin1DTO);
        when(smartBinMapper.toSmartBinDTO(bin2)).thenReturn(bin2DTO);

        // Esegue la richiesta GET
        mockMvc.perform(MockMvcRequestBuilders.get("/api/smartbin/type")
                        .param("type","Indifferenziata"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(binsRequestedDTO.get(0).getId()))
                .andExpect(jsonPath("$.[0].type").value(binsRequestedDTO.get(0).getType()))
                .andExpect(jsonPath("$.[1].id").value(binsRequestedDTO.get(1).getId()))
                .andExpect(jsonPath("$.[1].type").value(binsRequestedDTO.get(1).getType()));
    }

    @Test
    public void testGetBinsAboveThreshold() throws Exception {
        List<SmartBin> binsRequested = new ArrayList<>();

        SmartBin bin1 = new SmartBin();
        bin1.setId("1");
        bin1.setCurrentCapacity(new Decimal128(80));
        bin1.setTotalCapacity(new Decimal128(100));

        SmartBin bin2 = new SmartBin();
        bin2.setId("2");
        bin2.setCurrentCapacity(new Decimal128(80));
        bin2.setTotalCapacity(new Decimal128(100));

        binsRequested.add(bin1);
        binsRequested.add(bin2);

        List<SmartBinDTO> binsRequestedDTO = new ArrayList<>();

        SmartBinDTO bin1DTO = new SmartBinDTO();
        bin1DTO.setId(bin1.getId());
        bin1DTO.setCurrentCapacity(bin1.getCurrentCapacity().bigDecimalValue());
        bin1DTO.setTotalCapacity(bin1.getTotalCapacity().bigDecimalValue());

        SmartBinDTO bin2DTO = new SmartBinDTO();
        bin2DTO.setId(bin2.getId());
        bin2DTO.setCurrentCapacity(bin2.getCurrentCapacity().bigDecimalValue());
        bin2DTO.setTotalCapacity(bin2.getTotalCapacity().bigDecimalValue());

        binsRequestedDTO.add(bin1DTO);
        binsRequestedDTO.add(bin2DTO);

        // Configura il comportamento del mock del service
        when(manageSmartBinsService.filterSmartBinByCapacityRatio(any(Double.class))).thenReturn(binsRequested);

        when(smartBinMapper.toSmartBinDTO(bin1)).thenReturn(bin1DTO);
        when(smartBinMapper.toSmartBinDTO(bin2)).thenReturn(bin2DTO);

        // Esegue la richiesta GET
        mockMvc.perform(MockMvcRequestBuilders.get("/api/smartbin/binsAboveThreshold")
                        .param("capacityRatio","0.80"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }
}

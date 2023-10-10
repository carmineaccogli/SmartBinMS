package it.unisalento.pas.smartcitywastemanagement.smartbinms.restcontrollers;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.AllocationRequestViewDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.CleaningPathDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.CleaningPathNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.RequestNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.CleaningPathMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.service.CleaningPathService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CleaningPathRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CleaningPathRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CleaningPathService cleaningPathService;

    @MockBean
    private CleaningPathMapper cleaningPathMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testAddNewCleaningPath() throws Exception {

        CleaningPathDTO cleaningPathDTO = new CleaningPathDTO();
        cleaningPathDTO.setTimestamp(new Date());
        cleaningPathDTO.setSmartBinIDPath(List.of("1","2"));

        CleaningPath cleaningPath = new CleaningPath();
        cleaningPath.setTimestamp(cleaningPathDTO.getTimestamp());
        cleaningPath.setSmartBinIDs(cleaningPathDTO.getSmartBinIDPath());

        String jsonRequest = objectMapper.writeValueAsString(cleaningPathDTO);

        when(cleaningPathMapper.toCleaningPath(any(CleaningPathDTO.class))).thenReturn(cleaningPath);
        when(cleaningPathService.saveCleaningPath(any(CleaningPath.class))).thenReturn("TestID");


        mockMvc.perform(MockMvcRequestBuilders.post("/api/cleaningPath/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Cleaning Path created successfully"))
                .andExpect(jsonPath("$.data").value("TestID"));
    }

    @Test
    public void testGetCleaningPathByStatus() throws Exception {

        List<CleaningPath> mockPaths = new ArrayList<>();

        CleaningPath path1 = new CleaningPath();
        path1.setId("1");
        path1.setDone(true);

        CleaningPath path2 = new CleaningPath();
        path2.setId("2");
        path2.setDone(true);

        mockPaths.add(path1);
        mockPaths.add(path2);


        List<CleaningPathDTO> mockPathsDTO = new ArrayList<>();

        CleaningPathDTO pathDTO1 = new CleaningPathDTO();
        pathDTO1.setId(path1.getId());
        pathDTO1.setDone(path1.isDone());

        CleaningPathDTO pathDTO2 = new CleaningPathDTO();
        pathDTO2.setId(path2.getId());
        pathDTO2.setDone(path2.isDone());

        mockPathsDTO.add(pathDTO1);
        mockPathsDTO.add(pathDTO2);


        when(cleaningPathService.getCleaningPathByStatus(any(boolean.class))).thenReturn(mockPaths);
        when(cleaningPathMapper.toCleaningPathDTO(path1)).thenReturn(pathDTO1);
        when(cleaningPathMapper.toCleaningPathDTO(path2)).thenReturn(pathDTO2);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/cleaningPath/status")
                        .param("done","true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(mockPathsDTO.get(0).getId()))
                .andExpect(jsonPath("$.[0].done").value(mockPathsDTO.get(0).isDone()))
                .andExpect(jsonPath("$.[1].id").value(mockPathsDTO.get(1).getId()))
                .andExpect(jsonPath("$.[1].done").value(mockPathsDTO.get(1).isDone()));
    }

    @Test
    public void testUpdateStatusCleaningPath() throws Exception {

        String cleaningPathID = "TestID";


        mockMvc.perform(MockMvcRequestBuilders.patch("/api/cleaningPath/" + cleaningPathID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    public void testUpdateStatusCleaningPathWhenCleaningPathIsNotFound() throws Exception {

        String cleaningPathID = "TestID";

        doThrow(CleaningPathNotFoundException.class).when(cleaningPathService).updateCleaningPathStatus(any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/cleaningPath/"+cleaningPathID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}


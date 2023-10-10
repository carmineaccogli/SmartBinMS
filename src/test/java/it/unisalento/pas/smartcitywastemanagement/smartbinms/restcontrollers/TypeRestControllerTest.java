package it.unisalento.pas.smartcitywastemanagement.smartbinms.restcontrollers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.TypeDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.TypeMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.security.JwtUtilities;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.service.TypeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TypeRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TypeRestControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TypeMapper typeMapper;

    @MockBean
    private TypeService typeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testAddNewType() throws Exception {

        // Body mocked
        TypeDTO typeDTO = new TypeDTO();
        typeDTO.setId(null);
        typeDTO.setName("TestType");
        typeDTO.setDescription("Test Description");
        typeDTO.setColor("Test Color");

        // Mapping result
        Type type = new Type();
        type.setId(typeDTO.getId());
        type.setName(typeDTO.getName());
        type.setDescription(typeDTO.getDescription());
        type.setColor(typeDTO.getColor());

        when(typeMapper.toType(any(TypeDTO.class))).thenReturn(type);
        when(typeService.saveType(any(Type.class))).thenReturn(new String("TestID"));


        mockMvc.perform(MockMvcRequestBuilders.post("/api/smartbin/type/add")
                        .content(objectMapper.writeValueAsString(typeDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Type added successfully"))
                .andExpect(jsonPath("$.data").value("id:TestID"));

    }


    @Test
    public void testGetAllTypes() throws Exception {

        // Lista mock di types
        List<Type> mockTypes = new ArrayList<>();
        Type type1 = new Type();
        type1.setId("1");
        type1.setName("Type1");
        Type type2 = new Type();
        type2.setId("2");
        type2.setName("Type2");
        mockTypes.add(type1);
        mockTypes.add(type2);

        // Crea una lista fittizia di TypeDTO corrispondente
        List<TypeDTO> mockTypeDTOs = new ArrayList<>();
        TypeDTO typeDTO1 = new TypeDTO();
        typeDTO1.setId("1");
        typeDTO1.setName("Type1");
        TypeDTO typeDTO2 = new TypeDTO();
        typeDTO2.setId("2");
        typeDTO2.setName("Type2");
        mockTypeDTOs.add(typeDTO1);
        mockTypeDTOs.add(typeDTO2);

        // Configura il comportamento del mock del service
        when(typeService.getAllTypeNames()).thenReturn(mockTypes);
        when(typeMapper.toTypeDTO(type1)).thenReturn(typeDTO1);
        when(typeMapper.toTypeDTO(type2)).thenReturn(typeDTO2);

        // Esegue la richiesta GET
        mockMvc.perform(MockMvcRequestBuilders.get("/api/smartbin/type/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(mockTypeDTOs.get(0).getId()))
                .andExpect(jsonPath("$.[1].id").value(mockTypeDTOs.get(1).getId()));
    }






}

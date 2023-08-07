package it.unisalento.pas.smartcitywastemanagement.smartbinms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.utility.MunicipalityBoundaryUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Polygon;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MunicipalityBoundaryUtilsTest {

    private MunicipalityBoundaryUtils boundaryUtils;
    private ObjectMapper objectMapper;
    private Resource boundaryFile;


    @BeforeEach
    public void setUp() {
        boundaryUtils = new MunicipalityBoundaryUtils();
        objectMapper = mock(ObjectMapper.class);
        boundaryFile = mock(Resource.class);
        boundaryUtils.objectMapper = objectMapper;
        boundaryUtils.boundaryFile = boundaryFile;
    }

    @Test
    public void testLoadMunicipalityBoundary_Success() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("{\"coordinates\": [[...]]}".getBytes(StandardCharsets.UTF_8));
        when(boundaryFile.getInputStream()).thenReturn(inputStream);

        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(inputStream)).thenReturn(jsonNode);

        // Implement the rest of the mocking for JsonNode and coordinates as needed
        // ...

        // Act
        Polygon polygon = boundaryUtils.loadMunicipalityBoundary();

        // Assert
        assertNotNull(polygon);
        // Add more assertions based on your expected behavior
    }

    @Test
    public void testLoadMunicipalityBoundary_IOException() throws IOException {
        // Arrange
        when(boundaryFile.getInputStream()).thenThrow(new IOException());

        // Act and Assert
        assertThrows(IOException.class, () -> boundaryUtils.loadMunicipalityBoundary());
    }



}

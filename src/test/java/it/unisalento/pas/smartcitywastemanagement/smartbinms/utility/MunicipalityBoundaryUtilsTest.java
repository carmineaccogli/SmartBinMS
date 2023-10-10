package it.unisalento.pas.smartcitywastemanagement.smartbinms.utility;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.AllocationRequestMapper;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {MunicipalityBoundaryUtils.class})
@ExtendWith(MockitoExtension.class)
public class MunicipalityBoundaryUtilsTest {

    @InjectMocks
    private MunicipalityBoundaryUtils municipalityBoundaryUtils;

    @MockBean
    private Resource boundaryFile;

    @MockBean
    private ObjectMapper objectMapper;



    @Test
    public void testLoadMunicipalityBoundary() throws Exception{

        String jsonContent = "{ \"type\": \"Polygon\"," +
                "  \"coordinates\":" +
                "          [" +
                "            [" +
                "              18.4353593," +
                "              40.0243077" +
                "            ]," +
                "            [" +
                "              18.4336899," +
                "              40.0243738" +
                "            ]," +
                "            [" +
                "              18.4329969," +
                "              40.0242269" +
                "            ]," +
                "[" +
                "              18.4353593," +
                "              40.0243077" +
                "            ]]}";

        InputStream inputStream = IOUtils.toInputStream(jsonContent, "UTF-8");
        when(boundaryFile.getInputStream()).thenReturn(inputStream);
        when(objectMapper.readTree(inputStream)).thenReturn(new ObjectMapper().readTree(jsonContent));

        // Creare Coordinate per il confine atteso
        Coordinate[] expectedCoordinates = new Coordinate[]{
                new Coordinate(18.4353593, 40.0243077),
                new Coordinate(18.4336899, 40.0243738),
                new Coordinate(18.4329969, 40.0242269),
                new Coordinate(18.4353593, 40.0243077)
        };

        // Creare il Polygon atteso
        Polygon expectedPolygon = new GeometryFactory().createPolygon(expectedCoordinates);

        // Configurare ObjectMapper per restituire il JSON atteso


        // Eseguire il metodo da testare
        Polygon result = municipalityBoundaryUtils.loadMunicipalityBoundary();

        // Verificare che il Polygon restituito sia uguale a quello atteso
        assertEquals(expectedPolygon, result);

    }
}

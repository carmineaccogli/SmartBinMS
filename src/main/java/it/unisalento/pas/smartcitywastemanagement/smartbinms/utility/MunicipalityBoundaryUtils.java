package it.unisalento.pas.smartcitywastemanagement.smartbinms.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class MunicipalityBoundaryUtils {

    @Value("classpath:data/municipality_boundary.json")
    public Resource boundaryFile;

    @Autowired
    public ObjectMapper objectMapper;

    public Polygon loadMunicipalityBoundary() throws IOException {

        JsonNode jsonNode = objectMapper.readTree(boundaryFile.getInputStream());
        JsonNode coordinatesNode = jsonNode.get("coordinates");

        Coordinate[] coordinates = convertJsonToCoordinates(coordinatesNode);

        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing linearRing = geometryFactory.createLinearRing(coordinates);

        // Crea un oggetto Polygon dal LinearRing
        return geometryFactory.createPolygon(linearRing, null);
    }

    private Coordinate[] convertJsonToCoordinates(JsonNode coordinatesNode) {
        List<Coordinate> coordinateList = new ArrayList<>();

        for (JsonNode coordNode : coordinatesNode) {
            double x = coordNode.get(0).asDouble();
            double y = coordNode.get(1).asDouble();
            coordinateList.add(new Coordinate(x, y));
        }

        return coordinateList.toArray(new Coordinate[0]);
    }


}

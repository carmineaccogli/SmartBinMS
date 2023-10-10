package it.unisalento.pas.smartcitywastemanagement.smartbinms.validators;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.AllocationRequestMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.utility.MunicipalityBoundaryUtils;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ValidGeoJSONPointValidator.class})
@ExtendWith(MockitoExtension.class)
public class ValidGeoJSONPointValidatorTest {

    @MockBean
    private MunicipalityBoundaryUtils municipalityBoundaryUtils;

    @InjectMocks
    private ValidGeoJSONPointValidator validator;

    @MockBean
    private ConstraintValidatorContext constraintValidatorContext;



    @Test
    public void testValidGeoJSONPoint() throws Exception{
        GeoJsonPoint validPoint = new GeoJsonPoint(18.4249606, 40.0089752);

        GeometryFactory geometryFactory = new GeometryFactory();

        Polygon mockPolygon = geometryFactory.createPolygon(createLinearLingForPolygon());
        when(municipalityBoundaryUtils.loadMunicipalityBoundary()).thenReturn(mockPolygon);

       boolean result = validator.isValid(validPoint,constraintValidatorContext);
       assertTrue(result);
    }

    @Test
    public void testInvalidGeoJSONPoint_OutsideMunicipality() throws Exception{
        GeoJsonPoint invalidPoint = new GeoJsonPoint(18.4273102, 40.0108004);

        GeometryFactory geometryFactory = new GeometryFactory();

        Polygon mockPolygon = geometryFactory.createPolygon(createLinearLingForPolygon());
        when(municipalityBoundaryUtils.loadMunicipalityBoundary()).thenReturn(mockPolygon);

        boolean result = validator.isValid(invalidPoint,constraintValidatorContext);
        assertFalse(result);
    }

    private LinearRing createLinearLingForPolygon() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinates = new Coordinate[] {
                new Coordinate(18.4235981, 40.0098467),
                new Coordinate(18.4236410, 40.0081447),
                new Coordinate(18.4264626, 40.0078323),
                new Coordinate(18.4260979, 40.0098549),
                new Coordinate(18.4235981, 40.0098467)
        };

        return geometryFactory.createLinearRing(coordinates);
    }
}

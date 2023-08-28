package it.unisalento.pas.smartcitywastemanagement.smartbinms.validators;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.InvalidPositionException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.utility.MunicipalityBoundaryUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.locationtech.jts.geom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.io.IOException;


public class ValidGeoJSONPointValidator implements ConstraintValidator<ValidGeoJSONPoint, GeoJsonPoint> {


    @Autowired
    private MunicipalityBoundaryUtils municipalityBoundaryUtils;



    @Override
    public boolean isValid(GeoJsonPoint value, ConstraintValidatorContext context) {

        if(value == null)
            return false;

        double x = value.getX();
        double y = value.getY();

        if(x < -90 || x > 90 || y < -180 || y > 180) {
            return false;
        }

        Polygon municipalityPolygon = loadPolygon();
        Point point = convertToJTSPoint(value);
        return municipalityPolygon.contains(point);

    }

    private Polygon loadPolygon() {

        Polygon municipalityPoligon = null;
        try {
            municipalityPoligon = municipalityBoundaryUtils.loadMunicipalityBoundary();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return municipalityPoligon;

    }

    private Point convertToJTSPoint(GeoJsonPoint value) {

        double x = value.getX();
        double y = value.getY();

        GeometryFactory factory = new GeometryFactory();
        return factory.createPoint(new Coordinate(x,y));
    }


}

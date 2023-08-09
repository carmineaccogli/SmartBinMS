package it.unisalento.pas.smartcitywastemanagement.smartbinms.dto;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.validators.ValidGeoJSONPoint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.Date;

public class AllocationRequestSendDTO {


    @NotBlank
    private String smartBin_name;

    @NotNull
    @ValidGeoJSONPoint(message = "The point is outside the municipality boundaries")
    private GeoJsonPoint position;

    @NotNull
    @Min(value=1)
    private Float totalCapacity;

    @NotBlank
    private String type;


    public GeoJsonPoint getPosition() {
        return position;
    }

    public void setPosition(GeoJsonPoint position) {
        this.position = position;
    }

    public Float getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Float totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getSmartBin_name() {
        return smartBin_name;
    }

    public void setSmartBin_name(String smartBin_name) {
        this.smartBin_name = smartBin_name;
    }
}

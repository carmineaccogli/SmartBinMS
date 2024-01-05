package it.unisalento.pas.smartcitywastemanagement.smartbinms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.utility.GeoJsonDeserializer;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.validators.ValidGeoJSONPoint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.math.BigDecimal;
import java.util.Date;

public class AllocationRequestSendDTO {


    @NotBlank(message = "{NotBlank.AllocationRequest.smartBinName}")
    private String smartBinName;


    @JsonDeserialize(using= GeoJsonDeserializer.class)
    @ValidGeoJSONPoint(message = "{ValidGeoJSONPoint.AllocationRequest.position}")
    private GeoJsonPoint position;

    @NotNull(message = "{NotNull.AllocationRequest.totalCapacity}")
    @Min(value=1, message = "{Min.AllocationRequest.totalCapacity}")
    private BigDecimal totalCapacity;

    @NotBlank(message = "{NotBlank.AllocationRequest.type}")
    private String type;


    public GeoJsonPoint getPosition() {
        return position;
    }

    public void setPosition(GeoJsonPoint position) {
        this.position = position;
    }

    public BigDecimal getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(BigDecimal totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getSmartBinName() {
        return smartBinName;
    }

    public void setSmartBinName(String smartBinName) {
        this.smartBinName = smartBinName;
    }
}

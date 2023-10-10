package it.unisalento.pas.smartcitywastemanagement.smartbinms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.utility.GeoJsonDeserializer;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.utility.GeoJsonSerializer;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.math.BigDecimal;
import java.util.Locale;

public class SmartBinDTO {

    private String id;
    private String name;

    @JsonDeserialize(using = GeoJsonDeserializer.class)
    @JsonSerialize(using = GeoJsonSerializer.class)
    private GeoJsonPoint position;
    private String state;
    private String type;


    private BigDecimal totalCapacity;


    private BigDecimal currentCapacity;

    private String capacityThreshold;


    public GeoJsonPoint getPosition() {
        return position;
    }

    public void setPosition(GeoJsonPoint position) {
        this.position = position;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(BigDecimal totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public BigDecimal getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(BigDecimal currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCapacityThreshold() {
        return capacityThreshold;
    }

    public void setCapacityThreshold(Float capacityThreshold) {
        this.capacityThreshold = String.format(Locale.US,"%.2f",capacityThreshold);
    }
}

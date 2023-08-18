package it.unisalento.pas.smartcitywastemanagement.smartbinms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.utility.GeoJsonDeserializer;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.utility.GeoJsonSerializer;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public class SmartBinDTO {

    private String id;
    private String name;

    @JsonDeserialize(using = GeoJsonDeserializer.class)
    @JsonSerialize(using = GeoJsonSerializer.class)
    private GeoJsonPoint position;
    private String state;
    private String type;

    @JsonFormat(pattern = "#.##")
    private Float totalCapacity;

    @JsonFormat(pattern = "#.##")
    private Float currentCapacity;


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

    public Float getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Float totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Float getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(Float currentCapacity) {
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
}

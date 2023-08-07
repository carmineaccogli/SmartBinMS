package it.unisalento.pas.smartcitywastemanagement.smartbinms.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document("smartBin")
public class SmartBin {


    @Id
    private String id;

    private String name;

    private GeoJsonPoint position;

    private State state;

    @DBRef
    private Type type;

    private Float totalCapacity;

    private Float currentCapacity;


    public enum State {
        ALLOCATED,DEALLOCATED
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Float getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(Float currentCapacity) {
        this.currentCapacity = currentCapacity;
    }


    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


package it.unisalento.pas.smartcitywastemanagement.smartbinms.domain;

import org.bson.types.Decimal128;
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

    private Decimal128 totalCapacity;

    private Decimal128 currentCapacity;

    private Float capacityThreshold;


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



    public Decimal128 getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Decimal128 totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Decimal128 getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(Decimal128 currentCapacity) {
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

    public Float getCapacityThreshold() {
        return capacityThreshold;
    }

    public void setCapacityThreshold(Float capacityThreshold) {
        this.capacityThreshold = capacityThreshold;
    }
}


package it.unisalento.pas.smartcitywastemanagement.smartbinms.domain;


import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("allocationRequest")
public class AllocationRequest {

    @Id
    private String id;

    private String smartBinName;

    private GeoJsonPoint position;

    private Status status;

    private Date requestedDate;

    private Date decisionDate;

    private Decimal128 totalCapacity;

    @DBRef
    private Type type;



    public enum Status {
        PENDING,ACCEPTED,REJECTED
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public Decimal128 getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Decimal128 totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getSmartBinName() {
        return smartBinName;
    }

    public void setSmartBinName(String smartBinName) {
        this.smartBinName = smartBinName;
    }
}

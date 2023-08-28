package it.unisalento.pas.smartcitywastemanagement.smartbinms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.utility.GeoJsonDeserializer;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.utility.GeoJsonSerializer;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;


import java.math.BigDecimal;
import java.util.Date;

public class AllocationRequestViewDTO {


    private String id;
    private String smartBinName;

    @JsonDeserialize(using = GeoJsonDeserializer.class)
    @JsonSerialize(using = GeoJsonSerializer.class)
    private GeoJsonPoint position;

    private String status;

    private Date requestedDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date decisionDate;


    private BigDecimal totalCapacity;

    private String type;


    public String getSmartBinName() {
        return smartBinName;
    }

    public void setSmartBinName(String smartBinName) {
        this.smartBinName = smartBinName;
    }

    public GeoJsonPoint getPosition() {
        return position;
    }

    public void setPosition(GeoJsonPoint position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

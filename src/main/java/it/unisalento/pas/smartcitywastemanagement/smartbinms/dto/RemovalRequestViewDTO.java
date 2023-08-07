package it.unisalento.pas.smartcitywastemanagement.smartbinms.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

public class RemovalRequestViewDTO {


    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date decisionDate;

    private Date requestedDate;

    private String smartBin_id;

    private String status;


    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getSmartBin_id() {
        return smartBin_id;
    }

    public void setSmartBin_id(String smartBin_id) {
        this.smartBin_id = smartBin_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

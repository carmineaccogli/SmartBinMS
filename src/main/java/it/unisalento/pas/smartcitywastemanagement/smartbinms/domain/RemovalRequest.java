package it.unisalento.pas.smartcitywastemanagement.smartbinms.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("removalRequest")
public class RemovalRequest {


    @Id
    private String id;

    private Date decisionDate;

    private Date requestedDate;

    private String smartBin_id;

    private Status status;



    public enum Status {
        APPROVED, REJECTED, PENDING
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

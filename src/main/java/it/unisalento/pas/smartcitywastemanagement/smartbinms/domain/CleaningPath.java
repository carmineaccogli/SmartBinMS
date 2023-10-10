package it.unisalento.pas.smartcitywastemanagement.smartbinms.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("cleaningPath")
public class CleaningPath {

    @Id
    private String id;

    private boolean done;

    private List<String> smartBinIDs;

    private Date timestamp;


    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public List<String> getSmartBinIDs() {
        return smartBinIDs;
    }

    public void setSmartBinIDs(List<String> smartBinIDs) {
        this.smartBinIDs = smartBinIDs;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

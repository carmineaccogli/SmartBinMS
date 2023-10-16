package it.unisalento.pas.smartcitywastemanagement.smartbinms.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

public class CleaningPathDTO {

    private String Id;

    private boolean done;

    @NotNull(message = "{NotNull.CleaningPath.smartBinIDPath}")
    @Size(min = 1, message = "{Size.CleaningPath.smartBinIDPath}")
    private List<String> smartBinIDPath;

    @NotNull(message = "{NotNull.CleaningPath.scheduledDate}")
    private Date scheduledDate;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public List<String> getSmartBinIDPath() {
        return smartBinIDPath;
    }

    public void setSmartBinIDPath(List<String> smartBinIDPath) {
        this.smartBinIDPath = smartBinIDPath;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}

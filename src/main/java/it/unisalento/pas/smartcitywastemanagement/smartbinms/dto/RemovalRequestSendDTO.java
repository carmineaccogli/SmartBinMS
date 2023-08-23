package it.unisalento.pas.smartcitywastemanagement.smartbinms.dto;

import jakarta.validation.constraints.NotBlank;

public class RemovalRequestSendDTO {

    @NotBlank(message = "{NotBlank.RemovalRequest.smartBin_id}")
    private String smartBinID;


    public String getSmartBinID() {
        return smartBinID;
    }

    public void setSmartBinID(String smartBinID) {
        this.smartBinID = smartBinID;
    }
}

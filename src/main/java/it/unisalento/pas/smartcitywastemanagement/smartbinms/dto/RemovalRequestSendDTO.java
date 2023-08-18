package it.unisalento.pas.smartcitywastemanagement.smartbinms.dto;

import jakarta.validation.constraints.NotBlank;

public class RemovalRequestSendDTO {

    @NotBlank(message = "{NotBlank.RemovalRequest.smartBin_id}")
    private String smartBin_id;



    public String getSmartBin_id() {
        return smartBin_id;
    }

    public void setSmartBin_id(String smartBin_id) {
        this.smartBin_id = smartBin_id;
    }
}

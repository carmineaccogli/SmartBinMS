package it.unisalento.pas.smartcitywastemanagement.smartbinms.rabbitMQMessages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SmartBinUpdateMessage {

    @NotBlank(message ="smartBinID required")
    private String smartBinID;

    @NotNull(message ="amount required")
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal amount;


    public String getSmartBinID() {
        return smartBinID;
    }

    public void setSmartBinID(String smartBinID) {
        this.smartBinID = smartBinID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }



}

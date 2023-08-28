package it.unisalento.pas.smartcitywastemanagement.smartbinms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class DisposalRequestDTO {

    @NotNull(message = "{NotNull.DisposalRequest.amount}")
    @Positive(message="{Positive.DisposalRequest.amount}")
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

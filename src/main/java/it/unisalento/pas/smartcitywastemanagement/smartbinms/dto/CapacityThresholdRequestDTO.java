package it.unisalento.pas.smartcitywastemanagement.smartbinms.dto;

import jakarta.validation.constraints.*;

public class CapacityThresholdRequestDTO {


    @Positive
    @DecimalMax("1")
    @NotNull(message ="{NotNull.CapacityThresholdRequest.capacityThreshold}")
    private Float capacityThreshold;

    public Float getCapacityThreshold() {
        return capacityThreshold;
    }

    public void setCapacityThreshold(Float capacityThreshold) {
        this.capacityThreshold = capacityThreshold;
    }
}

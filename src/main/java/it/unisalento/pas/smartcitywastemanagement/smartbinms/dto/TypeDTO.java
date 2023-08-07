package it.unisalento.pas.smartcitywastemanagement.smartbinms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.index.Indexed;

public class TypeDTO {

    @NotNull
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

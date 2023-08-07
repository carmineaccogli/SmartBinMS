package it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.TypeDTO;
import org.springframework.stereotype.Component;

@Component
public class TypeMapper {

    public Type toType(TypeDTO typeDTO) {
        Type newType = new Type();
        newType.setName(typeDTO.getName());
        newType.setDescription(typeDTO.getDescription());
        return newType;
    }

    public TypeDTO toTypeDTO(Type type) {
        TypeDTO typeDTO= new TypeDTO();

        typeDTO.setName(type.getName());
        typeDTO.setDescription(type.getDescription());

        return typeDTO;
    }
}

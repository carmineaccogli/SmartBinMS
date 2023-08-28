package it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.AllocationRequestSendDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.AllocationRequestViewDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinTypeNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.TypeRepository;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AllocationRequestMapper {

    @Autowired
    private TypeRepository typeRepository;


    public AllocationRequest toAllocationRequest(AllocationRequestSendDTO allocationRequestSendDTO) throws SmartBinTypeNotFoundException {

        AllocationRequest allocationRequest = new AllocationRequest();

        allocationRequest.setSmartBinName(allocationRequestSendDTO.getSmartBinName());
        allocationRequest.setPosition(allocationRequestSendDTO.getPosition());
        allocationRequest.setTotalCapacity(new Decimal128(allocationRequestSendDTO.getTotalCapacity()));

        // Ricerca oggetto type
        Optional<Type> type = typeRepository.findByTypeName(allocationRequestSendDTO.getType());
        if(type.isPresent())
            allocationRequest.setType(type.get());
        else
            throw new SmartBinTypeNotFoundException();


        return allocationRequest;
    }


    public AllocationRequestViewDTO toAllocationRequestDTO(AllocationRequest allocationRequest) {

        AllocationRequestViewDTO allocationRequestViewDTO = new AllocationRequestViewDTO();

        allocationRequestViewDTO.setSmartBinName(allocationRequest.getSmartBinName());
        allocationRequestViewDTO.setStatus(allocationRequest.getStatus().toString());
        allocationRequestViewDTO.setType(allocationRequest.getType().getName());
        allocationRequestViewDTO.setPosition(allocationRequest.getPosition());
        allocationRequestViewDTO.setTotalCapacity(allocationRequest.getTotalCapacity().bigDecimalValue());
        allocationRequestViewDTO.setRequestedDate(allocationRequest.getRequestedDate());
        allocationRequestViewDTO.setDecisionDate(allocationRequest.getDecisionDate());
        allocationRequestViewDTO.setId(allocationRequest.getId());

        return allocationRequestViewDTO;

    }
}

package it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.RemovalRequestSendDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.RemovalRequestViewDTO;
import org.springframework.stereotype.Component;

@Component
public class RemovalRequestMapper {

    public RemovalRequest toRemovalRequest(RemovalRequestSendDTO removalRequestSendDTO){

        RemovalRequest removalRequest = new RemovalRequest();
        removalRequest.setSmartBin_id(removalRequestSendDTO.getSmartBin_id());
        return removalRequest;
    }

    public RemovalRequestViewDTO toRemovalRequestDTO(RemovalRequest removalRequest) {

        RemovalRequestViewDTO removalRequestViewDTO = new RemovalRequestViewDTO();

        removalRequestViewDTO.setSmartBin_id(removalRequest.getSmartBin_id());
        removalRequestViewDTO.setStatus(removalRequest.getStatus().toString());
        removalRequestViewDTO.setRequestedDate(removalRequest.getRequestedDate());
        removalRequestViewDTO.setDecisionDate(removalRequest.getDecisionDate());
        removalRequestViewDTO.setId(removalRequest.getId());

        return removalRequestViewDTO;
    }
}

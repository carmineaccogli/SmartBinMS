package it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.RemovalRequestSendDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.RemovalRequestViewDTO;
import org.springframework.stereotype.Component;

@Component
public class RemovalRequestMapper {

    public RemovalRequest toRemovalRequest(RemovalRequestSendDTO removalRequestSendDTO){

        RemovalRequest removalRequest = new RemovalRequest();
        removalRequest.setSmartBinID(removalRequestSendDTO.getSmartBinID());
        return removalRequest;
    }

    public RemovalRequestViewDTO toRemovalRequestDTO(RemovalRequest removalRequest) {

        RemovalRequestViewDTO removalRequestViewDTO = new RemovalRequestViewDTO();

        removalRequestViewDTO.setSmartBinID(removalRequest.getSmartBinID());
        removalRequestViewDTO.setStatus(removalRequest.getStatus().toString());
        removalRequestViewDTO.setRequestedDate(removalRequest.getRequestedDate());
        removalRequestViewDTO.setDecisionDate(removalRequest.getDecisionDate());
        removalRequestViewDTO.setId(removalRequest.getId());

        return removalRequestViewDTO;
    }
}

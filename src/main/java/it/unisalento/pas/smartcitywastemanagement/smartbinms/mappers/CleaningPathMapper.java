package it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.CleaningPathDTO;
import org.springframework.stereotype.Component;

@Component
public class CleaningPathMapper {

    public CleaningPath toCleaningPath(CleaningPathDTO cleaningPathDTO) {

        CleaningPath cleaningPath = new CleaningPath();

        cleaningPath.setTimestamp(cleaningPathDTO.getTimestamp());
        cleaningPath.setSmartBinIDs(cleaningPathDTO.getSmartBinIDPath());
        return cleaningPath;
    }

    public CleaningPathDTO toCleaningPathDTO(CleaningPath cleaningPath) {

        CleaningPathDTO cleaningPathDTO = new CleaningPathDTO();

        cleaningPathDTO.setId(cleaningPath.getId());
        cleaningPathDTO.setSmartBinIDPath(cleaningPath.getSmartBinIDs());
        cleaningPathDTO.setTimestamp(cleaningPath.getTimestamp());
        cleaningPathDTO.setDone(cleaningPath.isDone());

        return cleaningPathDTO;
    }
}

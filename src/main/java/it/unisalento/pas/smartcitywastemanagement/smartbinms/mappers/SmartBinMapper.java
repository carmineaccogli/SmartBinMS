package it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.SmartBinDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SmartBinMapper {


    public SmartBinDTO toSmartBinDTO(SmartBin smartBin) {

        SmartBinDTO smartBinDTO = new SmartBinDTO();

        smartBinDTO.setName(smartBin.getName());
        smartBinDTO.setState(smartBin.getState().toString());
        smartBinDTO.setPosition(smartBin.getPosition());

        smartBinDTO.setType(smartBin.getType().getName());
        smartBinDTO.setCurrentCapacity(smartBin.getCurrentCapacity());
        smartBinDTO.setTotalCapacity(smartBin.getTotalCapacity());
        smartBinDTO.setId(smartBin.getId());

        return smartBinDTO;
    }
}

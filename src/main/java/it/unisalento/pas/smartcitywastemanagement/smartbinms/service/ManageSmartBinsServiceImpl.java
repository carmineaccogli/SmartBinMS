package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.*;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.SmartBinRepository;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManageSmartBinsServiceImpl implements ManageSmartBinsService{

    @Autowired
    SmartBinRepository smartBinRepository;


    @Autowired
    TypeRepository typeRepository;

    public List<SmartBin> filterSmartBinByState(String state) throws SmartBinStateInvalidException {

        if(!validState(state))
            throw new SmartBinStateInvalidException();

        return smartBinRepository.findByState(state);
    }

    public List<SmartBin> filterSmartBinByType(String type) throws SmartBinTypeNotFoundException {

        Type filteringType = null;

        // Check esistenza stato
        Optional<Type> ty = typeRepository.findByTypeName(type);
        if(ty.isPresent())
            filteringType = ty.get();
        else
            throw new SmartBinTypeNotFoundException();

        return smartBinRepository.findByType(filteringType.getName());
    }



    public List<SmartBin> filterSmartBinByCapacityRatio(double capacityRatio)  {

        return smartBinRepository.findyByCapacityRatio(capacityRatio);
    }



    public List<SmartBin> getAllSmartBins() {

        return smartBinRepository.findAll();
    }

    public SmartBin getSmartBinByID(String smartBinID) throws SmartBinNotFoundException {

        SmartBin binRequested = null;

        // Controllo l'esistenza del bin richiesto
        Optional<SmartBin> smartBin = smartBinRepository.findById(smartBinID);
        if(!smartBin.isPresent())
            throw new SmartBinNotFoundException();

        binRequested = smartBin.get();

        return binRequested;

        }





    private boolean validState(String stateToCheck) {
        try {
            Enum.valueOf(SmartBin.State.class, stateToCheck.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }

    }
}






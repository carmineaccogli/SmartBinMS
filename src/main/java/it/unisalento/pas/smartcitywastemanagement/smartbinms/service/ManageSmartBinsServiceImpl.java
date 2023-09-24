package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.*;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.SmartBinRepository;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.TypeRepository;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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


    public void manageDisposalRequest(String smartBinID, BigDecimal disposalAmount) throws SmartBinNotFoundException, SmartBinIsFullException {

        // Check smartBinID
        SmartBin binRequested = null;

        // Controllo l'esistenza del bin richiesto
        Optional<SmartBin> smartBin = smartBinRepository.findById(smartBinID);
        if(!smartBin.isPresent())
            throw new SmartBinNotFoundException();


        binRequested = smartBin.get();

        // Setting scala valore in ingresso se supera la sensibilità di 1 gr
        if(disposalAmount.scale() > 4) {
            disposalAmount = disposalAmount.setScale(4, RoundingMode.HALF_UP);
        }

        /*// Check capacity
        boolean canPerform = checkPerformDisposal(binRequested,disposalAmount);

        if(!canPerform)
            throw new SmartBinIsFullException();*/

        // Update in caso di successo
        binRequested.setCurrentCapacity(new Decimal128(binRequested.getCurrentCapacity().bigDecimalValue().add(disposalAmount)));
        smartBinRepository.save(binRequested);
    }

    public void updateCapacityThreshold(String smartBinID, Float newCapacity) throws SmartBinNotFoundException {

        // Check smartBinID
        SmartBin binRequested = null;

        // Controllo l'esistenza del bin richiesto
        Optional<SmartBin> smartBin = smartBinRepository.findById(smartBinID);
        if(!smartBin.isPresent())
            throw new SmartBinNotFoundException();

        binRequested = smartBin.get();

        binRequested.setCapacityThreshold(newCapacity);
        smartBinRepository.save(binRequested);
    }

    public void resetCapacity(String smartBinID) throws SmartBinNotFoundException {
        SmartBin binRequested = null;

        // Controllo l'esistenza del bin richiesto
        Optional<SmartBin> smartBin = smartBinRepository.findById(smartBinID);
        if(!smartBin.isPresent())
            throw new SmartBinNotFoundException();

        binRequested = smartBin.get();

        binRequested.setCurrentCapacity(new Decimal128(0));
        smartBinRepository.save(binRequested);
    }






    private boolean validState(String stateToCheck) {
        try {
            Enum.valueOf(SmartBin.State.class, stateToCheck.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }

    }

    private boolean checkPerformDisposal(SmartBin bin, BigDecimal disposalAmount){

        BigDecimal newCurrentCapacity = bin.getCurrentCapacity().bigDecimalValue().add(disposalAmount);

        if(newCurrentCapacity.compareTo(bin.getTotalCapacity().bigDecimalValue()) > 0)
            return false;

        return true;
    }
}






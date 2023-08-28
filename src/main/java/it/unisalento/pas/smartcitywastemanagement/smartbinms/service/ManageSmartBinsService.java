package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinIsFullException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinStateInvalidException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinTypeNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface ManageSmartBinsService {



    List<SmartBin> getAllSmartBins();

    SmartBin getSmartBinByID(String smartBinID) throws SmartBinNotFoundException;

    List<SmartBin> filterSmartBinByState(String state) throws SmartBinStateInvalidException;

    List<SmartBin> filterSmartBinByType(String type) throws SmartBinTypeNotFoundException;

    List<SmartBin> filterSmartBinByCapacityRatio(double capacityThreshold);

    void manageDisposalRequest(String smartBinID, BigDecimal disposalAmount) throws SmartBinNotFoundException, SmartBinIsFullException;

}

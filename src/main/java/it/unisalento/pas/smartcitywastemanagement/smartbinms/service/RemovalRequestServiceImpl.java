package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.*;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.RemovalRequestRepository;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.SmartBinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RemovalRequestServiceImpl implements RemovalRequestService {

    @Autowired
    private SmartBinRepository smartBinRepository;

    @Autowired
    private RemovalRequestRepository removalRequestRepository;

    public String saveRemovalRequest(RemovalRequest removalRequest) throws SmartBinNotFoundException,RequestAlreadyExistsException {

        /* C1: STEP DI VALIDAZIONE DI BUSINESS
        Controllo che non esista una richiesta con status=PENDING relativa allo stesso smartBin_ID
        */
        Boolean requestExists = removalRequestRepository.existsBySmartBinIDAndStatus(removalRequest.getSmartBinID(), RemovalRequest.Status.PENDING);
        if(requestExists)
            throw new RequestAlreadyExistsException();

        // C2: Controlliamo se esiste lo smart bin apposito
        Optional<SmartBin> smartBin = smartBinRepository.findById(removalRequest.getSmartBinID());
        if (!smartBin.isPresent())
            throw new SmartBinNotFoundException();

        // CONTROLLI SUPERATI
        removalRequest.setRequestedDate(new Date());
        removalRequest.setStatus(RemovalRequest.Status.PENDING);

        // Salvataggio della richiesta
        RemovalRequest createdRequest = removalRequestRepository.save(removalRequest);

        return createdRequest.getId();
    }

    public void manageRemovalRequest(String requestID, String result) throws RequestNotFoundException, SmartBinNotFoundException, RequestAlreadyConfirmedException, SmartBinAlreadyRemovedException {

        // C1: Controllo che la richiesta esista
        RemovalRequest removalRequest = checkRequestExistence(requestID);

        // C2: Controllo che lo status sia in PENDING
        checkRequestValidity(removalRequest);

        // RICHIESTA NON APPROVATA
        if (result.equals("Disapprove")) {
            removalRequest.setStatus(RemovalRequest.Status.REJECTED);
            removalRequest.setDecisionDate(new Date());
            removalRequestRepository.save(removalRequest);
            return;
        }

        // Richiesta APPROVATA
        removalRequest.setStatus(RemovalRequest.Status.APPROVED);
        removalRequest.setDecisionDate(new Date());
        removalRequestRepository.save(removalRequest);

        // C3: Controllo che l'id dello smartBin indicato esista
        SmartBin smartBin = null;
        Optional<SmartBin> optSmartBin = smartBinRepository.findById(removalRequest.getSmartBinID());
        if (!optSmartBin.isPresent())
            throw new SmartBinNotFoundException();

        smartBin = optSmartBin.get();

        // C4: Controllo che lo smartBin indicato non sia stato già rimosso
        if(smartBin.getState() == SmartBin.State.DEALLOCATED) {

            // Cambiamento stato richiesta in quanto è stata negata
            removalRequest.setStatus(RemovalRequest.Status.REJECTED);
            removalRequest.setDecisionDate(new Date());
            removalRequestRepository.save(removalRequest);

            throw new SmartBinAlreadyRemovedException();
        }

        smartBin.setState(SmartBin.State.DEALLOCATED);
        smartBinRepository.save(smartBin);
    }


    public List<RemovalRequest> getAllRequests() {

        return removalRequestRepository.findAll();
    }


    public RemovalRequest getRequestByID(String requestID) throws RequestNotFoundException{

        return checkRequestExistence(requestID);
    }

    public List<RemovalRequest> getRequestByStatus(String status) throws RequestInvalidStatusException {

        if(!validStatus(status))
            throw new RequestInvalidStatusException();

        return removalRequestRepository.findByStatus(status);
    }



    private boolean validStatus(String statusToCheck) {
        try {
            Enum.valueOf(RemovalRequest.Status.class, statusToCheck.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void checkRequestValidity(RemovalRequest removalRequest) throws RequestAlreadyConfirmedException {

        if(removalRequest.getStatus() != RemovalRequest.Status.PENDING)
            throw new RequestAlreadyConfirmedException();
    }

    private RemovalRequest checkRequestExistence(String requestID) throws RequestNotFoundException {

        // Controllo l'esistenza della richiesta di allocazione
        Optional<RemovalRequest> request = removalRequestRepository.findById(requestID);
        if(!request.isPresent())
            throw new RequestNotFoundException();

        return request.get();
    }

}

package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.AllocationRequestNotFound;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.RemovalRequestNotFound;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.RequestInvalidStatusException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinNotFoundException;
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

    public String saveRemovalRequest(RemovalRequest removalRequest) throws SmartBinNotFoundException {

        // Controlliamo se esiste lo smart bin apposito
        Optional<SmartBin> smartBin = smartBinRepository.findById(removalRequest.getSmartBin_id());
        if (!smartBin.isPresent())
            throw new SmartBinNotFoundException();

        removalRequest.setRequestedDate(new Date());
        removalRequest.setStatus(RemovalRequest.Status.PENDING);

        // Salvataggio della richiesta
        RemovalRequest createdRequest = removalRequestRepository.save(removalRequest);
        return createdRequest.getId();
    }

    public void manageRemovalRequest(String requestID, String result) throws RemovalRequestNotFound, SmartBinNotFoundException {

        RemovalRequest removalRequest = null;

        // Controllo l'esistenza della richiesta di allocazione
        Optional<RemovalRequest> request = removalRequestRepository.findById(requestID);
        if (!request.isPresent())
            throw new RemovalRequestNotFound();

        removalRequest = request.get();

        // Richiesta NON APPROVATA
        if (result.equals("Disapprove")) {
            removalRequest.setStatus(RemovalRequest.Status.REJECTED);
            removalRequestRepository.save(removalRequest);
            return;
        }

        // Richiesta APPROVATA
        removalRequest.setStatus(RemovalRequest.Status.APPROVED);
        removalRequestRepository.save(removalRequest);

        Optional<SmartBin> smartBin = smartBinRepository.findById(removalRequest.getSmartBin_id());
        if (!smartBin.isPresent())
            throw new SmartBinNotFoundException();

        smartBin.get().setState(SmartBin.State.DEALLOCATED);
        smartBinRepository.save(smartBin.get());
    }


    public List<RemovalRequest> getAllRequests() {

        return removalRequestRepository.findAll();
    }


    public RemovalRequest getRequestByID(String requestID) throws RemovalRequestNotFound {

        RemovalRequest removalRequest = null;

        // Controllo l'esistenza della richiesta di allocazione
        Optional<RemovalRequest> request = removalRequestRepository.findById(requestID);
        if(!request.isPresent())
            throw new RemovalRequestNotFound();

        removalRequest = request.get();

        return removalRequest;

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

}

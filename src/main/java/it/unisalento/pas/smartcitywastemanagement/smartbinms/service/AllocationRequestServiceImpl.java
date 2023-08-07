package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.AllocationRequestNotFound;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.RequestInvalidStatusException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinAlreadyAllocatedException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.AllocationRequestRepository;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.SmartBinRepository;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AllocationRequestServiceImpl implements AllocationRequestService{

    @Autowired
    SmartBinRepository smartBinRepository;


    @Autowired
    TypeRepository typeRepository;

    @Autowired
    AllocationRequestRepository allocationRequestRepository;


    public String saveAllocationRequest(AllocationRequest allocationRequest) throws SmartBinAlreadyAllocatedException{

        /* STEP DI VALIDAZIONE DI BUSINESS
        Controllo che la posizione richiesta non sia già occupata da uno smartBin allocato dello stesso tipo
        */
        List<SmartBin> smartBinList = smartBinRepository.findByPositionAndState(allocationRequest.getPosition(), SmartBin.State.ALLOCATED.toString());
        boolean alreadyAllocated = isTypePresent(smartBinList, allocationRequest.getType().getName());
        if (alreadyAllocated) {
            throw new SmartBinAlreadyAllocatedException();
        }

        // Inseriamo i parametri rimanenti della richiesta
        allocationRequest.setStatus(AllocationRequest.Status.PENDING);
        allocationRequest.setRequestedDate(new Date());

        // Salvataggio della richiesta
        AllocationRequest createdRequest = allocationRequestRepository.save(allocationRequest);
        return createdRequest.getId();
    }



    public void manageAllocationRequest(String requestID, String result) throws AllocationRequestNotFound {

        AllocationRequest allocationRequest = null;

        // Controllo l'esistenza della richiesta di allocazione
        Optional<AllocationRequest> request = allocationRequestRepository.findById(requestID);
        if(!request.isPresent())
            throw new AllocationRequestNotFound();

        allocationRequest = request.get();

        allocationRequest.setDecisionDate(new Date());

        // Richiesta NON APPROVATA
        if(result.equals("Disapprove")) {
            allocationRequest.setStatus(AllocationRequest.Status.REJECTED);
            allocationRequestRepository.save(allocationRequest);
            return;
        }

        // Richiesta APPROVATA
        allocationRequest.setStatus(AllocationRequest.Status.ACCEPTED);
        allocationRequestRepository.save(allocationRequest);

        // Creazione del nuovo documento corrispondente nella collezione SmartBin
        SmartBin smartBin = requestToSmartBin(allocationRequest);

        smartBinRepository.save(smartBin);
    }



    public List<AllocationRequest> getAllRequests() {

        return allocationRequestRepository.findAll();
    }

    public AllocationRequest getRequestByID(String requestID) throws AllocationRequestNotFound {

        AllocationRequest allocationRequest = null;

        // Controllo l'esistenza della richiesta di allocazione
        Optional<AllocationRequest> request = allocationRequestRepository.findById(requestID);
        if(!request.isPresent())
            throw new AllocationRequestNotFound();

        allocationRequest = request.get();

        return allocationRequest;

    }


    public List<AllocationRequest> getRequestByStatus(String status) throws RequestInvalidStatusException {

        if(!validStatus(status))
            throw new RequestInvalidStatusException();

        return allocationRequestRepository.findByStatus(status);
    }




    // Controlla che il bin richiesto non corrisponda a un bin già allocato dello stesso tipo
    private boolean isTypePresent(List<SmartBin> smartBins, String desiredType) {
        return smartBins.stream()
                .anyMatch(smartBin -> smartBin.getType().getName().equalsIgnoreCase(desiredType));
    }

    private SmartBin requestToSmartBin(AllocationRequest allocationRequest) {

        SmartBin smartBin = new SmartBin();
        smartBin.setState(SmartBin.State.ALLOCATED);
        smartBin.setName(allocationRequest.getSmartBin_name());
        smartBin.setPosition(allocationRequest.getPosition());
        smartBin.setCurrentCapacity(null);
        smartBin.setType(allocationRequest.getType());
        smartBin.setTotalCapacity(allocationRequest.getTotalCapacity());
        return smartBin;
    }


    private boolean validStatus(String statusToCheck) {
        try {
            Enum.valueOf(AllocationRequest.Status.class, statusToCheck.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }

    }

}

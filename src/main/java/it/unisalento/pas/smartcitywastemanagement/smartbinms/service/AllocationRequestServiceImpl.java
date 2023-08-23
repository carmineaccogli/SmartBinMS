package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.*;
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


    public String saveAllocationRequest(AllocationRequest allocationRequest) throws RequestAlreadyExistsException {

        /* STEP DI VALIDAZIONE DI BUSINESS
        Controllo che non esista una richiesta con status=PENDING dello stesso type e nella stessa position
        Evito di accettare richieste con stessi dati e mantenerle in PENDING per evitare ridondanza e complessità di gestione
        */
        Boolean requestExists = allocationRequestRepository.existsAllocationRequestByTypeAndPositionAndStatus(allocationRequest.getType(), allocationRequest.getPosition(), AllocationRequest.Status.PENDING);
        if(requestExists)
            throw new RequestAlreadyExistsException();

        // RICHIESTA VALIDA

        // Inseriamo i parametri rimanenti della richiesta
        allocationRequest.setStatus(AllocationRequest.Status.PENDING);
        allocationRequest.setRequestedDate(new Date());

        // Salvataggio della richiesta
        AllocationRequest createdRequest = allocationRequestRepository.save(allocationRequest);
        return createdRequest.getId();
    }



    public void approveAllocationRequest(String requestID) throws RequestNotFoundException, SmartBinAlreadyAllocatedException, RequestAlreadyConfirmedException {

        // C1: Richiesta con dato ID esiste? Throw RequestNotFoundException
        AllocationRequest allocationRequest = checkRequestExistence(requestID);

        // C2: Richiesta è nello stato PENDING? Throw RequestAlreadyConfirmedException
        checkRequestValidity(allocationRequest);

        /* C3:
        Controllo che non esista già uno smartBin con stato=ALLOCATED, dello stesso type e nella stessa position di quello richiesto
         */
        Boolean smartBinExists = smartBinRepository.existsByTypeAndPositionAndState(allocationRequest.getType(),allocationRequest.getPosition(), SmartBin.State.ALLOCATED);
        if(smartBinExists) {

            // Cambiamento stato richiesta in quanto è stata negata
            allocationRequest.setStatus(AllocationRequest.Status.REJECTED);
            allocationRequest.setDecisionDate(new Date());
            allocationRequestRepository.save(allocationRequest);

            throw new SmartBinAlreadyAllocatedException();
        }


        /*boolean alreadyAllocated = isTypePresent(smartBinList, allocationRequest.getType().getName());
        if (alreadyAllocated) {
            throw new SmartBinAlreadyAllocatedException();
        }*/

        // C SUPERATI
        allocationRequest.setDecisionDate(new Date());
        allocationRequest.setStatus(AllocationRequest.Status.ACCEPTED);
        allocationRequestRepository.save(allocationRequest);

        // Creazione del nuovo documento corrispondente nella collezione SmartBin
        SmartBin smartBin = requestToSmartBin(allocationRequest);

        smartBinRepository.save(smartBin);
    }

    public void denyAllocationRequest(String requestID) throws RequestNotFoundException, RequestAlreadyConfirmedException {

        // C1: Richiesta con dato ID esiste? Throw RequestNotFoundException
        AllocationRequest allocationRequest = checkRequestExistence(requestID);

        // C2: Richiesta è nello stato PENDING? Throw RequestAlreadyConfirmedException
        checkRequestValidity(allocationRequest);

        // CONTROLLI SUPERATI
        allocationRequest.setDecisionDate(new Date());
        allocationRequest.setStatus(AllocationRequest.Status.REJECTED);

        allocationRequestRepository.save(allocationRequest);

    }



    public List<AllocationRequest> getAllRequests() {

        return allocationRequestRepository.findAll();
    }

    public AllocationRequest getRequestByID(String requestID) throws RequestNotFoundException {

        return checkRequestExistence(requestID);
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
        smartBin.setName(allocationRequest.getSmartBinName());
        smartBin.setPosition(allocationRequest.getPosition());
        smartBin.setCurrentCapacity(0.0f);
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

    private AllocationRequest checkRequestExistence(String requestID) throws RequestNotFoundException{

        // Controllo l'esistenza della richiesta di allocazione
        Optional<AllocationRequest> request = allocationRequestRepository.findById(requestID);
        if(!request.isPresent())
            throw new RequestNotFoundException();

        return request.get();
    }

    private void checkRequestValidity(AllocationRequest allocationRequest) throws RequestAlreadyConfirmedException {

        if(allocationRequest.getStatus() != AllocationRequest.Status.PENDING)
           throw new RequestAlreadyConfirmedException();
    }

}

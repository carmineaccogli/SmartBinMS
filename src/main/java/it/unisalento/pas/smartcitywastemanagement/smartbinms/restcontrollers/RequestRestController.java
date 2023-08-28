package it.unisalento.pas.smartcitywastemanagement.smartbinms.restcontrollers;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.*;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.*;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.AllocationRequestMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.RemovalRequestMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.service.AllocationRequestService;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.service.RemovalRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/smartbin/request")
public class RequestRestController {

    @Autowired
    private AllocationRequestService allocationRequestService;

    @Autowired
    private AllocationRequestMapper allocationRequestMapper;

    @Autowired
    private RemovalRequestMapper removalRequestMapper;

    @Autowired
    private RemovalRequestService removalRequestService;




    /* -----
    API PER SALVATAGGIO RICHIESTE
     ----- */

    @RequestMapping(value="/allocation", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> sendAllocationRequest(@Valid @RequestBody AllocationRequestSendDTO allocationRequestSendDTO) throws RequestAlreadyExistsException, SmartBinTypeNotFoundException, InvalidPositionException {

        // Conversione DTO->Domain
        AllocationRequest allocationRequest = allocationRequestMapper.toAllocationRequest(allocationRequestSendDTO);

        /*if (getSavePositionError(bindingResult))
            throw new InvalidPositionException();*/

        // Validazione e salvataggio della richiesta
        String createdId = allocationRequestService.saveAllocationRequest(allocationRequest);


        return new ResponseEntity<>(
                new ResponseDTO("Allocation Request created successfully", createdId),
                HttpStatus.CREATED);
    }


    @RequestMapping(value="/removal", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> sendRemovalRequest(@RequestBody @Valid RemovalRequestSendDTO removalRequestSendDTO) throws SmartBinNotFoundException, RequestAlreadyExistsException {

        // Conversione DTO->Domain
        RemovalRequest removalRequest = removalRequestMapper.toRemovalRequest(removalRequestSendDTO);

        // Validazione e salvataggio della richiesta
        String createdId = removalRequestService.saveRemovalRequest(removalRequest);

        return new ResponseEntity<>(
                new ResponseDTO("Removal Request created successfully", createdId),
                HttpStatus.CREATED);
    }


    /* -----
    API PER GESTIONE RICHIESTE DI ALLOCAZIONE
     ----- */

    @RequestMapping(value="/allocation/approve/{requestID}", method=RequestMethod.POST)
    public ResponseEntity<?> approveAllocation(@PathVariable String requestID) throws RequestNotFoundException,SmartBinAlreadyAllocatedException, RequestAlreadyConfirmedException {

        allocationRequestService.approveAllocationRequest(requestID);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="/allocation/disapprove/{requestID}", method=RequestMethod.POST)
    public ResponseEntity<?> disapproveAllocation(@PathVariable String requestID) throws RequestNotFoundException, RequestAlreadyConfirmedException {

        allocationRequestService.denyAllocationRequest(requestID);

        return ResponseEntity.noContent().build();
    }

    /* -----
    API PER GESTIONE RICHIESTE DI RIMOZIONE
     ----- */

    @RequestMapping(value="/removal/approve/{requestID}", method=RequestMethod.POST)
    public ResponseEntity<?> approveRemoval(@PathVariable String requestID) throws RequestNotFoundException, SmartBinNotFoundException, RequestAlreadyConfirmedException, SmartBinAlreadyRemovedException{
        removalRequestService.manageRemovalRequest(requestID, "Approve");
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="/removal/disapprove/{requestID}", method=RequestMethod.POST)
    public ResponseEntity<?> disapproveRemoval(@PathVariable String requestID) throws RequestNotFoundException,SmartBinNotFoundException,RequestAlreadyConfirmedException, SmartBinAlreadyRemovedException{
        removalRequestService.manageRemovalRequest(requestID, "Disapprove");
        return ResponseEntity.noContent().build();
    }


    /* -----
    API PER TROVARE TUTTE LE RICHIESTE PRESENTI
     ----- */

    @RequestMapping(value="/allocation/", method = RequestMethod.GET)
    public ResponseEntity<List<AllocationRequestViewDTO>> getAll_AllocationRequest() throws IOException {
        List<AllocationRequest> results = allocationRequestService.getAllRequests();


        List<AllocationRequestViewDTO> all_requests = fromAllocationRequestToDTOArray(results);

        if (all_requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(all_requests);
    }

    @RequestMapping(value="/removal/", method = RequestMethod.GET)
    public ResponseEntity<List<RemovalRequestViewDTO>> getAll_RemovalRequest() {
        List<RemovalRequest> results = removalRequestService.getAllRequests();


        List<RemovalRequestViewDTO> all_requests = fromRemovalRequestToDTOArray(results);

        if (all_requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(all_requests);
    }


    /* -----
    API PER FILTRARE PER STATO LE RICHIESTE PRESENTI
     ----- */

    @RequestMapping(value="/allocation", method = RequestMethod.GET)
    public ResponseEntity<List<AllocationRequestViewDTO>> getAllocationRequestByStatus(@RequestParam("status") String status) throws RequestInvalidStatusException {

        List<AllocationRequest> results = allocationRequestService.getRequestByStatus(status);

        List<AllocationRequestViewDTO> all_requests = fromAllocationRequestToDTOArray(results);

        if (all_requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(all_requests);

    }

    @RequestMapping(value="/removal", method = RequestMethod.GET)
    public ResponseEntity<List<RemovalRequestViewDTO>> getRemovalRequestByStatus(@RequestParam("status") String status) throws RequestInvalidStatusException {

        List<RemovalRequest> results = removalRequestService.getRequestByStatus(status);

        List<RemovalRequestViewDTO> all_requests = fromRemovalRequestToDTOArray(results);

        if (all_requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(all_requests);

    }


    /* -----
    API PER TROVARE UNA SPECIFICA RICHIESTA
     ----- */

    @RequestMapping(value="/allocation/{requestID}", method=RequestMethod.GET)
    public AllocationRequestViewDTO getAllocationRequest(@PathVariable String requestID) throws RequestNotFoundException{
        AllocationRequest request = allocationRequestService.getRequestByID(requestID);

        AllocationRequestViewDTO result = allocationRequestMapper.toAllocationRequestDTO(request);

        return result;
    }

    @RequestMapping(value="/removal/{requestID}", method=RequestMethod.GET)
    public RemovalRequestViewDTO getRemovalRequest(@PathVariable String requestID) throws RequestNotFoundException{

        RemovalRequest request = removalRequestService.getRequestByID(requestID);

        RemovalRequestViewDTO result = removalRequestMapper.toRemovalRequestDTO(request);

        return result;
    }


    private List<AllocationRequestViewDTO> fromAllocationRequestToDTOArray(List<AllocationRequest> entityRequests) {
        List<AllocationRequestViewDTO> result = new ArrayList<>();

        for(AllocationRequest request: entityRequests) {
            AllocationRequestViewDTO allocationRequestViewDTO = allocationRequestMapper.toAllocationRequestDTO(request);
            result.add(allocationRequestViewDTO);
        }
        return result;
    }

    private List<RemovalRequestViewDTO> fromRemovalRequestToDTOArray(List<RemovalRequest> entityRequests) {
        List<RemovalRequestViewDTO> result = new ArrayList<>();

        for(RemovalRequest request: entityRequests) {
            RemovalRequestViewDTO removalRequestViewDTO = removalRequestMapper.toRemovalRequestDTO(request);
            result.add(removalRequestViewDTO);
        }
        return result;
    }

    /*private boolean getSavePositionError(BindingResult errors) {
        if (errors.hasErrors()) {
            for (FieldError error : errors.getFieldErrors()) {
                String fieldName = error.getField();

                if ("position".equals(fieldName)) {
                    return true;
                }
            }
        }
        return false;
    }*/

}

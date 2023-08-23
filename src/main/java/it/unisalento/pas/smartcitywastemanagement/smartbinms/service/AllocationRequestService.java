package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.*;

import java.util.List;

public interface AllocationRequestService {

    String saveAllocationRequest(AllocationRequest allocationRequest) throws RequestAlreadyExistsException;
    void approveAllocationRequest(String requestID) throws RequestNotFoundException,SmartBinAlreadyAllocatedException, RequestAlreadyConfirmedException;

    void denyAllocationRequest(String requestID) throws RequestNotFoundException, RequestAlreadyConfirmedException;

    List<AllocationRequest> getAllRequests();

    AllocationRequest getRequestByID(String requestID) throws RequestNotFoundException;

    List<AllocationRequest> getRequestByStatus(String status) throws RequestInvalidStatusException;

}

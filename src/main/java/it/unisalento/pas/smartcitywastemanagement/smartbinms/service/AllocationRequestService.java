package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.AllocationRequestNotFound;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.RequestInvalidStatusException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinAlreadyAllocatedException;

import java.util.List;

public interface AllocationRequestService {

    String saveAllocationRequest(AllocationRequest allocationRequest) throws SmartBinAlreadyAllocatedException;
    void manageAllocationRequest(String requestID, String result) throws AllocationRequestNotFound;

    List<AllocationRequest> getAllRequests();

    AllocationRequest getRequestByID(String requestID) throws AllocationRequestNotFound;

    List<AllocationRequest> getRequestByStatus(String status) throws RequestInvalidStatusException;

}

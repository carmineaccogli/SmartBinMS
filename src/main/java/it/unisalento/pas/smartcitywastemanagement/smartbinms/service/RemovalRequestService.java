package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.*;

import java.util.List;

public interface RemovalRequestService {

    String saveRemovalRequest(RemovalRequest removalRequest) throws SmartBinNotFoundException;

    void manageRemovalRequest(String requestID, String result) throws RemovalRequestNotFound, SmartBinNotFoundException;

    List<RemovalRequest> getAllRequests();

    RemovalRequest getRequestByID(String requestID) throws RemovalRequestNotFound;

    List<RemovalRequest> getRequestByStatus(String status) throws RequestInvalidStatusException;
}

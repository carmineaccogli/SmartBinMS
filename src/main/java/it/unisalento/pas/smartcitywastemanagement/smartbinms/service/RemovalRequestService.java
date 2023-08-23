package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.*;

import java.util.List;

public interface RemovalRequestService {

    String saveRemovalRequest(RemovalRequest removalRequest) throws SmartBinNotFoundException, RequestAlreadyExistsException;

    void manageRemovalRequest(String requestID, String result) throws RequestNotFoundException, SmartBinNotFoundException, RequestAlreadyConfirmedException, SmartBinAlreadyRemovedException;

    List<RemovalRequest> getAllRequests();

    RemovalRequest getRequestByID(String requestID) throws RequestNotFoundException;

    List<RemovalRequest> getRequestByStatus(String status) throws RequestInvalidStatusException;
}

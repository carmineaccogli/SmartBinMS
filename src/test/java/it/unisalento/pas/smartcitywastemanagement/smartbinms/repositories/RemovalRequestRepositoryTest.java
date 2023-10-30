package it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@AutoConfigureDataMongo
public class RemovalRequestRepositoryTest {

    @Autowired
    private RemovalRequestRepository removalRequestRepository;


    @Test
    public void testFindAll() {

        RemovalRequest request1 = new RemovalRequest();
        request1.setId("1");

        RemovalRequest request2 = new RemovalRequest();
        request2.setId("2");

        removalRequestRepository.save(request1);
        removalRequestRepository.save(request2);

        List<RemovalRequest> allRequests = removalRequestRepository.findAll();

        assertFalse(allRequests.isEmpty());

        assertEquals(request1.getId(),allRequests.get(0).getId());
        assertEquals(request2.getId(),allRequests.get(1).getId());
    }

    @Test
    public void testFindById() {

        RemovalRequest request = new RemovalRequest();
        request.setId("TestID");

        removalRequestRepository.save(request);

        Optional<RemovalRequest> optRequest = removalRequestRepository.findById(request.getId());

        assertTrue(optRequest.isPresent());
        assertEquals(request.getId(), optRequest.get().getId());
    }

    @Test
    public void testFindByStatus() {

        RemovalRequest request1 = new RemovalRequest();
        request1.setId("1");
        request1.setStatus(RemovalRequest.Status.PENDING);

        RemovalRequest request2 = new RemovalRequest();
        request2.setId("2");
        request2.setStatus(RemovalRequest.Status.PENDING);

        removalRequestRepository.save(request1);
        removalRequestRepository.save(request2);

        List<RemovalRequest> pendingRequests = removalRequestRepository.findByStatus("Pending");

        assertFalse(pendingRequests.isEmpty());
        assertEquals(RemovalRequest.Status.PENDING, pendingRequests.get(0).getStatus());
        assertEquals(RemovalRequest.Status.PENDING, pendingRequests.get(1).getStatus());
    }

    @Test
    public void testExistsRemovalRequestByBySmartBinIDAndStatus() {


        RemovalRequest request = new RemovalRequest();
        request.setId("TestID");
        request.setSmartBinID("SmartBinID");
        request.setStatus(RemovalRequest.Status.PENDING);

        removalRequestRepository.save(request);

        boolean exists = removalRequestRepository.existsBySmartBinIDAndStatus(request.getSmartBinID(), RemovalRequest.Status.PENDING);

        assertTrue(exists);
    }









    @AfterEach
    public void cleanup() {
        removalRequestRepository.deleteAll();
    }
}

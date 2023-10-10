package it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories;


import de.flapdoodle.embed.mongo.packageresolver.DistributionMatch;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import org.assertj.core.api.AbstractLocalDateAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class AllocationRequestRepositoryTest {

    @Autowired
    private AllocationRequestRepository allocationRequestRepository;



    @Test
    public void testFindAll() {

        AllocationRequest request1 = new AllocationRequest();
        request1.setId("1");

        AllocationRequest request2 = new AllocationRequest();
        request2.setId("2");

        allocationRequestRepository.save(request1);
        allocationRequestRepository.save(request2);

        List<AllocationRequest> allRequests = allocationRequestRepository.findAll();

        assertFalse(allRequests.isEmpty());

        assertEquals(request1.getId(),allRequests.get(0).getId());
        assertEquals(request2.getId(),allRequests.get(1).getId());
    }


    @Test
    public void testFindById() {

        AllocationRequest request = new AllocationRequest();
        request.setId("TestID");

        allocationRequestRepository.save(request);

        Optional<AllocationRequest> optRequest = allocationRequestRepository.findById(request.getId());

        assertTrue(optRequest.isPresent());
        assertEquals(request.getId(), optRequest.get().getId());
    }


    @Test
    public void testFindByStatus() {

        AllocationRequest request1 = new AllocationRequest();
        request1.setId("1");
        request1.setStatus(AllocationRequest.Status.PENDING);

        AllocationRequest request2 = new AllocationRequest();
        request2.setId("2");
        request2.setStatus(AllocationRequest.Status.PENDING);

        allocationRequestRepository.save(request1);
        allocationRequestRepository.save(request2);

        List<AllocationRequest> pendingRequests = allocationRequestRepository.findByStatus("Pending");

        assertFalse(pendingRequests.isEmpty());
        assertEquals(AllocationRequest.Status.PENDING, pendingRequests.get(0).getStatus());
        assertEquals(AllocationRequest.Status.PENDING, pendingRequests.get(1).getStatus());
    }


    @Test
    public void testExistsAllocationRequestByTypeAndPositionAndStatus() {

        Type type = new Type();
        type.setName("TestType");
        type.setId("TestTypeID");

        GeoJsonPoint point = new GeoJsonPoint(18,42);

        AllocationRequest allocationRequest = new AllocationRequest();
        allocationRequest.setType(type);
        allocationRequest.setPosition(point);
        allocationRequest.setStatus(AllocationRequest.Status.PENDING);
        allocationRequest.setId("TestID");

        allocationRequestRepository.save(allocationRequest);

        boolean exists = allocationRequestRepository.existsAllocationRequestByTypeAndPositionAndStatus(type, point, AllocationRequest.Status.PENDING);

        assertTrue(exists);
    }



    @AfterEach
    public void cleanup() {
        allocationRequestRepository.deleteAll();
    }

}

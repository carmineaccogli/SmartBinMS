package it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AllocationRequestRepository extends MongoRepository<AllocationRequest, String> {

    public Optional<AllocationRequest> findById(String id);

    List<AllocationRequest> findAll();

    @Query(value="{'status': {$regex:  '^?0$', $options:  'i'}}" )
    List<AllocationRequest> findByStatus(String status);

    Boolean existsAllocationRequestByTypeAndPositionAndStatus(Type type,GeoJsonPoint position, AllocationRequest.Status status);

}

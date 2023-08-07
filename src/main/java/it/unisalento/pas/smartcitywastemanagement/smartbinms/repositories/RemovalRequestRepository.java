package it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RemovalRequestRepository extends MongoRepository<RemovalRequest, String> {

    public Optional<RemovalRequest> findById(String id);

    List<RemovalRequest> findAll();

    @Query(value="{'status': {$regex:  '^?0$', $options:  'i'}}" )
    List<RemovalRequest> findByStatus(String status);
}

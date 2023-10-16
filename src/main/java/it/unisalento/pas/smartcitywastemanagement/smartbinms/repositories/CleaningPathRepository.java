package it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.CleaningPathDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CleaningPathRepository extends MongoRepository<CleaningPath, String> {


    List<CleaningPath> findByDone(boolean done);

    Optional<CleaningPath> findById(String cleaningPathID);

    List<CleaningPath> findByScheduledDateGreaterThanEqualOrderByScheduledDate(Date startDate);
}

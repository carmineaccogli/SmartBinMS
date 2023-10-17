package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.CleaningPathDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.CleaningPathNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.CleaningPathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CleaningPathServiceImpl implements CleaningPathService {


    @Autowired
    private CleaningPathRepository cleaningPathRepository;

    public String saveCleaningPath(CleaningPath cleaningPath) {

        // Setting status completed a false
        cleaningPath.setDone(false);

        CleaningPath result = cleaningPathRepository.save(cleaningPath);

        return result.getId();
    }


    public List<CleaningPath> getCleaningPathByStatus(boolean done) {
        return cleaningPathRepository.findByDone(done);
    }

    public void updateCleaningPathStatus(String cleaningPathID) throws CleaningPathNotFoundException {

        CleaningPath pathRequested = null;
        Optional<CleaningPath> optPathRequested = cleaningPathRepository.findById(cleaningPathID);

        if(!optPathRequested.isPresent())
            throw new CleaningPathNotFoundException();

        pathRequested = optPathRequested.get();

        pathRequested.setDone(true);

        cleaningPathRepository.save(pathRequested);
    }

    public List<CleaningPath> getCleaningPathToDoFrom(String startDateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date startDate = sdf.parse(startDateStr);
        return cleaningPathRepository.findByScheduledDateGreaterThanEqualOrderByScheduledDate(startDate);
    }
}

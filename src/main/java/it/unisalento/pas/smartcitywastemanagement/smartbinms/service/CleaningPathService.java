package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.CleaningPathNotFoundException;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface CleaningPathService {


    String saveCleaningPath(CleaningPath cleaningPath);

    List<CleaningPath> getCleaningPathByStatus(boolean done);

    void updateCleaningPathStatus(String cleaningPathID) throws CleaningPathNotFoundException;

    List<CleaningPath> getCleaningPathToDoFrom(String startDate) throws ParseException;
}

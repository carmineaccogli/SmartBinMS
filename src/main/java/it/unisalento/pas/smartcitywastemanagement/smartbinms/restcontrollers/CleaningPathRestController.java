package it.unisalento.pas.smartcitywastemanagement.smartbinms.restcontrollers;


import com.fasterxml.jackson.databind.annotation.JsonValueInstantiator;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.RemovalRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.CleaningPathDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.RemovalRequestViewDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.ResponseDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.CleaningPathNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.CleaningPathMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.service.CleaningPathService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value="/api/cleaningPath")
public class CleaningPathRestController {

    @Autowired
    private CleaningPathService cleaningPathService;

    @Autowired
    private CleaningPathMapper cleaningPathMapper;

    @PreAuthorize("hasRole('ROLE_WasteManagementCompany')")
    @RequestMapping(value="/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> addNewCleaningPath(@Valid @RequestBody CleaningPathDTO cleaningPathDTO)  {

        CleaningPath cleaningPath = cleaningPathMapper.toCleaningPath(cleaningPathDTO);

        String createdID = cleaningPathService.saveCleaningPath(cleaningPath);
        return new ResponseEntity<>(
                new ResponseDTO("Cleaning Path created successfully",createdID),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasAnyRole('ROLE_WasteManagementCompany','ROLE_SmartBinNode','ROLE_Admin')")
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity<List<CleaningPathDTO>> getCleaningPathByStatus(@RequestParam("done") boolean done) {

        List<CleaningPath> results = cleaningPathService.getCleaningPathByStatus(done);

        List<CleaningPathDTO> all_paths = fromCleaningPathToDTOArray(results);

        if (all_paths.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(all_paths);
    }

    @PreAuthorize("hasAnyRole('ROLE_WasteManagementCompany','SmartBinNode','ROLE_Admin')")
    @RequestMapping(value = "/date", method = RequestMethod.GET)
    public ResponseEntity<List<CleaningPathDTO>> getCleaningPathToDoFromStartDate(@RequestParam("from") String startDate) throws ParseException {

        List<CleaningPath> results = cleaningPathService.getCleaningPathToDoFrom(startDate);

        List<CleaningPathDTO> all_paths = fromCleaningPathToDTOArray(results);

        if (all_paths.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(all_paths);
    }


    @PreAuthorize("hasAnyRole('ROLE_WasteManagementCompany','ROLE_SmartBinNode')")
    @RequestMapping(value ="/{cleaningPathID}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateStatusCleaningPath(@PathVariable("cleaningPathID") String cleaningPathID) throws CleaningPathNotFoundException {

        cleaningPathService.updateCleaningPathStatus(cleaningPathID);

        return ResponseEntity.noContent().build();
    }



    private List<CleaningPathDTO> fromCleaningPathToDTOArray(List<CleaningPath> entityPaths) {
        List<CleaningPathDTO> result = new ArrayList<>();

        for(CleaningPath cleaningPath: entityPaths) {
            CleaningPathDTO cleaningPathDTO = cleaningPathMapper.toCleaningPathDTO(cleaningPath);
            result.add(cleaningPathDTO);
        }
        return result;
    }
}

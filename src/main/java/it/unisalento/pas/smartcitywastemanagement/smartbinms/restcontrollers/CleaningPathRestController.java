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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value="/api/cleaningPath")
public class CleaningPathRestController {

    @Autowired
    private CleaningPathService cleaningPathService;

    @Autowired
    private CleaningPathMapper cleaningPathMapper;

    @RequestMapping(value="/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> addNewCleaningPath(@Valid @RequestBody CleaningPathDTO cleaningPathDTO)  {

        CleaningPath cleaningPath = cleaningPathMapper.toCleaningPath(cleaningPathDTO);

        String createdID = cleaningPathService.saveCleaningPath(cleaningPath);
        return new ResponseEntity<>(
                new ResponseDTO("Cleaning Path created successfully",createdID),
                HttpStatus.CREATED
        );
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity<List<CleaningPathDTO>> getCleaningPathByStatus(@RequestParam("done") boolean done) {

        List<CleaningPath> results = cleaningPathService.getCleaningPathByStatus(done);

        List<CleaningPathDTO> all_paths = fromCleaningPathToDTOArray(results);

        if (all_paths.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(all_paths);
    }


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

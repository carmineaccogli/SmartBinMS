package it.unisalento.pas.smartcitywastemanagement.smartbinms.restcontrollers;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.CapacityThresholdRequestDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.DisposalRequestDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.ResponseDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.SmartBinDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.*;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.SmartBinMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.service.ManageSmartBinsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api/smartbin")
public class SmartBinRestController {


    @Autowired
    private ManageSmartBinsService manageSmartBinsService;


    @Autowired
    private SmartBinMapper smartBinMapper;



    /*-----
    API PER TROVARE SMARTBIN
    -----*/

    @PreAuthorize("hasAnyRole('ROLE_WasteManagementCompany','ROLE_Admin','ROLE_MunicipalOffice')")
    @RequestMapping(value="/", method=RequestMethod.GET)
    public ResponseEntity<List<SmartBinDTO>> getAllBins() {

        List<SmartBin> results = manageSmartBinsService.getAllSmartBins();

        List<SmartBinDTO> allBins = new ArrayList<>();

        // Conversione SmartBin -> SmartBinRequestDTO
        for(SmartBin bin: results) {
            SmartBinDTO smartBinDTO = smartBinMapper.toSmartBinDTO(bin);

            System.out.println(smartBinDTO.getPosition());

            allBins.add(smartBinDTO);
        }

        if (allBins.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(allBins);
    }

    @PreAuthorize("hasAnyRole('ROLE_WasteManagementCompany','ROLE_SmartBinNode','ROLE_Admin','ROLE_MunicipalOffice')")
    @RequestMapping(value="/{smartBinID}", method=RequestMethod.GET)
    public SmartBinDTO getSmartBin(@PathVariable String smartBinID) throws SmartBinNotFoundException{
        SmartBin bin = manageSmartBinsService.getSmartBinByID(smartBinID);

        SmartBinDTO binDTO = smartBinMapper.toSmartBinDTO(bin);

        return binDTO;
    }


    /*-----
    API PER AGGIORNARE LA CAPACITA' DI UNO SMARTBIN
     -----*/
    /*@RequestMapping(value="/{smartBinID}",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> doDisposal(@PathVariable String smartBinID, @RequestBody @Valid DisposalRequestDTO disposalRequest) throws SmartBinNotFoundException, SmartBinIsFullException {

        manageSmartBinsService.manageDisposalRequest(smartBinID, disposalRequest.getAmount());

        return ResponseEntity.noContent().build();
    }*/

    @PreAuthorize("hasAnyRole('ROLE_WasteManagementCompany','ROLE_Admin')")
    @RequestMapping(value="/{smartBinID}/capacityThreshold", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCapacityThreshold(@PathVariable("smartBinID") String smartBinID, @RequestBody @Valid CapacityThresholdRequestDTO capacityThresholdRequestDTO) throws SmartBinNotFoundException {

        manageSmartBinsService.updateCapacityThreshold(smartBinID, capacityThresholdRequestDTO.getCapacityThreshold());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_SmartBinNode','ROLE_Admin')")
    @RequestMapping(value="{smartBinID}/reset", method=RequestMethod.POST)
    public ResponseEntity<?> resetSmartBinCapacity(@PathVariable("smartBinID") String smartBinID) throws SmartBinNotFoundException {
        manageSmartBinsService.resetCapacity(smartBinID);
        return ResponseEntity.noContent().build();
    }

    /*-----
    API PER FILTRARE SMARTBIN
    -----*/

    @PreAuthorize("hasAnyRole('ROLE_MunicipalOffice','ROLE_WasteManagementCompany','ROLE_SmartBinNode','ROLE_Admin')")
    @RequestMapping(value="/state",method = RequestMethod.GET)
    public ResponseEntity<List<SmartBinDTO>> getSmartBinByState(@RequestParam("state") String state) throws SmartBinStateInvalidException {

        List<SmartBin> results = manageSmartBinsService.filterSmartBinByState(state);

        List<SmartBinDTO> filteredBins = new ArrayList<>();

        // Conversione SmartBin -> SmartBinRequestDTO
        for(SmartBin bin: results) {
            SmartBinDTO smartBinDTO = smartBinMapper.toSmartBinDTO(bin);
            filteredBins.add(smartBinDTO);
        }

        if (filteredBins.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(filteredBins);
    }

    @PreAuthorize("hasAnyRole('ROLE_WasteManagementCompany','ROLE_Admin','ROLE_MunicipalOffice')")
    @RequestMapping(value="/type",method = RequestMethod.GET)
    public ResponseEntity<List<SmartBinDTO>> getSmartBinByType(@RequestParam("type") String type) throws SmartBinTypeNotFoundException {

        List<SmartBin> results = manageSmartBinsService.filterSmartBinByType(type);

        List<SmartBinDTO> filteredBins = new ArrayList<>();

        // Conversione SmartBin -> SmartBinRequestDTO
        for(SmartBin bin: results) {
            SmartBinDTO smartBinDTO = smartBinMapper.toSmartBinDTO(bin);
            filteredBins.add(smartBinDTO);
        }

        if (filteredBins.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(filteredBins);
    }


    @PreAuthorize("hasAnyRole('ROLE_WasteManagementCompany','ROLE_Admin')")
    @RequestMapping(value="/binsAboveThreshold", method=RequestMethod.GET)
    public ResponseEntity<List<SmartBinDTO>> getBinsAboveThreshold(@RequestParam(value = "capacityRatio") @Min(0) @Max(1) double capacityRatio) {

        List<SmartBin> results = manageSmartBinsService.filterSmartBinByCapacityRatio(capacityRatio);

        List<SmartBinDTO> filteredBins = new ArrayList<>();

        // Conversione SmartBin -> SmartBinRequestDTO
        for(SmartBin bin: results) {
            SmartBinDTO smartBinDTO = smartBinMapper.toSmartBinDTO(bin);
            filteredBins.add(smartBinDTO);
        }

        if (filteredBins.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(filteredBins);
    }


    @PreAuthorize("hasAnyRole('ROLE_WasteManagementCompany')")
    @RequestMapping(value="/test", method=RequestMethod.GET)
    public String test(){
        return "Hello world";
    }






}

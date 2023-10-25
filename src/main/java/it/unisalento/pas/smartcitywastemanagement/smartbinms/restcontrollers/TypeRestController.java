package it.unisalento.pas.smartcitywastemanagement.smartbinms.restcontrollers;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.AllocationRequestViewDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.ResponseDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.TypeDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers.TypeMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.TypeRepository;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.service.TypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value="/api/smartbin/type")
public class TypeRestController {

        @Autowired
        private TypeMapper typeMapper;

        @Autowired
        private TypeService typeService;

        @PreAuthorize("hasRole('ROLE_WasteManagementCompany')")
        @RequestMapping(value="/add", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<ResponseDTO> addNewType(@Valid @RequestBody TypeDTO typeDTO) throws DuplicateKeyException {

            Type type = typeMapper.toType(typeDTO);
            String createdID = typeService.saveType(type);
            return new ResponseEntity<>(new ResponseDTO("Type added successfully","id:"+createdID),
                    HttpStatus.CREATED);
        }

        @PreAuthorize("hasAnyRole('MunicipalOffice','WasteManagementCompany','Citizen')")
        @RequestMapping(value="/", method=RequestMethod.GET)
        public ResponseEntity<List<TypeDTO>> getAll_Type() {

            List<Type> results = typeService.getAllTypeNames();

            List<TypeDTO> all_types = new ArrayList<>();

            // Conversione
            for(Type type: results) {
                TypeDTO typeDTO = typeMapper.toTypeDTO(type);
                all_types.add(typeDTO);
            }

            if (all_types.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(all_types);
        }
}

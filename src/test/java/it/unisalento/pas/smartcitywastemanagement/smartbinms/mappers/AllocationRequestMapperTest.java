package it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.AllocationRequestSendDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.AllocationRequestViewDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.SmartBinDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinTypeNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.TypeRepository;
import org.bson.types.Decimal128;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AllocationRequestMapper.class})
@ExtendWith(MockitoExtension.class)
public class AllocationRequestMapperTest {


    @InjectMocks
    private AllocationRequestMapper allocationRequestMapper;

    @MockBean
    private TypeRepository typeRepository;


    @Test
    public void testToAllocationRequestDTO() {
        AllocationRequest allocationRequest = new AllocationRequest();

        Date requested =new Date() ;
        Date decision = new Date(System.currentTimeMillis() + 3600_000);
        Type type = new Type();
        type.setName("Indifferenziata");
        GeoJsonPoint point = new GeoJsonPoint(18,42.3);
        Decimal128 totalCapacity = new Decimal128(50);

        allocationRequest.setId("TestID");
        allocationRequest.setSmartBinName("TestSmartBinID");
        allocationRequest.setStatus(AllocationRequest.Status.ACCEPTED);
        allocationRequest.setPosition(point);
        allocationRequest.setType(type);
        allocationRequest.setTotalCapacity(totalCapacity);
        allocationRequest.setDecisionDate(decision);
        allocationRequest.setRequestedDate(requested);

        AllocationRequestViewDTO result = allocationRequestMapper.toAllocationRequestDTO(allocationRequest);

        assertEquals("TestID", result.getId());
        assertEquals("TestSmartBinID", result.getSmartBinName());
        assertEquals(AllocationRequest.Status.ACCEPTED.toString(), result.getStatus());
        assertEquals(type.getName(),result.getType());
        assertEquals(totalCapacity.bigDecimalValue(), result.getTotalCapacity());
        assertEquals(point, result.getPosition());
        assertEquals(requested, result.getRequestedDate());
        assertEquals(decision, result.getDecisionDate());
    }

    @Test
    public void testToAllocationRequest() throws Exception{
        Type type = new Type();
        type.setName("TypeTest");
        GeoJsonPoint point = new GeoJsonPoint(18,42.3);
        Decimal128 totalCapacity = new Decimal128(50);

        when(typeRepository.findByTypeName(any(String.class))).thenReturn(Optional.of(type));

        AllocationRequestSendDTO sendDTO = new AllocationRequestSendDTO();
        sendDTO.setSmartBinName("TestSmartBin");
        sendDTO.setPosition(point);
        sendDTO.setTotalCapacity(totalCapacity.bigDecimalValue());
        sendDTO.setType(type.getName());

        AllocationRequest result = allocationRequestMapper.toAllocationRequest(sendDTO);

        // Verificare che l'oggetto AllocationRequest sia stato creato correttamente
        assertEquals("TestSmartBin", result.getSmartBinName());
        assertEquals(point, result.getPosition());
        assertEquals(totalCapacity, result.getTotalCapacity());
        assertEquals(type.getName(), result.getType().getName());
    }

    @Test
    public void testToAllocationRequestWithSmartBinTypeNotFound()  {
        AllocationRequestSendDTO sendDTO = new AllocationRequestSendDTO();
        GeoJsonPoint point = new GeoJsonPoint(18,42.3);
        Decimal128 totalCapacity = new Decimal128(50);
        sendDTO.setPosition(point);
        sendDTO.setSmartBinName("TestSmartBin");
        sendDTO.setTotalCapacity(totalCapacity.bigDecimalValue());

        when(typeRepository.findByTypeName(any(String.class))).thenReturn(Optional.empty());


        assertThrows(SmartBinTypeNotFoundException.class, () ->allocationRequestMapper.toAllocationRequest(sendDTO));
    }
}

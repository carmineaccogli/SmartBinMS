package it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.SmartBinDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.TypeDTO;
import org.bson.types.Decimal128;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {SmartBinMapper.class})
@ExtendWith(MockitoExtension.class)
public class SmartBinMapperTest {

    private SmartBinMapper smartBinMapper;

    @BeforeEach
    public void setUp() {
        smartBinMapper = new SmartBinMapper();
    }


    @Test
    public void testToSmartBinDTO() {
        Type type = new Type();
        type.setName("Indifferenziata");

        GeoJsonPoint point = new GeoJsonPoint(18,42.3);
        Decimal128 currentCapacity = new Decimal128(BigDecimal.valueOf(10.2));
        Decimal128 totalCapacity = new Decimal128(50);
        Float capacityThreshold = 0.80f;

        SmartBin smartBin = new SmartBin();
        smartBin.setName("TestName");
        smartBin.setId("TestID");
        smartBin.setType(type);
        smartBin.setState(SmartBin.State.ALLOCATED);
        smartBin.setCurrentCapacity(currentCapacity);
        smartBin.setTotalCapacity(totalCapacity);
        smartBin.setCapacityThreshold(capacityThreshold);
        smartBin.setPosition(point);



        // Eseguire il metodo da testare
        SmartBinDTO result = smartBinMapper.toSmartBinDTO(smartBin);

        // Verificare che il risultato sia corretto
        assertEquals("TestName", result.getName());
        assertEquals("TestID", result.getId());
        assertEquals("ALLOCATED", result.getState());
        assertEquals("Indifferenziata",result.getType());
        assertEquals(currentCapacity.bigDecimalValue(), result.getCurrentCapacity());
        assertEquals(totalCapacity.bigDecimalValue(), result.getTotalCapacity());
        assertEquals(String.format(Locale.US,"%.2f",capacityThreshold), result.getCapacityThreshold());
        assertEquals(point, result.getPosition());
    }
}

package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinStateInvalidException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinTypeNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.SmartBinRepository;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.TypeRepository;
import org.bson.types.Decimal128;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.fasterxml.jackson.databind.ObjectWriter.Prefetch.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {ManageSmartBinsServiceImpl.class})
public class ManageSmartBinsServiceImplTest {

    @InjectMocks
    private ManageSmartBinsServiceImpl manageSmartBinsService;

    @MockBean
    private SmartBinRepository smartBinRepository;

    @MockBean
    private TypeRepository typeRepository;


    @Test
    public void testFilterSmartBinByState() throws Exception{
        SmartBin smartBin1 = new SmartBin();
        smartBin1.setId("1");
        smartBin1.setState(SmartBin.State.ALLOCATED);

        SmartBin smartBin2 = new SmartBin();
        smartBin2.setId("2");
        smartBin2.setState(SmartBin.State.ALLOCATED);

        List<SmartBin> activeSmartBins = new ArrayList<>();
        activeSmartBins.add(smartBin1);
        activeSmartBins.add(smartBin2);

        when(smartBinRepository.findByState(any(String.class))).thenReturn(activeSmartBins);

        assertEquals(activeSmartBins, manageSmartBinsService.filterSmartBinByState(SmartBin.State.ALLOCATED.toString()));

        verify(smartBinRepository, times(1)).findByState(SmartBin.State.ALLOCATED.toString());

    }

    @Test
    public void testFilterSmartBinByStateWhenSmartBinStateIsNotValid() {

        assertThrows(SmartBinStateInvalidException.class, () -> manageSmartBinsService.filterSmartBinByState("INVALID_STATE"));

        verify(smartBinRepository, never()).findByState("INVALID_STATE");
    }


    @Test
    public void testFilterSmartBinByType() throws Exception {

        Type type = new Type();
        type.setName("TypeTest");

        SmartBin smartBin1 = new SmartBin();
        smartBin1.setId("1");
        smartBin1.setType(type);

        SmartBin smartBin2 = new SmartBin();
        smartBin2.setId("2");
        smartBin2.setType(type);

        List<SmartBin> filteredSmartBins = new ArrayList<>();
        filteredSmartBins.add(smartBin1);
        filteredSmartBins.add(smartBin2);


        when(typeRepository.findByTypeName(any(String.class))).thenReturn(Optional.of(type));
        when(smartBinRepository.findByType(any(String.class))).thenReturn(filteredSmartBins);

        List<SmartBin> result = manageSmartBinsService.filterSmartBinByType(type.getName());

        assertEquals(filteredSmartBins, result);

        verify(typeRepository, times(1)).findByTypeName("TypeTest");
        verify(smartBinRepository, times(1)).findByType("TypeTest");

    }

    @Test
    public void testFilterSmartBinByTypeWithSmartBinTypeNotFoundException() {

        when(typeRepository.findByTypeName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(SmartBinTypeNotFoundException.class, () -> manageSmartBinsService.filterSmartBinByType("TypeTest"));


        verify(typeRepository, times(1)).findByTypeName("TypeTest");
        verify(smartBinRepository, never()).findByType(anyString());
    }

    @Test
    public void testFilterSmartBinByCapacityRatio() {

        SmartBin smartBin1 = new SmartBin();
        smartBin1.setId("1");

        SmartBin smartBin2 = new SmartBin();
        smartBin2.setId("2");

        List<SmartBin> filteredSmartBins = new ArrayList<>();
        filteredSmartBins.add(smartBin1);
        filteredSmartBins.add(smartBin2);

        when(smartBinRepository.findyByCapacityRatio(any(Double.class))).thenReturn(filteredSmartBins);

        List<SmartBin> result = manageSmartBinsService.filterSmartBinByCapacityRatio(0.8);

        assertEquals(filteredSmartBins, result);

        verify(smartBinRepository, times(1)).findyByCapacityRatio(0.8);
    }

    @Test
    public void testGetAllSmartBins() {
        List<SmartBin> smartBinList = new ArrayList<>();
        SmartBin bin1 = new SmartBin();
        bin1.setId("1");

        SmartBin bin2 = new SmartBin();
        bin2.setId("2");

        smartBinList.add(bin1);
        smartBinList.add(bin2);

        when(smartBinRepository.findAll()).thenReturn(smartBinList);

        List<SmartBin> result = manageSmartBinsService.getAllSmartBins();

        assertNotNull(result);
        assertEquals(smartBinList, result);
    }

    @Test
    public void testGetSmartBinByID() throws Exception {
        String smartBinID = "TestID";

        SmartBin smartBin = new SmartBin();
        smartBin.setId(smartBinID);

        when(smartBinRepository.findById(any(String.class))).thenReturn(Optional.of(smartBin));

        SmartBin result = manageSmartBinsService.getSmartBinByID(smartBinID);

        assertNotNull(result);
        assertEquals(smartBin, result);

        verify(smartBinRepository, times(1)).findById(smartBinID);
    }


    @Test
    public void testManageDisposalRequest() throws Exception{

        BigDecimal disposalAmount = new BigDecimal("0.00043");


        SmartBin smartBin = new SmartBin();
        smartBin.setId("1");
        smartBin.setCurrentCapacity(new Decimal128(50));
        smartBin.setTotalCapacity(new Decimal128(100));


        when(smartBinRepository.findById(any(String.class))).thenReturn(Optional.of(smartBin));

        manageSmartBinsService.manageDisposalRequest(smartBin.getId(),disposalAmount);

        assertEquals(new Decimal128(BigDecimal.valueOf(50.0004)), smartBin.getCurrentCapacity());

        verify(smartBinRepository, times(1)).findById("1");
        verify(smartBinRepository, times(1)).save(smartBin);
    }

    @Test
    public void testManageDisposalRequestWhenSmartBinNotFound() {
        BigDecimal disposalAmount = new BigDecimal("30");

        when(smartBinRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(SmartBinNotFoundException.class, () -> manageSmartBinsService.manageDisposalRequest("InvalidID", disposalAmount));

        verify(smartBinRepository, times(1)).findById("InvalidID");
        verify(smartBinRepository, never()).save(any(SmartBin.class));
    }

    @Test
    public void testUpdateCapacityThreshold() throws Exception{
        Float newCapacityThreshold = 0.8f;

        SmartBin smartBin = new SmartBin();
        smartBin.setId("1");
        smartBin.setCapacityThreshold(0.5f);

        when(smartBinRepository.findById(any(String.class))).thenReturn(Optional.of(smartBin));

        manageSmartBinsService.updateCapacityThreshold(smartBin.getId(), newCapacityThreshold);

        assertEquals(newCapacityThreshold, smartBin.getCapacityThreshold());

        verify(smartBinRepository, times(1)).findById("1");
    }

    @Test
    public void testUpdateCapacityThresholdWhenSmartBinNotFound() {
        Float newCapacityThreshold = 0.8f;

        when(smartBinRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(SmartBinNotFoundException.class, () -> manageSmartBinsService.updateCapacityThreshold("InvalidID",newCapacityThreshold));

        verify(smartBinRepository, times(1)).findById("InvalidID");
        verify(smartBinRepository, never()).save(any(SmartBin.class));
    }

    @Test
    public void testResetCapacity() throws Exception {

        SmartBin smartBin = new SmartBin();
        smartBin.setId("1");
        smartBin.setCurrentCapacity(new Decimal128(100));

        when(smartBinRepository.findById(any(String.class))).thenReturn(Optional.of(smartBin));

        manageSmartBinsService.resetCapacity(smartBin.getId());
        assertEquals(new Decimal128(0), smartBin.getCurrentCapacity());

        verify(smartBinRepository, times(1)).findById("1");
    }

    @Test
    public void testResetCapacityWhenSmartBinNotFound() {

        when(smartBinRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(SmartBinNotFoundException.class, () -> manageSmartBinsService.resetCapacity("InvalidID"));

        verify(smartBinRepository, times(1)).findById("InvalidID");
        verify(smartBinRepository, never()).save(any(SmartBin.class));
    }
}

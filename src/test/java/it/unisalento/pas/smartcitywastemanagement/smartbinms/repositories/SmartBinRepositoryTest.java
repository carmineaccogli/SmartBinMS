package it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import org.bson.types.Decimal128;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@AutoConfigureDataMongo
public class SmartBinRepositoryTest {

    @Autowired
    private SmartBinRepository smartBinRepository;

    @Autowired
    private TypeRepository typeRepository;



    @Test
    public void testFindAll() {

        SmartBin bin1 = new SmartBin();
        bin1.setId("1");

        SmartBin bin2 = new SmartBin();
        bin2.setId("2");

        smartBinRepository.save(bin1);
        smartBinRepository.save(bin2);

        List<SmartBin> allBins = smartBinRepository.findAll();

        assertFalse(allBins.isEmpty());

        assertEquals(bin1.getId(),allBins.get(0).getId());
        assertEquals(bin2.getId(),allBins.get(1).getId());
    }


    @Test
    public void testFindById() {

        SmartBin bin = new SmartBin();
        bin.setId("TestID");

        smartBinRepository.save(bin);

        Optional<SmartBin> optBin = smartBinRepository.findById(bin.getId());

        assertTrue(optBin.isPresent());
        assertEquals(bin.getId(), optBin.get().getId());
    }

    @Test
    public void testExistsSmartBinByTypeAndPositionAndState() {

        Type type = new Type();
        type.setName("TestType");
        type.setId("TestTypeID");

        GeoJsonPoint point = new GeoJsonPoint(18,42);

        SmartBin bin = new SmartBin();
        bin.setType(type);
        bin.setPosition(point);
        bin.setState(SmartBin.State.ALLOCATED);
        bin.setId("TestID");

        smartBinRepository.save(bin);

        boolean exists = smartBinRepository.existsByTypeAndPositionAndState(type, point, SmartBin.State.ALLOCATED);

        assertTrue(exists);
    }

    @Test
    public void testFindByType() {

        Type type = new Type();
        type.setName("TestType");
        type.setId("TestTypeID");

        typeRepository.save(type);

        SmartBin bin1 = new SmartBin();
        bin1.setType(type);
        bin1.setId("1");

        SmartBin bin2 = new SmartBin();
        bin2.setType(type);
        bin2.setId("2");

        smartBinRepository.save(bin1);
        smartBinRepository.save(bin2);

        List<SmartBin> sameTypeBins = smartBinRepository.findByType("TestType");
        System.out.println(sameTypeBins.get(0));

        assertFalse(sameTypeBins.isEmpty());
        assertEquals(type.getName(), sameTypeBins.get(0).getType().getName());
        assertEquals(type.getName(), sameTypeBins.get(1).getType().getName());
    }


    @Test
    public void testFindByState() {
        SmartBin bin1 = new SmartBin();
        bin1.setState(SmartBin.State.ALLOCATED);
        bin1.setId("1");

        SmartBin bin2 = new SmartBin();
        bin2.setState(SmartBin.State.ALLOCATED);
        bin2.setId("2");

        smartBinRepository.save(bin1);
        smartBinRepository.save(bin2);

        List<SmartBin> allocatedBins = smartBinRepository.findByState("Allocated");

        assertFalse(allocatedBins.isEmpty());
        assertEquals(SmartBin.State.ALLOCATED, allocatedBins.get(0).getState());
        assertEquals(SmartBin.State.ALLOCATED, allocatedBins.get(1).getState());
    }


    @Test
    public void testFindyByCapacityRatio() {

        SmartBin bin1 = new SmartBin();
        bin1.setCurrentCapacity(new Decimal128(50));
        bin1.setTotalCapacity(new Decimal128(100));
        bin1.setId("1");

        SmartBin bin2 = new SmartBin();
        bin2.setCurrentCapacity(new Decimal128(60));
        bin2.setTotalCapacity(new Decimal128(100));
        bin2.setId("2");


        smartBinRepository.save(bin1);
        smartBinRepository.save(bin2);

        List<SmartBin> smartBins = smartBinRepository.findyByCapacityRatio(0.5);

        assertFalse(smartBins.isEmpty());
        assertEquals(2,smartBins.size());
    }


    @AfterEach
    public void cleanup() {
        smartBinRepository.deleteAll();
    }
}

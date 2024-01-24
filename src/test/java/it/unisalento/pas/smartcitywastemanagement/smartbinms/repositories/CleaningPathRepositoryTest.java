package it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.CleaningPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataMongoTest
public class CleaningPathRepositoryTest {

    @Autowired
    private CleaningPathRepository cleaningPathRepository;


    @Test
    public void testFindById() {

        CleaningPath path = new CleaningPath();
        path.setId("TestID");
        path.setDone(false);

        cleaningPathRepository.save(path);

        Optional<CleaningPath> optPath = cleaningPathRepository.findById(path.getId());

        assertTrue(optPath.isPresent());

        assertEquals(path.getId(), optPath.get().getId());
    }

    @Test
    public void findByDoneOrderByScheduledDate() {

        CleaningPath path1 = new CleaningPath();
        path1.setId("1");
        path1.setDone(false);

        CleaningPath path2 = new CleaningPath();
        path2.setId("2");
        path2.setDone(false);

        cleaningPathRepository.save(path1);
        cleaningPathRepository.save(path2);

        List<CleaningPath> pendingPaths = cleaningPathRepository.findByDoneOrderByScheduledDate(false);

        assertFalse(pendingPaths.isEmpty());
        assertFalse(pendingPaths.get(0).isDone());
        assertFalse(pendingPaths.get(1).isDone());
    }

    @Test
    public void testFindByScheduledDateGreaterThanEqualOrderByScheduledDate() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate1 = sdf.parse("2023-10-15");
        Date startDate2 = sdf.parse("2023-10-16");
        Date startDate3 = sdf.parse("2023-10-17");

        CleaningPath path1 = new CleaningPath();
        path1.setId("1");
        path1.setScheduledDate(startDate1);

        CleaningPath path2 = new CleaningPath();
        path2.setId("2");
        path2.setScheduledDate(startDate2);

        CleaningPath path3 = new CleaningPath();
        path3.setId("3");
        path3.setScheduledDate(startDate3);

        cleaningPathRepository.save(path1);
        cleaningPathRepository.save(path2);
        cleaningPathRepository.save(path3);

        List<CleaningPath> pendingPaths = cleaningPathRepository.findByScheduledDateGreaterThanEqualOrderByScheduledDate(startDate2);
        assertFalse(pendingPaths.isEmpty());
        assertEquals("2",pendingPaths.get(0).getId());
        assertEquals("3",pendingPaths.get(1).getId());

    }







    @AfterEach
    public void cleanup() {
        cleaningPathRepository.deleteAll();
    }

}

package it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.CleaningPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

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
    public void testFindByDone() {

        CleaningPath path1 = new CleaningPath();
        path1.setId("1");
        path1.setDone(false);

        CleaningPath path2 = new CleaningPath();
        path2.setId("2");
        path2.setDone(false);

        cleaningPathRepository.save(path1);
        cleaningPathRepository.save(path2);

        List<CleaningPath> pendingPaths = cleaningPathRepository.findByDone(false);

        assertFalse(pendingPaths.isEmpty());
        assertFalse(pendingPaths.get(0).isDone());
        assertFalse(pendingPaths.get(1).isDone());
    }







    @AfterEach
    public void cleanup() {
        cleaningPathRepository.deleteAll();
    }

}

package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.AllocationRequest;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.CleaningPathNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.CleaningPathRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {CleaningPathServiceImpl.class})
public class CleaningPathServiceImplTest {

    @InjectMocks
    private CleaningPathServiceImpl cleaningPathService;

    @MockBean
    private CleaningPathRepository cleaningPathRepository;


    @Test
    public void testSaveCleaningPath() {

        CleaningPath cleaningPath = new CleaningPath();
        cleaningPath.setId("TestID");

        when(cleaningPathRepository.save(cleaningPath)).thenReturn(cleaningPath);

        String createdId = cleaningPathService.saveCleaningPath(cleaningPath);

        assertFalse(cleaningPath.isDone());
        assertEquals("TestID", createdId);

        verify(cleaningPathRepository, times(1)).save(any(CleaningPath.class));
    }


    @Test
    public void testGetCleaningPathByStatus() {

            boolean status = true;

            List<CleaningPath> cleaningPaths = new ArrayList<>();
            CleaningPath path1 = new CleaningPath();
            path1.setDone(true);

            CleaningPath path2 = new CleaningPath();
            path2.setDone(true);

            cleaningPaths.add(path1);
            cleaningPaths.add(path2);

            when(cleaningPathRepository.findByDone(any(boolean.class))).thenReturn(cleaningPaths);

            List<CleaningPath> result = cleaningPathService.getCleaningPathByStatus(status);

            assertNotNull(result);
            assertEquals(cleaningPaths, result);


            verify(cleaningPathRepository, times(1)).findByDone(any(boolean.class));

    }

    @Test
    public void testUpdateCleaningPathStatus() {

        CleaningPath cleaningPath = new CleaningPath();
        cleaningPath.setId("TestID");
        cleaningPath.setDone(false);

        when(cleaningPathRepository.findById(any(String.class))).thenReturn(Optional.of(cleaningPath));

        assertDoesNotThrow(() -> cleaningPathService.updateCleaningPathStatus("TestID"));

        assertTrue(cleaningPath.isDone());

        verify(cleaningPathRepository, times(1)).findById("TestID");
        verify(cleaningPathRepository, times(1)).save(cleaningPath);
    }

    @Test
    public void testUpdateCleaningPathStatusWhenCleaningIsPathNotFound() {
        when(cleaningPathRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(CleaningPathNotFoundException.class, () -> cleaningPathService.updateCleaningPathStatus("InvalidPathID"));

        verify(cleaningPathRepository, times(1)).findById("InvalidPathID");
        verify(cleaningPathRepository, never()).save(any(CleaningPath.class));

    }


}

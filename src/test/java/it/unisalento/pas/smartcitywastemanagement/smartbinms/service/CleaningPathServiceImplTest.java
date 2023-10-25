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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public void testSaveCleaningPath() throws Exception{

        Date now = new Date();
        Date toSet = new Date(now.getTime() + (1000 * 60 * 60 * 24));

        CleaningPath cleaningPath = new CleaningPath();
        cleaningPath.setId("TestID");
        cleaningPath.setScheduledDate(toSet);


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

    @Test
    public void testGetCleaningPathToDoFrom() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse("16/10/2023");
        Date startDate1 = sdf.parse("17/10/2023");
        Date startDate2 = sdf.parse("18/10/2023");

        List<CleaningPath> mockCleaningPaths = new ArrayList<>();
        CleaningPath path1 = new CleaningPath();
        path1.setId("1");
        path1.setScheduledDate(startDate1);

        CleaningPath path2 = new CleaningPath();
        path2.setId("2");
        path2.setScheduledDate(startDate2);


        when(cleaningPathRepository.findByScheduledDateGreaterThanEqualOrderByScheduledDate(any(Date.class)))
                .thenReturn(mockCleaningPaths);

        List<CleaningPath> result = cleaningPathService.getCleaningPathToDoFrom(sdf.format(date));

        assertEquals(mockCleaningPaths, result);

        verify(cleaningPathRepository, times(1)).findByScheduledDateGreaterThanEqualOrderByScheduledDate(date);
    }


}

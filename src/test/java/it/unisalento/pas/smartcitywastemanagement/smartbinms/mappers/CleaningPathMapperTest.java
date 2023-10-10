package it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.CleaningPath;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.CleaningPathDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = {CleaningPathMapper.class})
@ExtendWith(MockitoExtension.class)
public class CleaningPathMapperTest {

    @InjectMocks
    private CleaningPathMapper cleaningPathMapper;


    @Test
    public void toCleaningPath() {

        List<String> path = new ArrayList<>();
        path.add("1");
        path.add("2");

        CleaningPathDTO cleaningPathDTO = new CleaningPathDTO();
        cleaningPathDTO.setSmartBinIDPath(path);
        cleaningPathDTO.setTimestamp(new Date());

        CleaningPath result = cleaningPathMapper.toCleaningPath(cleaningPathDTO);

        assertEquals(cleaningPathDTO.getSmartBinIDPath(),result.getSmartBinIDs());
        assertEquals(cleaningPathDTO.getTimestamp(),result.getTimestamp());
    }

    @Test
    public void toCleaningPathDTO() {

        List<String> path = new ArrayList<>();
        path.add("1");
        path.add("2");

        CleaningPath cleaningPath = new CleaningPath();
        cleaningPath.setDone(false);
        cleaningPath.setSmartBinIDs(path);
        cleaningPath.setTimestamp(new Date());
        cleaningPath.setId("TestID");

        CleaningPathDTO result = cleaningPathMapper.toCleaningPathDTO(cleaningPath);

        assertEquals(cleaningPath.getSmartBinIDs(),result.getSmartBinIDPath());
        assertEquals(cleaningPath.isDone(),result.isDone());
        assertEquals(cleaningPath.getId(),result.getId());
        assertEquals(cleaningPath.getTimestamp(),result.getTimestamp());

    }
}

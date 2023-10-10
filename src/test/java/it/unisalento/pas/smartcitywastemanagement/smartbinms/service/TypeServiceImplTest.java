package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.TypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {TypeServiceImpl.class})
public class TypeServiceImplTest {


    @InjectMocks
    private TypeServiceImpl typeService;

    @MockBean
    private TypeRepository typeRepository;




    @Test
    public void testSaveType()  {

        Type type = new Type();
        type.setId("TestID");
        type.setName("TestType");

        when(typeRepository.save(any(Type.class))).thenReturn(type);

        String typeId = typeService.saveType(type);
        assertNotNull(typeId);
        assertEquals(type.getId(), typeId);

        verify(typeRepository, times(1)).save(type);
    }

    @Test
    public void testSaveType_DuplicateKeyException() {

        Type type = new Type();
        type.setId("TestID");
        type.setName("ExampleType");

        when(typeRepository.save(any(Type.class))).thenThrow(DuplicateKeyException.class);

        assertThrows(DuplicateKeyException.class, () -> typeService.saveType(type));
    }

    @Test
    public void testGetAllTypeNames() {

        Type type1 = new Type();
        type1.setId("TestID");

        Type type2 = new Type();
        type2.setId("TestID");

        List<Type> typeList = new ArrayList<>();
        typeList.add(type1);
        typeList.add(type2);


        when(typeRepository.findAll()).thenReturn(typeList);

        List<Type> result = typeService.getAllTypeNames();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(typeRepository, times(1)).findAll();
    }
}

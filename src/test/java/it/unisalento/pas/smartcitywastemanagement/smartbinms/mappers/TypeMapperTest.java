package it.unisalento.pas.smartcitywastemanagement.smartbinms.mappers;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.dto.TypeDTO;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.restcontrollers.RequestRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {TypeMapper.class})
@ExtendWith(MockitoExtension.class)
public class TypeMapperTest {

    private TypeMapper typeMapper;

    @BeforeEach
    public void setUp() {
        typeMapper = new TypeMapper();
    }

    @Test
    public void testToType()  {
        TypeDTO typeDTO = new TypeDTO();
        typeDTO.setName("TestName");
        typeDTO.setDescription("TestDescription");
        typeDTO.setColor("TestColor");

        // Eseguire il metodo da testare
        Type result = typeMapper.toType(typeDTO);

        // Verificare che il risultato sia corretto
        assertEquals("TestName", result.getName());
        assertEquals("TestDescription", result.getDescription());
        assertEquals("TestColor", result.getColor());
    }

    @Test
    public void testToTypeDTO()  {

        Type type = new Type();
        type.setName("TestName");
        type.setDescription("TestDescription");
        type.setColor("TestColor");

        // Eseguire il metodo da testare
        TypeDTO result = typeMapper.toTypeDTO(type);

        // Verificare che il risultato sia corretto
        assertEquals("TestName", result.getName());
        assertEquals("TestDescription", result.getDescription());
        assertEquals("TestColor", result.getColor());


    }


}

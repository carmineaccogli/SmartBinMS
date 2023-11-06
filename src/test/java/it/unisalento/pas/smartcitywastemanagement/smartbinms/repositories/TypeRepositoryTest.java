package it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class TypeRepositoryTest {


    @Autowired
    private TypeRepository typeRepository;


    @Test
    public void testFindAllTypes() {

        Type type1 = new Type();
        type1.setName("Type1");
        type1.setId("1");

        Type type2 = new Type();
        type2.setName("Type2");
        type2.setId("2");

        typeRepository.save(type1);
        typeRepository.save(type2);


        List<String> types = typeRepository.findAllTypes();

        assertFalse(types.isEmpty());
    }

    @Test
    public void testFindAll() {

        Type type1 = new Type();
        type1.setName("Type1");
        type1.setId("1");

        Type type2 = new Type();
        type2.setName("Type2");
        type2.setId("2");

        typeRepository.save(type1);
        typeRepository.save(type2);

        List<Type> types = typeRepository.findAll();

        assertFalse(types.isEmpty());

        assertEquals(type1.getName(),types.get(0).getName());
        assertEquals(type2.getName(),types.get(1).getName());
    }

    @Test
    public void testFindByTypeName() {

        Type type = new Type();
        type.setName("Type");
        type.setId("TestID");

        typeRepository.save(type);

        Optional<Type> optType = typeRepository.findByTypeName("Type");

        assertTrue(optType.isPresent());
        assertEquals("Type", optType.get().getName());
    }

    @AfterEach
    public void cleanup() {
        typeRepository.deleteAll();
    }




}

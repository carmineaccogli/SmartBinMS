package it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TypeRepository extends MongoRepository<Type, String> {

    // Retrieve all the type values of the collection "Type"
    @Query(value = "{}", fields = "{ '_id': 0, 'name': 1}")
    List<String> findAllTypes();

    @Query(value="{'name': {$regex:  '^?0$', $options:  'i'}}" )
    Optional<Type> findByTypeName(String name);

    List<Type> findAll();
}

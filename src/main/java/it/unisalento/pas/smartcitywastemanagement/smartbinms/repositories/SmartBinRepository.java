package it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SmartBinRepository extends MongoRepository<SmartBin, String> {


    public Optional<SmartBin> findById(String id);

    public List<SmartBin> findAll();



    // Restituisce una lista di Smart Bin allocati in una determinata posizione e che siano segnalati come attivi
    public List<SmartBin> findByPositionAndState(GeoJsonPoint position, String state);

    Boolean existsByTypeAndPositionAndState(Type type, GeoJsonPoint position, SmartBin.State state);

    @Query(value="{'state': {$regex:  '^?0$', $options:  'i'}}" )
    public List<SmartBin> findByState(String stateName);

    @Aggregation(pipeline= {
            "{$addFields: { fk: {$objectToArray:  '$$ROOT.type'}}}",
            "{ $lookup:  " +
                    "{from: 'type', localField: 'fk.1.v', foreignField: '_id',as: 'typeInfo'}}",
            "{$unwind: {path: '$typeInfo'}}",
            "{$match:  {'typeInfo.name': ?0}}"
    })
    public List<SmartBin> findByType(String typeName);


    @Aggregation(pipeline = {
            "{ $addFields: { ratio: { $divide: ['$currentCapacity', '$totalCapacity'] } } }",
            "{ $match: { ratio: { $gte: ?0 } } }"
    })
    public List<SmartBin> findyByCapacityRatio(double capacityRatio);






}

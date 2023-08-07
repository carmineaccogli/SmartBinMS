package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;


import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService{

    @Autowired
    private TypeRepository typeRepository;

    public String saveType(Type type) {
        Type createdType = typeRepository.save(type);
        return createdType.getId();
    }

    public List<Type> getAllTypeNames() {
        return typeRepository.findAll();
    }
}

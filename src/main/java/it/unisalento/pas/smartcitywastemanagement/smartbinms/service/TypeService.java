package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.Type;
import jdk.dynalink.linker.LinkerServices;

import java.util.List;

public interface TypeService {

    String saveType(Type type);

    List<Type> getAllTypeNames();
}

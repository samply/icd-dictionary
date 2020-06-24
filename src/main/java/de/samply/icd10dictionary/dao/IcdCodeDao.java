package de.samply.icd10dictionary.dao;

import de.samply.icd10dictionary.model.IcdCode;

import java.util.List;
import java.util.Optional;

public interface IcdCodeDao {

    int insert(IcdCode icdCode);

    Optional<IcdCode> selectIcdCodeByCode(String codeParam);

    int deleteByCode(String code);

    List<IcdCode> retrieveAll();

    int deleteAll();

    int count();

    List<IcdCode> retrieveCodesBySearchword(String searchword);
}

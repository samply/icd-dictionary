package de.samply.icd10dictionary.dao;

import de.samply.icd10dictionary.model.IcdCode;
import java.util.List;
import java.util.Optional;

/**
 * Main DAO.
 */
public interface IcdCodeDao {

  void insert(IcdCode icdCode);

  Optional<IcdCode> selectIcdCodeByCode(String codeParam);

  int deleteByCode(String code);

  List<IcdCode> retrieveAll();

  int deleteAll();

  int count();

  List<IcdCode> retrieveCodesByQueryText(String queryText);
}

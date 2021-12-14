package de.samply.icd10dictionary.service;

import de.samply.icd10dictionary.dao.IcdCodeDao;
import de.samply.icd10dictionary.model.IcdCode;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Main Service.
 */
@Service
public class SearchIcdCodeService {

  private final IcdCodeDao icdCodeDao;

  public SearchIcdCodeService(IcdCodeDao icdCodeDao) {
    this.icdCodeDao = icdCodeDao;
  }

  public List<IcdCode> retrieveCodesByQueryText(String queryText) {
    return this.icdCodeDao.retrieveCodesByQueryText(queryText);
  }
}

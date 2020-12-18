package de.samply.icd10dictionary.service;

import de.samply.icd10dictionary.dao.IcdCodeDao;
import de.samply.icd10dictionary.model.IcdCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SearchIcdCodeService {

  private final IcdCodeDao icdCodeDao;

  @Autowired
  public SearchIcdCodeService(@Qualifier("postgres") IcdCodeDao icdCodeDao) {
    this.icdCodeDao = icdCodeDao;
  }

  public List<IcdCode> retrieveCodesByQueryText(String queryText) {
    return this.icdCodeDao.retrieveCodesByQueryText(queryText);
  }
}

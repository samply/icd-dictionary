package de.samply.icd10dictionary.service;


import de.samply.icd10dictionary.dao.IcdCodeDao;
import de.samply.icd10dictionary.model.IcdCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchIcdCodeService {

    private final IcdCodeDao icdCodeDao;

    @Autowired
    public SearchIcdCodeService(@Qualifier("postgres") IcdCodeDao icdCodeDao) {
        this.icdCodeDao = icdCodeDao;
    }

    public List<IcdCode> retrieveCodesBySearchword(String searchword) {
        return this.icdCodeDao.retrieveCodesBySearchword(searchword);
    }

}

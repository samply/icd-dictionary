package de.samply.icd10dictionary.service;

import de.samply.icd10dictionary.dao.IcdCodeDao;
import de.samply.icd10dictionary.model.IcdCode;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoadIcdCodeService {

    private final IcdCodeDao icdCodeDao;

    @Autowired
    public LoadIcdCodeService(@Qualifier("postgres") IcdCodeDao icdCodeDao) {
        this.icdCodeDao = icdCodeDao;
    }

    public int load(CodeSystem codeSystem) {
        if (this.icdCodeDao.count() > 0) {
            return -1;
        }

        creaeIcdCodes(codeSystem);
        return 0;
    }

    private void creaeIcdCodes(CodeSystem codeSystem) {
        codeSystem.getConcept().forEach(
                concept -> this.icdCodeDao.insert(createIcdCode(concept))
        );
    }

    private IcdCode createIcdCode(Concept concept) {
        return new IcdCode(
                concept.getCode(),
                determineKind(concept),
                concept.getDefinition(),
                concept.getDisplay(),
                determineParentCode(concept),
                StringUtils.join(determineChildCodes(concept), ','));
    }

    private Collection<String> determineChildCodes(Concept concept) {
        return concept.getProperty().stream()
                .filter(property -> property.getCode().equals("child"))
                .map(Property::getValueCode)
                .collect(Collectors.toList());
    }

    private String determineParentCode(Concept concept) {
        return findProperty(concept, "parent");
    }

    private String determineKind(Concept concept) {
        return findProperty(concept, "kind");
    }

    private String findProperty(Concept concept, String propertyCode) {
        Optional<Property> parentPropertyMayBe = concept.getProperty().stream()
                .filter(property -> property.getCode().equals(propertyCode)).findFirst();
        if (parentPropertyMayBe.isEmpty()) {
            return null;
        } else {
            return parentPropertyMayBe.get().getValueCode();
        }
    }
}

package de.samply.icd10dictionary.service;

import de.samply.icd10dictionary.dao.IcdCodeDao;
import de.samply.icd10dictionary.model.IcdCode;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Data load service.
 */
@Service
public class LoadIcdCodeService {

  private static final Logger logger = LoggerFactory.getLogger(LoadIcdCodeService.class);

  private final IcdCodeDao icdCodeDao;

  public LoadIcdCodeService(IcdCodeDao icdCodeDao) {
    this.icdCodeDao = icdCodeDao;
  }

  /**
   * Imports the given CodeSystem into the database.
   *
   * @param codeSystem the CodeSystem
   * @return {@code DB_NOT_EMPTY} if the database contains already some codes
   */
  public ErrorCode load(CodeSystem codeSystem) {
    if (icdCodeDao.count() > 0) {
      return ErrorCode.DB_NOT_EMPTY;
    }

    createIcdCodes(codeSystem);
    return ErrorCode.OK;
  }

  private void createIcdCodes(CodeSystem codeSystem) {
    codeSystem.concept()
        .stream().map(LoadIcdCodeService::createIcdCode)
        .forEach(optionalIcdCode -> {
          if (optionalIcdCode.isPresent()) {
            logger.debug("load ICD cod {}", optionalIcdCode.get());
            icdCodeDao.insert(optionalIcdCode.get());
          } else {
            logger.warn("skip non-buildable ICD code");
          }
        });
  }

  private static Optional<IcdCode> createIcdCode(Concept concept) {
    return findProperty(concept, "kind")
        .map(kind -> new IcdCode(
            concept.code(),
            kind,
            concept.definition(),
            concept.display(),
            findProperty(concept, "parent").orElse(null),
            StringUtils.join(determineChildCodes(concept), ',')));
  }

  private static Collection<String> determineChildCodes(Concept concept) {
    return concept.property().stream()
        .filter(property -> property.code().equals("child"))
        .map(Property::valueCode)
        .collect(Collectors.toList());
  }

  private static Optional<String> findProperty(Concept concept, String propertyCode) {
    return concept.property().stream()
        .filter(property -> property.code().equals(propertyCode))
        .findFirst()
        .map(Property::valueCode);
  }

  /**
   * Error codes.
   */
  public enum ErrorCode {
    OK,
    FILE_NOT_FOUND,
    DB_NOT_EMPTY,
    OTHER
  }
}

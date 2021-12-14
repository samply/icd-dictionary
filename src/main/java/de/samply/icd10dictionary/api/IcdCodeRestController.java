package de.samply.icd10dictionary.api;

import de.samply.icd10dictionary.model.IcdCode;
import de.samply.icd10dictionary.model.ValueSet;
import de.samply.icd10dictionary.model.ValueSetEntry;
import de.samply.icd10dictionary.model.ValueSetExpansion;
import de.samply.icd10dictionary.service.CodeSystem;
import de.samply.icd10dictionary.service.LoadIcdCodeService;
import de.samply.icd10dictionary.service.SearchIcdCodeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * READ API.
 */
@RestController
@CrossOrigin
public class IcdCodeRestController {

  private static final String URL_ICD_10_GM = "http://hl7.org/fhir/sid/icd-10-gm";

  private final LoadIcdCodeService loadIcdCodeService;
  private final SearchIcdCodeService searchIcdCodeService;

  @Autowired
  public IcdCodeRestController(
      LoadIcdCodeService loadIcdCodeService, SearchIcdCodeService searchIcdCodeService) {
    this.loadIcdCodeService = loadIcdCodeService;
    this.searchIcdCodeService = searchIcdCodeService;
  }

  @GetMapping("/health")
  public ResponseEntity<String> check() {
    return new ResponseEntity<>("System running", HttpStatus.OK);
  }

  /**
   * Loads ICD-10 catalog from specified {@code codeSystem}.
   *
   * @param codeSystem the CodeSystem
   * @return ResponseEntity Http status
   */
  @PostMapping("api/v1/icd/load")
  public ResponseEntity<String> load(@RequestBody CodeSystem codeSystem) {
    try {
      LoadIcdCodeService.ErrorCode errorCode = this.loadIcdCodeService.load(codeSystem);
      switch (errorCode) {
        case OK:
          break;
        case DB_NOT_EMPTY:
          return new ResponseEntity<>("Database not empty", HttpStatus.CONFLICT);
        default:
          return new ResponseEntity<>("Unspecified error", HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>("Data imported", HttpStatus.OK);
  }

  /**
   * Implements (partially) the $expand operator - see https://www.hl7.org/fhir/operation-valueset-expand.html
   *
   * @param url    for the ICD-10-GM codesystem (must be 'http://hl7.org/fhir/sid/icd-10-gm')
   * @param filter term to search in display entries
   * @return ValueSet containing all filtered codes
   */
  @GetMapping("fhir/ValueSet/$expand")
  public ValueSet expand(@RequestParam String url, @RequestParam String filter) {
    if (!URL_ICD_10_GM.equalsIgnoreCase(url)) {
      return new ValueSet();
    }
    List<IcdCode> icdCodes = this.searchIcdCodeService.retrieveCodesByQueryText(filter);
    return createValueSet(icdCodes);
  }

  private ValueSet createValueSet(List<IcdCode> icdCodes) {
    return new ValueSet(new ValueSetExpansion(icdCodes.stream()
        .map(icdCode -> new ValueSetEntry(icdCode.code(), icdCode.display()))
        .toList()));
  }
}

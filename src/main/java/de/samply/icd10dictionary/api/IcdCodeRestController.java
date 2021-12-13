package de.samply.icd10dictionary.api;

import de.samply.icd10dictionary.model.IcdCode;
import de.samply.icd10dictionary.model.ValueSet;
import de.samply.icd10dictionary.model.ValueSetEntry;
import de.samply.icd10dictionary.service.LoadIcdCodeService;
import de.samply.icd10dictionary.service.SearchIcdCodeService;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
   * Loads ICD-10 catalog from specified location 'clamlFileUri' on server.
   *
   * @param clamlFileUri Location of content file
   * @return ResponseEntity Http status
   */
  @PostMapping("api/v1/icd/load")
  public ResponseEntity<String> loadFromFile(@RequestBody String clamlFileUri) {
    try {
      LoadIcdCodeService.ErrorCode errorCode = this.loadIcdCodeService.load(clamlFileUri);
      switch (errorCode) {
        case OK:
          break;
        case FILE_NOT_FOUND:
          return new ResponseEntity<>("File not found", HttpStatus.BAD_REQUEST);
        case DB_NOT_EMPTY:
          return new ResponseEntity<>("Database not empty", HttpStatus.CONFLICT);
        case OTHER:
        default:
          return new ResponseEntity<>("Unspecified error", HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>("File imported", HttpStatus.OK);
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
    if (!StringUtils.equalsIgnoreCase(url, URL_ICD_10_GM)) {
      return new ValueSet();
    }
    List<IcdCode> icdCodes = this.searchIcdCodeService.retrieveCodesByQueryText(filter);
    return createValueSet(icdCodes);
  }

  private ValueSet createValueSet(List<IcdCode> icdCodes) {
    ValueSet valueSet = new ValueSet();
    List<ValueSetEntry> entries =
        icdCodes.stream()
            .map(
                icdCode -> {
                  ValueSetEntry entry = new ValueSetEntry();
                  entry.setCode(icdCode.getCode());
                  entry.setDisplay(icdCode.getDisplay());
                  return entry;
                })
            .collect(Collectors.toList());
    valueSet.getExpansion().setContains(entries);

    return valueSet;
  }
}

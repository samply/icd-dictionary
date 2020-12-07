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

  @PostMapping("api/v1/icd/load")
  public void loadFromFile(@RequestBody String clamlContent) {
    this.loadIcdCodeService.load(clamlContent);
  }

  /**
   * Implements (partially) the $expand operator - see
   * https://www.hl7.org/fhir/operation-valueset-expand.html
   *
   * @param url for the ICD-10-GM codesystem (must be 'http://hl7.org/fhir/sid/icd-10-gm')
   * @param filter term to search in display entries
   * @return ValueSet containing all filtered codes
   */
  @GetMapping("fhir/ValueSet/$expand")
  public ValueSet retrieveCodesBySearchword(@RequestParam String url, @RequestParam String filter) {
    if (!StringUtils.equalsIgnoreCase(url, URL_ICD_10_GM)) {
      return new ValueSet();
    }

    String searchword = StringUtils.trim(filter).replaceAll("\\s+","");
    List<IcdCode> icdCodes = this.searchIcdCodeService.retrieveCodesBySearchword(searchword);

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

package de.samply.icd10dictionary.model;

import java.util.List;

/**
 * FHIR ValueSet.
 */
public record ValueSetExpansion(List<ValueSetEntry> contains) {

  ValueSetExpansion() {
    this(List.of());
  }
}

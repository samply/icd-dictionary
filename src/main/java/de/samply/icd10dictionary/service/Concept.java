package de.samply.icd10dictionary.service;

import java.util.List;

/**
 * FHIR CodeSystem.
 */
public record Concept(String code, String display, String definition, List<Property> property) {

  @Override
  public List<Property> property() {
    return property == null ? List.of() : property;
  }
}

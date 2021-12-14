package de.samply.icd10dictionary.service;

import java.util.List;

/**
 * FHIR CodeSystem.
 */
public record CodeSystem(List<Concept> concept) {

  @Override
  public List<Concept> concept() {
    return concept == null ? List.of() : concept;
  }
}

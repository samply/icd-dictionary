package de.samply.icd10dictionary.model;

/**
 * FHIR ValueSet.
 */
public record ValueSet(ValueSetExpansion expansion) {

  public ValueSet() {
    this(new ValueSetExpansion());
  }
}

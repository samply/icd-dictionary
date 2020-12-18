package de.samply.icd10dictionary.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValueSet {
  private ValueSetExpansion expansion = new ValueSetExpansion();
}

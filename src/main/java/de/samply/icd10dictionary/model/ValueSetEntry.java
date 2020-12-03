package de.samply.icd10dictionary.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValueSetEntry {
  private String code;
  private String display;
}

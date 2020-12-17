package de.samply.icd10dictionary.service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Property {
  private String code;
  private String valueString;
  private String valueCode;
}

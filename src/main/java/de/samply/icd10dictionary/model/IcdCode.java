package de.samply.icd10dictionary.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IcdCode {

  private String code;
  private String kind;
  private String display;
  private String definition;
  private String parentCode;
  private String childCodes;
}

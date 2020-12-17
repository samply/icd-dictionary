package de.samply.icd10dictionary.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Concept {
  private String code;
  private String display;
  private String definition;
  private List<Property> property = new ArrayList<>();
}

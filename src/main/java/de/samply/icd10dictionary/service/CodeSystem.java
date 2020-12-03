package de.samply.icd10dictionary.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CodeSystem {
  private List<Concept> concept = new ArrayList<>();
}

package de.samply.icd10dictionary.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValueSetExpansion {
  private List<ValueSetEntry> contains = new ArrayList<>();
}

package de.samply.icd10dictionary.model;

import java.util.ArrayList;
import java.util.List;

public class ValueSetExpansion {
    private List<ValueSetEntry> contains = new ArrayList<>();

    public List<ValueSetEntry> getContains() {
        return contains;
    }

    public void setContains(List<ValueSetEntry> contains) {
        this.contains = contains;
    }
}

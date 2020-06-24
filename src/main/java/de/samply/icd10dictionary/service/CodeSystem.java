package de.samply.icd10dictionary.service;

import java.util.ArrayList;
import java.util.List;

public class CodeSystem {
    private List<Concept> concept = new ArrayList<>();

    public List<Concept> getConcept() {
        return concept;
    }

    public void setConcept(List<Concept> concept) {
        this.concept = concept;
    }
}

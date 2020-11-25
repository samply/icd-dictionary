package de.samply.icd10dictionary.model;

public class ValueSet {
    private ValueSetExpansion expansion = new ValueSetExpansion();

    public ValueSetExpansion getExpansion() {
        return expansion;
    }

    public void setExpansion(ValueSetExpansion expansion) {
        this.expansion = expansion;
    }
}

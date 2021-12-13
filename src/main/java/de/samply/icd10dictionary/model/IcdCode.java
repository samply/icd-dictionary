package de.samply.icd10dictionary.model;

/**
 * A row of the IcdCode database table.
 */
public record IcdCode(String code, String kind, String display, String definition,
                      String parentCode, String childCodes) {

}

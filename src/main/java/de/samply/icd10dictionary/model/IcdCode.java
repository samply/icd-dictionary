package de.samply.icd10dictionary.model;

public class IcdCode {

    private String code;
    private String kind;
    private String display;
    private String definition;
    private String parentCode;
    private String childCodes;

    public IcdCode(String code, String kind, String display, String definition, String parentCode, String childCodes) {
        this.code = code;
        this.kind = kind;
        this.display = display;
        this.definition = definition;
        this.parentCode = parentCode;
        this.childCodes = childCodes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getChildCodes() {
        return childCodes;
    }

    public void setChildCodes(String childCodes) {
        this.childCodes = childCodes;
    }
}

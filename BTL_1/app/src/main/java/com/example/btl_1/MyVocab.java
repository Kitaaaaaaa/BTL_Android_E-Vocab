package com.example.btl_1;

public class MyVocab {
    int id;
    String terminology, type, definition, example;
    public MyVocab(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTerminology() {
        return terminology;
    }

    public void setTerminology(String terminology) {
        this.terminology = terminology;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public MyVocab(String terminology, String definition) {
        this.terminology = terminology;
        this.definition = definition;
    }

    public MyVocab(String terminology, String type, String definition) {
        this.terminology = terminology;
        this.type = type;
        this.definition = definition;
    }

    public MyVocab(int id, String terminology, String type, String definition) {
        this.id = id;
        this.terminology = terminology;
        this.type = type;
        this.definition = definition;
    }

    public MyVocab(String terminology, String type, String definition, String example) {
        this.terminology = terminology;
        this.type = type;
        this.definition = definition;
        this.example = example;
    }

    public MyVocab(int id, String terminology, String type, String definition, String example) {
        this.id = id;
        this.terminology = terminology;
        this.type = type;
        this.definition = definition;
        this.example = example;
    }
}

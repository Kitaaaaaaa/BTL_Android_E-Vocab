package com.example.btl_thuong;

import java.io.Serializable;

public class VocabItem implements Serializable {
    int vocabID, folderID;
    String terminology, type, definition, example;
    public VocabItem(){
    }

    public VocabItem(String terminology, String definition) {
        this.terminology = terminology;
        this.definition = definition;
    }

    public VocabItem(String terminology, String type, String definition) {
        this.terminology = terminology;
        this.type = type;
        this.definition = definition;
    }

    public VocabItem(String terminology, String type, String definition, String example, int folderID) {
        this.terminology = terminology;
        this.type = type;
        this.definition = definition;
        this.example = example;
        this.folderID = folderID;
    }

    public VocabItem(String terminology, String type, String definition, String example) {
        this.terminology = terminology;
        this.type = type;
        this.definition = definition;
        this.example = example;
    }

    public VocabItem(int vocabID, String terminology, String type, String definition, String example, int folderID) {
        this.vocabID = vocabID;
        this.terminology = terminology;
        this.type = type;
        this.definition = definition;
        this.example = example;
        this.folderID = folderID;
    }

    public int getVocabID() {
        return vocabID;
    }

    public void setVocabID(int vocabID) {
        this.vocabID = vocabID;
    }

    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
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
}

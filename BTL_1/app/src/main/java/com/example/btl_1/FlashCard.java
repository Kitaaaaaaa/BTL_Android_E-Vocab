package com.example.btl_1;

public class FlashCard {
    private String term, type, definition;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FlashCard(String term, String type, String definition) {
        this.term = term;
        this.type = type;
        this.definition = definition;
    }

    public FlashCard(String term, String definition) {
        this.term = term;
        this.definition = definition;
    }
}

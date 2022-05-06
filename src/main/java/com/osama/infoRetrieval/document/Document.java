package com.osama.infoRetrieval.document;

import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Document{
    private final long id;
    private List<Token> terms;
    private Map<String, String> info;

    protected Document(long id, List<Token> terms){
        this.id = id;
        this.terms = terms;

    }

    protected Document(long id, List<Token> terms, Map<String, String> info){
        this(id, terms);
        this.info = info;
    }

    protected Document(Document doc){
        this.id = doc.getId();
        this.terms = new ArrayList<Token>(doc.getTerms());
    }

    public long getId() {
        return id;
    }

    public List<Token> getTerms() {
        return terms;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", terms=" + terms +
                ", info=" + info +
                '}';
    }
}

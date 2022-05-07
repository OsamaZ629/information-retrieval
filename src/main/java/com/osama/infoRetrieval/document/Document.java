package com.osama.infoRetrieval.document;

import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.*;

public class Document implements IsTokenizable{
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
        this.terms = new ArrayList<>(doc.getTokens());
    }

    public long getId() {
        return id;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    @Override
    public Collection<Token> getTokens() {
        return this.terms;
    }

    @Override
    public void setTokens(Collection<Token> tokens){
        this.terms = (List<Token>) tokens;
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

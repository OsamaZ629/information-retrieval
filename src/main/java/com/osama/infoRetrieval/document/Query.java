package com.osama.infoRetrieval.document;

import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.List;

public class Query {
    private final List<Token> terms;

    public Query(List<Token> processedQuery) {
        this.terms = processedQuery;
    }


    public List<Token> getTerms() {
        return terms;
    }

}

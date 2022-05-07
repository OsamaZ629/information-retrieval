package com.osama.infoRetrieval.document;

import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.Collection;
import java.util.List;

public class Query implements IsTokenizable{
    private List<Token> terms;

    public Query(List<Token> processedQuery) {
        this.terms = processedQuery;
    }

    @Override
    public Collection<Token> getTokens() {
        return this.terms;
    }

    @Override
    public void setTokens(Collection<Token> tokens){
        this.terms = (List<Token>) tokens;
    }

}

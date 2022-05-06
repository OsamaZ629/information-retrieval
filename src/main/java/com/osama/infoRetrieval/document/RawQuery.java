package com.osama.infoRetrieval.document;


import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.List;

public class RawQuery {
    private final String content;

    protected RawQuery(String content, List<Token> processedQuery) {
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }
}

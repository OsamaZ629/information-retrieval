package com.osama.infoRetrieval.document;

import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.Collection;

public interface IsTokenizable {
    Collection<Token> getTokens();

    void setTokens(Collection<Token> newTokens);
}

package com.osama.infoRetrieval.processing.tokenization;

import com.osama.infoRetrieval.document.RawDocument;

import java.util.List;

public interface Tokenizer {
    List<Token> tokenize(RawDocument doc);
}

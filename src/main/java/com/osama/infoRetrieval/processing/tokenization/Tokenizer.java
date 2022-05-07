package com.osama.infoRetrieval.processing.tokenization;

import com.osama.infoRetrieval.document.HasContent;

import java.util.List;

public interface Tokenizer {
    List<Token> tokenize(HasContent doc);
}

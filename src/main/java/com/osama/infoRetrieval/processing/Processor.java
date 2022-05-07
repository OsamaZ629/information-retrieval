package com.osama.infoRetrieval.processing;

import com.osama.infoRetrieval.document.IsTokenizable;
import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.Collection;

public interface Processor {
    Collection<Token> process(IsTokenizable doc);
}

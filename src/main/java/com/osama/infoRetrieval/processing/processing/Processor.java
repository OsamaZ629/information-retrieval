package com.osama.infoRetrieval.processing.processing;

import com.osama.infoRetrieval.document.IsTokenizable;
import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.Collection;
import java.util.List;

public abstract class Processor {
    public abstract Collection<Token> process(IsTokenizable doc);
    public abstract Collection<Token> process(Collection<Token> tokens);
    protected abstract void processNoCopy(List<Token> tokens);
}

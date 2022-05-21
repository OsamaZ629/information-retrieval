package com.osama.infoRetrieval.processing.processing;

import com.osama.infoRetrieval.document.IsTokenizable;
import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RemovePunctuationProcessor extends Processor {

    @Override
    public Collection<Token> process(IsTokenizable doc) {
        return process(doc.getTokens());
    }

    @Override
    public Collection<Token> process(Collection<Token> tokens) {
        Collection<Token> res = new ArrayList<>(tokens.size());

        for (Token term: tokens){
            String newTerm = term.getContent().replaceAll("\\p{Punct}", "");
            res.add(new Token(newTerm));
        }

        return res;
    }

    @Override
    protected void processNoCopy(List<Token> tokens) {
        for (Token term: tokens){
            term.setContent(term.getContent().replaceAll("\\p{Punct}", ""));
        }
    }
}

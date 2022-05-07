package com.osama.infoRetrieval.processing;

import com.osama.infoRetrieval.document.IsTokenizable;
import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class ToLowerCaseProcessor implements Processor {
    @Override
    public Collection<Token> process(IsTokenizable doc) {
        Collection<Token> docTokens = doc.getTokens();
        Collection<Token> res = new ArrayList<>(docTokens.size());

        for (Token term: docTokens){
            String newTerm = term.getContent().toLowerCase(Locale.ROOT);
            res.add(new Token(newTerm));
        }

        return res;
    }
}

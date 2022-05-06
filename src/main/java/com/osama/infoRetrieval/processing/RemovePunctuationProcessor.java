package com.osama.infoRetrieval.processing;

import com.osama.infoRetrieval.document.Document;
import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.List;

public class RemovePunctuationProcessor implements DocumentProcessor {

    @Override
    public Document process(Document doc) {
        List<Token> docTokens = doc.getTerms();
        for (Token term: docTokens){
            term.setContent(term.getContent().replaceAll("\\p{Punct}", ""));
        }

        return doc;
    }
}

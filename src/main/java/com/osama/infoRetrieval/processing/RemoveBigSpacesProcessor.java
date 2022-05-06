package com.osama.infoRetrieval.processing;

import com.osama.infoRetrieval.document.Document;
import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.List;

public class RemoveBigSpacesProcessor implements DocumentProcessor {
    @Override
    public Document process(Document doc) {
        List<Token> docTokens = doc.getTerms();
        for (Token term: docTokens){
            if (term.getContent().contains("present")){
                System.out.println(term);
            }
            term.setContent(term.getContent().replaceAll("\\s", " ").trim());
        }

        return doc;
    }
}

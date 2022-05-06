package com.osama.infoRetrieval.processing.tokenization;

import com.osama.infoRetrieval.document.RawDocument;

import java.util.ArrayList;
import java.util.List;

public class WhiteSpaceTokenizer implements Tokenizer{
    @Override
    public List<Token> tokenize(RawDocument doc) {
        String[] parts = doc.getMainContent().split(" ");
        List<Token> res = new ArrayList<>(parts.length);

        for (String str: parts){
            res.add(new Token(str));
        }
        return res;
    }
}

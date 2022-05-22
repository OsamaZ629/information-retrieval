package com.osama.infoRetrieval.processing.tokenization;

import com.osama.infoRetrieval.document.HasContent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BooleanTokenizer implements Tokenizer{
    public static final Pattern ALL_PATTERN = Pattern.compile("(?=(\\b(and|or|not)\\b))");

    @Override
    public List<Token> tokenize(HasContent doc) {
        String[] parts = ALL_PATTERN.split(doc.getContent());
        List<Token> res = new ArrayList<>(parts.length);

        for (String str: parts){
            if (str.startsWith("and ") || str.startsWith("or ") || str.startsWith("not ")){
                String[] strParts = str.split(" ");
                res.add(new Token(strParts[0]));
                res.add(new Token(str.replace(strParts[0] + " ", "")));
            } else {
                res.add(new Token(str));
            }
        }
        return res;
    }
}

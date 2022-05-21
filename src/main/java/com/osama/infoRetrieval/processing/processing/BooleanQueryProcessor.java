package com.osama.infoRetrieval.processing.processing;

import com.osama.infoRetrieval.document.IsTokenizable;
import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BooleanQueryProcessor extends Processor {
    private static final Token AND_TOKEN = new Token("and");
    private static final Token OR_TOKEN = new Token("or");
    private static final Token NOT_TOKEN = new Token("not");

    @Override
    public Collection<Token> process(IsTokenizable doc) {
        List<Token> tokens = (List<Token>) doc.getTokens();
        List<Token> res = new ArrayList<>(tokens);

        for (int i = 0; i < tokens.size() - 1; i++){
            if (tokens.get(i).equals(AND_TOKEN)){
                if (tokens.get(i + 1).equals(NOT_TOKEN)){
                    res.set(i, new Token("&&!"));
                    i++;
                } else{
                    res.set(i, new Token("&&"));

                }
            } else if (tokens.get(i).equals(OR_TOKEN)){
                res.set(i, new Token("||"));
            }
        }

        return res;
    }

    @Override
    public Collection<Token> process(Collection<Token> tokens) {
        List<Token> tokens1 = (List<Token>) tokens;
        List<Token> res = new ArrayList<>(tokens);

        for (int i = 0; i < tokens.size() - 1; i++){
            if (tokens1.get(i).equals(AND_TOKEN)){
                if (tokens1.get(i + 1).equals(NOT_TOKEN)){
                    res.set(i, new Token("&&!"));
                    i++;
                } else{
                    res.set(i, new Token("&&"));

                }
            } else if (tokens1.get(i).equals(OR_TOKEN)){
                res.set(i, new Token("||"));
            }
        }

        return res;
    }

    @Override
    protected void processNoCopy(List<Token> tokens) {
        for (int i = 0; i < tokens.size() - 1; i++){
            if (tokens.get(i).equals(AND_TOKEN)){
                if (tokens.get(i + 1).equals(NOT_TOKEN)){
                    tokens.set(i, new Token("&&!"));
                    tokens.remove(i + 1);
                    i++;
                } else{
                    tokens.set(i, new Token("&&"));

                }
            } else if (tokens.get(i).equals(OR_TOKEN)){
                tokens.set(i, new Token("||"));
            }
        }
    }
}

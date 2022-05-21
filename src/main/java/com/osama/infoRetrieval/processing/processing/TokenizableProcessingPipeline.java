package com.osama.infoRetrieval.processing.processing;

import com.osama.infoRetrieval.document.*;
import com.osama.infoRetrieval.processing.tokenization.Token;
import com.osama.infoRetrieval.processing.tokenization.Tokenizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TokenizableProcessingPipeline {
    protected final List<Processor> processors;
    protected Tokenizer tokenizer;

    public TokenizableProcessingPipeline(Tokenizer tokenizer){
        this.processors = new ArrayList<>();
        this.tokenizer = tokenizer;
    }

    public boolean addProcessor(Processor proc){
        for (Processor processor: this.processors){
            if (processor.getClass() == proc.getClass()){
                return false;
            }
        }

        this.processors.add(proc);
        return true;
    }

    public Processor removeProcessor(Class<? extends Processor> proc){
        for (int i = 0; i < this.processors.size(); i++){
            if (proc == this.processors.get(i).getClass()){
                return this.processors.remove(i);
            }
        }

        return null;
    }

    public Collection<Token> process(IsTokenizable doc){
        Collection<Token> newTokens = new ArrayList<>(doc.getTokens());
        for (Processor proc: this.processors){
            newTokens = proc.process(newTokens);
        }
        return newTokens;
    }

    public Collection<Token> process(List<Token> tokens){
        Collection<Token> newTokens = new ArrayList<>(tokens);
        for (Processor proc: this.processors){
            newTokens = proc.process(newTokens);
        }
        return newTokens;
    }

    public Document processAndTokenizeDocument(RawDocument doc){
         List<Token> docTokens = this.tokenizer.tokenize(doc);
         Collection<Token> tok = process(docTokens);
         long docId = DocumentManager.newDocument((List<Token>) tok);
         return DocumentManager.getDocument(docId);
    }

    public Query processAndTokenizeQuery(RawQuery query){
        List<Token> docTokens = this.tokenizer.tokenize(query);
        Collection<Token> tok = process(docTokens);
        return new Query((List<Token>) tok);
    }
}

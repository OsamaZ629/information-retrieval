package com.osama.infoRetrieval.processing.pipeline;

import com.osama.infoRetrieval.document.Document;
import com.osama.infoRetrieval.document.DocumentManager;
import com.osama.infoRetrieval.document.IsTokenizable;
import com.osama.infoRetrieval.document.RawDocument;
import com.osama.infoRetrieval.processing.Processor;
import com.osama.infoRetrieval.processing.tokenization.Token;
import com.osama.infoRetrieval.processing.tokenization.Tokenizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TokenizableProcessingPipeline {
    private final List<Processor> processors;
    private Tokenizer tokenizer;

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
        Collection<Token> newTokens = doc.getTokens();
        for (Processor proc: this.processors){
            Collection<Token> finalNewTokens = newTokens;
            newTokens = proc.process(new IsTokenizable() {
                @Override
                public Collection<Token> getTokens() {
                    return finalNewTokens;
                }

                @Override
                public void setTokens(Collection<Token> newTokens) {

                }
            });
        }
        return newTokens;
    }

    public long processAndTokenizeDocument(RawDocument doc){
         List<Token> docTokens = this.tokenizer.tokenize(doc);
         long docId = DocumentManager.newDocument(docTokens);
         Document wellMadeDoc = DocumentManager.getDocument(docId);
         Collection<Token> tok = process(wellMadeDoc);
         wellMadeDoc.setTokens(tok);
         return docId;
    }
}

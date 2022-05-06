package com.osama.infoRetrieval.processing.pipeline;

import com.osama.infoRetrieval.document.Document;
import com.osama.infoRetrieval.document.DocumentManager;
import com.osama.infoRetrieval.document.RawDocument;
import com.osama.infoRetrieval.processing.DocumentProcessor;
import com.osama.infoRetrieval.processing.tokenization.Token;
import com.osama.infoRetrieval.processing.tokenization.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class DocumentProcessingPipeline {
    private List<DocumentProcessor> processors;
    private Tokenizer tokenizer;

    public DocumentProcessingPipeline(Tokenizer tokenizer){
        this.processors = new ArrayList<>();
        this.tokenizer = tokenizer;
    }

    public boolean addProcessor(DocumentProcessor proc){
        for (DocumentProcessor processor: this.processors){
            if (processor.getClass() == proc.getClass()){
                return false;
            }
        }

        this.processors.add(proc);
        return true;
    }

    public DocumentProcessor removeProcessor(Class<? extends DocumentProcessor> proc){
        for (int i = 0; i < this.processors.size(); i++){
            if (proc == this.processors.get(i).getClass()){
                return this.processors.remove(i);
            }
        }

        return null;
    }

    public void processDocument(Document doc){
        for (DocumentProcessor proc: this.processors){
            doc = proc.process(doc);
        }
    }

    public long processAndTokenizeDocument(RawDocument doc){
         List<Token> docTokens = this.tokenizer.tokenize(doc);
         long docId = DocumentManager.newDocument(docTokens);
         Document wellMadeDoc = DocumentManager.getDocument(docId);

         for (DocumentProcessor proc: this.processors){
             wellMadeDoc = proc.process(wellMadeDoc);
        }

         return docId;
    }
}

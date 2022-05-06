package com.osama.infoRetrieval;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import com.osama.infoRetrieval.document.Document;
import com.osama.infoRetrieval.document.DocumentManager;
import com.osama.infoRetrieval.document.Query;
import com.osama.infoRetrieval.document.RawDocument;
import com.osama.infoRetrieval.matching.TDMMatcher;
import com.osama.infoRetrieval.preproccesing.dilenation.CISIDilenator;
import com.osama.infoRetrieval.processing.RemoveBigSpacesProcessor;
import com.osama.infoRetrieval.processing.RemovePunctuationProcessor;
import com.osama.infoRetrieval.processing.ToLowerCaseProcessor;
import com.osama.infoRetrieval.processing.indexing.TDMIndexer;
import com.osama.infoRetrieval.processing.pipeline.DocumentProcessingPipeline;
import com.osama.infoRetrieval.processing.tokenization.BigWhiteSpaceTokenizer;
import com.osama.infoRetrieval.processing.tokenization.Token;
import com.osama.infoRetrieval.processing.tokenization.Tokenizer;
import com.osama.infoRetrieval.processing.tokenization.WhiteSpaceTokenizer;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

// TODO: CREATE QUERY PIPELINE, and find out how to generalize queries and documents.
// they should use same processors and same tokenizers.

public class Main {
    public static void main(String[] args) {
        Path resourceDirectory = Paths.get("src","main", "resources", "CISI.ALL");;
        CISIDilenator dilenator = new CISIDilenator(resourceDirectory.toAbsolutePath().toString());

        TDMIndexer indexer = new TDMIndexer();
        Tokenizer tokenizer = new BigWhiteSpaceTokenizer();

        DocumentProcessingPipeline pl = new DocumentProcessingPipeline(tokenizer);
        pl.addProcessor(new ToLowerCaseProcessor());
        pl.addProcessor(new RemovePunctuationProcessor());
        pl.addProcessor(new RemoveBigSpacesProcessor());

        int counter = 0;
        while(dilenator.hasNext() && counter < 25){
            RawDocument rawDoc = dilenator.next();
            long docId = pl.processAndTokenizeDocument(rawDoc);
            Document doc = DocumentManager.getDocument(docId);
            indexer.index(doc);

            counter++;
        }

        List<Token> terms = new ArrayList<Token>();
        terms.add(new Token("present"));
        Query q = new Query(terms);

        TDMMatcher matcher = new TDMMatcher(indexer.getStorageDevice());
        List<Document> docsMatched = matcher.match(q);

        for (Document doc: docsMatched){
            System.out.println(doc.getId());
        }
    }

    public static void print1DArr(NDArray arr){
        arr = arr.toType(DataType.INT32, true);
        System.out.println("[");
        for (int i = 0; i < arr.size(); i++){
            System.out.println(arr.getInt(i) + ",");
        }
        System.out.println("\b\b\n]");
    }

    public static void print2DArr(NDArray arr){
        System.out.println("[");
        for (int i = 0; i < arr.size(0); i++){
            for (int j = 0; j < arr.size(1); j++){
                System.out.print(arr.getInt(i, j) + ", ");
            }
            System.out.println("\b");
        }
        System.out.println("\b\n]");
    }
}

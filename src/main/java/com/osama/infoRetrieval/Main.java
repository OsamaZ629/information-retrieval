package com.osama.infoRetrieval;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.types.DataType;
import com.osama.infoRetrieval.document.*;
import com.osama.infoRetrieval.matching.boolean_matchers.TDMBooleanMatcher;
import com.osama.infoRetrieval.matching.word_matchers.TDMWordMatcher;
import com.osama.infoRetrieval.preproccesing.dilenation.CISIDilenator;
import com.osama.infoRetrieval.processing.processing.BooleanQueryProcessor;
import com.osama.infoRetrieval.processing.processing.RemoveBigSpacesProcessor;
import com.osama.infoRetrieval.processing.processing.RemovePunctuationProcessor;
import com.osama.infoRetrieval.processing.processing.ToLowerCaseProcessor;
import com.osama.infoRetrieval.processing.indexing.TDMIndexer;
import com.osama.infoRetrieval.processing.processing.TokenizableProcessingPipeline;
import com.osama.infoRetrieval.processing.tokenization.BigWhiteSpaceTokenizer;
import com.osama.infoRetrieval.processing.tokenization.BooleanTokenizer;
import com.osama.infoRetrieval.processing.tokenization.Token;
import com.osama.infoRetrieval.processing.tokenization.Tokenizer;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        Path resourceDirectory = Paths.get("src","main", "resources", "CISI.ALL");;
        CISIDilenator dilenator = new CISIDilenator(resourceDirectory.toAbsolutePath().toString());

        TDMIndexer indexer = new TDMIndexer();
        Tokenizer tokenizer = new BigWhiteSpaceTokenizer();
        Tokenizer queryTokenizer = new BooleanTokenizer();

        TokenizableProcessingPipeline docPL = new TokenizableProcessingPipeline(tokenizer);
        docPL.addProcessor(new ToLowerCaseProcessor());
        docPL.addProcessor(new RemovePunctuationProcessor());
        docPL.addProcessor(new RemoveBigSpacesProcessor());

        TokenizableProcessingPipeline queryPL = new TokenizableProcessingPipeline(queryTokenizer);
        queryPL.addProcessor(new RemoveBigSpacesProcessor());
        queryPL.addProcessor(new ToLowerCaseProcessor());
        queryPL.addProcessor(new RemovePunctuationProcessor());
        queryPL.addProcessor(new BooleanQueryProcessor());

        int counter = 0;
        while(dilenator.hasNext() && counter < 25){
            RawDocument rawDoc = dilenator.next();
            Document doc = docPL.processAndTokenizeDocument(rawDoc);
            indexer.index(doc);

            counter++;
        }

        RawQuery rq = new RawQuery("present and history and not selective");
        Query q = queryPL.processAndTokenizeQuery(rq);

        TDMBooleanMatcher matcher = new TDMBooleanMatcher(indexer.getStorageDevice());
        List<Document> docsMatched = matcher.match(q);

        for (Document doc: docsMatched){
            System.out.println(doc.getId());
        }
    }

//    public static void print1DArr(NDArray arr){
//        arr = arr.toType(DataType.INT32, true);
//        System.out.println("[");
//        for (int i = 0; i < arr.size(); i++){
//            System.out.println(arr.getInt(i) + ",");
//        }
//        System.out.println("\b\b\n]");
//    }
//
//    public static void print2DArr(NDArray arr){
//        System.out.println("[");
//        for (int i = 0; i < arr.size(0); i++){
//            for (int j = 0; j < arr.size(1); j++){
//                System.out.print(arr.getInt(i, j) + ", ");
//            }
//            System.out.println("\b");
//        }
//        System.out.println("\b\n]");
//    }
}

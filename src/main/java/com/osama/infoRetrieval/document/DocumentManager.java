package com.osama.infoRetrieval.document;

import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DocumentManager {
    private static List<Document> docs;
    private static long currentId = 0;

    static {
        docs = new ArrayList<>();
    }

    public static long newDocument(List<Token> terms){
        docs.add(new Document(currentId, terms));

        return currentId++;
    }

    public static long newDocument(List<Token> terms, Map<String, String> info){
        docs.add(new Document(currentId, terms, info));
        return currentId++;
    }

    public static Document getDocument(long id){
        return docs.get((int) id);
    }
}

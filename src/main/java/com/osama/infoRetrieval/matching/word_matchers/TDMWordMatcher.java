package com.osama.infoRetrieval.matching.word_matchers;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import com.osama.infoRetrieval.document.Document;
import com.osama.infoRetrieval.document.DocumentManager;
import com.osama.infoRetrieval.document.Query;
import com.osama.infoRetrieval.matching.Matcher;
import com.osama.infoRetrieval.processing.storageDevice.TDMStorage;
import com.osama.infoRetrieval.processing.tokenization.Token;

import java.security.KeyException;
import java.util.ArrayList;
import java.util.List;

public class TDMWordMatcher extends Matcher<TDMStorage> {
    private NDManager manager = NDManager.newBaseManager();

    public TDMWordMatcher(TDMStorage storage) {
        super(storage);
    }

    @Override
    public List<Document> match(Query q) {
        List<Document> docs = new ArrayList<Document>();
        List<Token> tokens = (List<Token>) q.getTokens();

        NDArray nd = null;
        for(Token term: tokens){
            NDArray termVector = storage.getTermVector(term.getContent());
            if (termVector == null){
                continue;
            } else if (nd == null){
                nd = termVector;
            }
            nd = NDArrays.logicalAnd(nd, termVector);
        }

        if (nd == null){
            return docs;
        }

        for (int i = 0; i < nd.size(); i++){
            if (nd.toType(DataType.BOOLEAN, false).getBoolean(i)){
                docs.add(DocumentManager.getDocument(i));
            }
        }
        return docs;
    }
}

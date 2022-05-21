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

import java.util.ArrayList;
import java.util.List;

public class TDMWordMatcher extends Matcher<TDMStorage> {
    private NDManager manager = NDManager.newBaseManager();

    public TDMWordMatcher(TDMStorage storage) {
        super(storage);
    }

    @Override
    public List<Document> match(Query q) {
        NDArray queryVec = createQueryVector(q);
        List<Document> docs = new ArrayList<Document>((int) queryVec.size());

        for (int i = 0; i < this.storage.getNumberOfDocument(); i++) {
            NDArray tmp = NDArrays.logicalAnd(queryVec.toType(DataType.INT32, false), (this.storage.getDocumentVector(i)));
            if (tmp.sum().toType(DataType.INT32, false).getInt() == queryVec.sum().toType(DataType.INT32, true).getInt()){
                docs.add(DocumentManager.getDocument(i));
            }
        }

        return docs;
    }

    private NDArray createQueryVector(Query q){
        long vectorSize = this.storage.getNumberOfTerms();
        NDArray nd = manager.zeros(new Shape(vectorSize), DataType.INT32);

        for (Token term: q.getTokens()){
            nd.setScalar(new NDIndex(this.storage.getTermIndex(term)), 1);
        }

        return nd;
    }
}

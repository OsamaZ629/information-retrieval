package com.osama.infoRetrieval.processing.indexing;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import com.osama.infoRetrieval.document.Document;
import com.osama.infoRetrieval.processing.storageDevice.TDMStorage;
import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.*;

public class TDMIndexer extends Indexer<TDMStorageDevice> {
    private final TDMStorageDevice storage;

    public TDMIndexer(){
        storage = new TDMStorageDevice();
    }

    public TDMIndexer(TDMStorageDevice storage){
        this.storage = storage;
    }

    @Override
    public void index(Document doc) {
        long docId = doc.getId();
        List<Token> docTerms = doc.getTerms();

        this.storage.addDoc(docId, docTerms);
    }

    @Override
    public TDMStorageDevice getStorageDevice() {
        return storage;
    }
}

class TDMStorageDevice implements TDMStorage{
    /*
           doc1 doc2
      term1
      term2
      term3

    */

    private final NDManager manager = NDManager.newBaseManager();
    private NDArray tdm;
    private final Map<Long, Long> docIds;
    private final Map<Token, Long> terms;
    public static final DataType DATA_TYPE = DataType.INT32;

    public TDMStorageDevice(){
        this.docIds = new HashMap<>();
        this.tdm = manager.zeros(new Shape(0, 0));
        this.terms = new HashMap<>();
    }


    @Override
    public NDArray getTermVector(String term) {
        Long termIndex = this.terms.get(new Token(term));
        if (termIndex == null)
            return null;
        return this.tdm.get(new NDIndex(termIndex + ", :")).toType(DATA_TYPE, false);
    }

    @Override
    public NDArray getDocumentVector(long docId) {
        Long docIndex = this.docIds.get(docId);
        if (docIndex == null)
            return null;
        return this.tdm.get(new NDIndex(":, " + docIndex)).toType(DATA_TYPE, false);
    }

    @Override
    public NDArray getTermVector(int idx) {
        return this.tdm.get(idx + ", :").toType(DATA_TYPE, false);
    }

    @Override
    public NDArray getDocumentVector(int idx) {
        return this.tdm.get(":, " + idx).toType(DATA_TYPE, false);
    }

    @Override
    public long getNumberOfTerms() {
        return this.terms.size();
    }

    @Override
    public long getNumberOfDocument() {
        return this.docIds.size();
    }

    @Override
    public long getTermIndex(Token term) {
        return this.terms.get(term);
    }

    @Override
    public long getDocumentIndex(long id) {
        return this.docIds.get(id);
    }

    protected void addDoc(long docId, List<Token> terms){
        if (docExists(docId)){
            return;
        }

        this.docIds.put(docId, (long) this.docIds.size());
        NDArray docColumn = manager.zeros(new Shape(this.terms.size()), DATA_TYPE);
        List<Token> notFoundTerms = new ArrayList<>();
        for (int i = 0; i < terms.size(); i++){
            Long termIdx = this.terms.get(terms.get(i));
            if (termIdx == null){
                notFoundTerms.add(terms.get(i));
            }else {
                docColumn.setScalar(new NDIndex(this.terms.get(terms.get(i))), 1);
            }
        }

        if (this.tdm.isEmpty() && !notFoundTerms.isEmpty()){
            this.tdm = manager.create(new int[][] {{1}});
            this.terms.put(notFoundTerms.get(notFoundTerms.size() - 1), 0L);
            notFoundTerms.remove(notFoundTerms.size() - 1);
        }else{
            this.tdm = this.tdm.concat(docColumn.reshape(docColumn.size(), 1), 1);
        }

        for (Token term : notFoundTerms) {
            // duplicate terms
            if (this.terms.get(term) != null)
                continue;
            addTerm(term);
            tdm.setScalar(new NDIndex(-1 + ", " + docIds.get(docId)), 1);
        }

    }

    private boolean termExists(Token term){
        return this.terms.containsKey(term);
    }

    private boolean docExists(long docId){
        return docIds.containsKey(docId);
    }

    private void addTerm(Token term){
        if (termExists(term)) return;
        this.terms.put(term, (long) this.terms.size());
        NDArray termArr = manager.zeros(new Shape(this.docIds.size()), DATA_TYPE);
        this.tdm = this.tdm.concat(termArr.reshape(1, termArr.size(0)));
    }
}

package com.osama.infoRetrieval.processing.storageDevice;

import ai.djl.ndarray.NDArray;
import com.osama.infoRetrieval.processing.tokenization.Token;

import java.security.KeyException;
import java.util.Collection;

public interface TDMStorage extends StorageDevice {
    NDArray getTermVector(String term);
    NDArray getDocumentVector(long docId);
    NDArray getTermVector(int idx);
    NDArray getDocumentVector(int idx);
    long getNumberOfTerms();
    long getNumberOfDocument();
    long getTermIndex(Token term) throws KeyException;
    long getDocumentIndex(long id) throws KeyException;
}

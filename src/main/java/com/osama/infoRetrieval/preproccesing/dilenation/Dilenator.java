package com.osama.infoRetrieval.preproccesing.dilenation;

import com.osama.infoRetrieval.document.RawDocument;

public interface Dilenator {
    RawDocument next();
    boolean hasNext();
}

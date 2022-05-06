package com.osama.infoRetrieval.matching;

import com.osama.infoRetrieval.document.Document;
import com.osama.infoRetrieval.document.Query;
import com.osama.infoRetrieval.processing.storageDevice.StorageDevice;

import java.util.List;

public abstract class Matcher<T extends StorageDevice> {
    protected T storage;

    public Matcher(T storage){
        this.storage = storage;
    }

    public abstract List<Document> match(Query q);
}

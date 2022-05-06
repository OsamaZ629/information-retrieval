package com.osama.infoRetrieval.processing.indexing;


import com.osama.infoRetrieval.document.Document;
import com.osama.infoRetrieval.processing.storageDevice.StorageDevice;


public abstract class Indexer<T extends StorageDevice>{

    public abstract void index(Document doc);
    public abstract T getStorageDevice();

}

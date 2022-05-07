package com.osama.infoRetrieval.document;


public class RawQuery implements HasContent{
    private final String content;

    protected RawQuery(String content) {
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }
}

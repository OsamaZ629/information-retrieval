package com.osama.infoRetrieval.document;

import java.util.Map;

public class RawDocument {
    private final Map<String, String> info;
    private String mainContent;

    public RawDocument(String content, Map<String, String> info) {
        this.mainContent = content;
        this.info = info;
    }

    public RawDocument(String content) {
        this(content, null);
    }


    public Map<String, String> getInfo() {
        return info;
    }

    public String getMainContent() {
        return mainContent;
    }
}

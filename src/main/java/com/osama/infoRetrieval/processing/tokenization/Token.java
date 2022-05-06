package com.osama.infoRetrieval.processing.tokenization;

import java.util.Objects;

public class Token {
    private String content;

    public Token(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return token.getContent().equals(this.content);
    }

    @Override
    public int hashCode() {
        return this.content.hashCode();
    }

    @Override
    public String toString(){
        return this.content;
    }
}

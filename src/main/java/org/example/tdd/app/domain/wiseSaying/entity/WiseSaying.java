package org.example.tdd.app.domain.wiseSaying.entity;

public class WiseSaying {
    private long id;
    private String content;
    private String author;

    public WiseSaying(long id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public WiseSaying(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public WiseSaying update(String changedContent, String changedAuthor) {
        this.content = changedContent;
        this.author = changedAuthor;
        return this;
    }
}

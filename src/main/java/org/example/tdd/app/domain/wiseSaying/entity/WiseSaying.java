package org.example.tdd.app.domain.wiseSaying.entity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

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

    public static WiseSaying fromMap(Map<String, Object> readAsMap) {
        return new WiseSaying(
                Long.parseLong((String) readAsMap.get("id")),
                (String) readAsMap.get("content"),
                (String) readAsMap.get("author"));
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

    public LinkedHashMap<String, Object> toMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("content", content);
        map.put("author", author);
        return map;
    }
}

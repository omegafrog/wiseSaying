package org.example;

import java.util.List;

public class WiseSayingService {
    private final WiseSayingRepository repository;

    public WiseSayingService(WiseSayingRepository repository) {
        this.repository = repository;
    }

    public WiseSaying create(String content, String author) {
        WiseSaying lifeQuote = new WiseSaying(content, author);
        return repository.insert(lifeQuote);
    }

    public List<WiseSaying> findAll() {
        return repository.findAll();
    }

    public void remove(int id) {
        repository.remove(id);
    }

    public WiseSaying find(int id) {
        return repository.find(id);
    }

    public void update(int id, WiseSaying wiseSaying) {
        repository.update(id, wiseSaying);
    }

    public void build() {
        repository.build();
    }

    public List<WiseSaying> findBy(String type, String word) {
        return repository.findBy(type, word);
    }
}

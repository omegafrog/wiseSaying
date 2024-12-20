package org.example.tdd.service;

import org.example.tdd.entity.WiseSaying;
import org.example.tdd.repository.WiseSayingRepositoryImpl;

import java.util.List;


public class WiseSayingService {
    private static final WiseSayingService INSTANCE = new WiseSayingService();
    private WiseSayingService() {}

    public static WiseSayingService getInstance(){
        return INSTANCE;
    }

    private final WiseSayingRepository repository = WiseSayingRepositoryImpl.getInstance();

    public WiseSaying create(String content, String author) {
        return repository.insert(new WiseSaying(content, author));
    }

    public List<WiseSaying> findAll() {
        return repository.findAll();
    }

    public void delete(long id){
        repository.deleteById(id);
    }
    public WiseSaying findById(long id){
        return repository.findById(id);
    }

    public void update(long id, WiseSaying wiseSaying) {
        repository.update(id, wiseSaying);
    }

    public void build() {
        repository.build();
    }
}

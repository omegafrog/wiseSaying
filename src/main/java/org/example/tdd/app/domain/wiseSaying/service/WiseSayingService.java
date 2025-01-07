package org.example.tdd.app.domain.wiseSaying.service;

import org.example.tdd.app.domain.wiseSaying.entity.WiseSaying;
import org.example.tdd.app.domain.wiseSaying.repository.WiseSayingRepositoryImpl;

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
    public WiseSaying getItem(long id){
        return repository.findById(id).get();
    }

    public void update(long id, WiseSaying wiseSaying) {
        repository.update(id, wiseSaying);
    }

    public void build() {
        repository.build();
    }

    public List<WiseSaying> search(String keywordType, String keyword, int pageNum) {
        return repository.findByPage(keywordType,keyword,pageNum);
    }
}

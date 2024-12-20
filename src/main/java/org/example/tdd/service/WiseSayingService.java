package org.example.tdd.service;

import org.example.tdd.entity.WiseSaying;
import org.example.tdd.repository.WiseSayingRepositoryImpl;



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
}

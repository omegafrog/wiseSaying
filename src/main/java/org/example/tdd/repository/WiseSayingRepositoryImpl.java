package org.example.tdd.repository;

import org.example.tdd.JsonUtil;
import org.example.tdd.entity.WiseSaying;
import org.example.tdd.service.WiseSayingRepository;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.example.tdd.App.BASE_PATH;

public class WiseSayingRepositoryImpl implements WiseSayingRepository {
    private static final WiseSayingRepositoryImpl INSTANCE = new WiseSayingRepositoryImpl();
    public static WiseSayingRepository getInstance() {
        return (WiseSayingRepository) INSTANCE;
    }

    private WiseSayingRepositoryImpl() {

    }

    @Override
    public WiseSaying insert(WiseSaying wiseSaying) {
        long id = JsonUtil.getLastId();

        wiseSaying.setId(++id);

        JsonUtil.setLastId(id);
        JsonUtil.serialize(BASE_PATH,wiseSaying, id);
        return wiseSaying;
    }

    @Override
    public WiseSaying findById(long id) {
        return null;
    }

    @Override
    public List<WiseSaying> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public WiseSaying update(long id, WiseSaying wiseSaying) {
        return null;
    }
}

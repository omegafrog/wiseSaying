package org.example.tdd.app.domain.wiseSaying.service;

import org.example.tdd.app.domain.wiseSaying.entity.WiseSaying;

import java.util.List;

public interface WiseSayingRepository {


    WiseSaying insert(WiseSaying wiseSaying);

    WiseSaying findById(long id);

    List<WiseSaying> findAll();

    void deleteById(long id);

    WiseSaying update(long id, WiseSaying wiseSaying);

    boolean existsById(long id);

    void build();
}

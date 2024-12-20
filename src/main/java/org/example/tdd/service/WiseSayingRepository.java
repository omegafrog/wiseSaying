package org.example.tdd.service;

import org.example.tdd.entity.WiseSaying;

import java.util.List;

public interface WiseSayingRepository {
    WiseSaying insert(WiseSaying wiseSaying);

    WiseSaying findById(long id);

    List<WiseSaying> findAll();

    void deleteById(long id);

    WiseSaying update(long id, WiseSaying wiseSaying);

}

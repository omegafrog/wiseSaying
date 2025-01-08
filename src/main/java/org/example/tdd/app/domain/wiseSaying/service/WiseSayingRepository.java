package org.example.tdd.app.domain.wiseSaying.service;

import org.example.nonTdd.Page;
import org.example.tdd.app.domain.wiseSaying.entity.WiseSaying;

import java.util.List;
import java.util.Optional;

public interface WiseSayingRepository {

    WiseSaying insert(WiseSaying wiseSaying);

    Optional<WiseSaying> findById(long id);

    List<WiseSaying> findAll();

    void deleteById(long id) ;

    WiseSaying update(long id, WiseSaying wiseSaying);

    boolean existsById(long id);

    void build();

    List<WiseSaying> findByPage(String keywordType, String keyword, int pageNum);

    Page<WiseSaying> findAllByPage(int pageNum);
}

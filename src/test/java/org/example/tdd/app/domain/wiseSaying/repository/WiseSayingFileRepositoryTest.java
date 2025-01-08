package org.example.tdd.app.domain.wiseSaying.repository;

import org.assertj.core.api.Assertions;
import org.example.tdd.app.common.Util;
import org.example.tdd.app.domain.wiseSaying.entity.WiseSaying;
import org.example.tdd.app.global.AppConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;


class WiseSayingFileRepositoryTest {

    private static WiseSayingFileRepository wiseSayingRepository;

    @BeforeAll
    static void setMode(){
        AppConfig.setMode(AppConfig.Mode.TEST);
        wiseSayingRepository = new WiseSayingFileRepository();
    }
    @BeforeEach
    void setUp() throws IOException {
        if(Files.exists(Path.of(AppConfig.getDbPath()+"/wiseSaying")))
            Util.File.deleteDir(AppConfig.getDbPath()+"/wiseSaying");
        Files.createDirectories(Path.of(AppConfig.getDbPath()+"/wiseSaying"));

    }

    @Test
    @DisplayName("wisesaying을 저장")
    void t1() {
        WiseSaying wiseSaying = new WiseSaying("content1", "author1");
        WiseSaying saved = wiseSayingRepository.insert(wiseSaying);

        Assertions.assertThat(saved.getId()).isEqualTo(1L);
        Assertions.assertThat(saved).isEqualTo(wiseSaying);
    }

    @Test
    @DisplayName("wisesaying을 조회")
    void t2() {
        WiseSaying wiseSaying = new WiseSaying("content1", "author1");
        WiseSaying saved = wiseSayingRepository.insert(wiseSaying);

        WiseSaying found = wiseSayingRepository.findById(saved.getId()).get();

        Assertions.assertThat(found).isEqualTo(saved);
    }

    @Test
    @DisplayName("wisesaying 삭제")
    void t3() throws NoSuchFileException {
        WiseSaying wiseSaying = new WiseSaying("content1", "author1");
        WiseSaying saved = wiseSayingRepository.insert(wiseSaying);

        wiseSayingRepository.deleteById(saved.getId());

        Optional<WiseSaying> found = wiseSayingRepository.findById(saved.getId());

        Assertions.assertThat(found).isEmpty();

    }

    @Test
    @DisplayName("모든 wisesaying 조회")
    void t4() {
        WiseSaying wiseSaying1 = new WiseSaying("content1", "author1");
        WiseSaying saved1 = wiseSayingRepository.insert(wiseSaying1);
        WiseSaying wiseSaying2 = new WiseSaying("content2", "author2");
        WiseSaying saved2 = wiseSayingRepository.insert(wiseSaying2);
        WiseSaying wiseSaying3 = new WiseSaying("content3", "author3");
        WiseSaying saved3 = wiseSayingRepository.insert(wiseSaying3);
        WiseSaying wiseSaying4 = new WiseSaying("content4", "author4");
        WiseSaying saved4 = wiseSayingRepository.insert(wiseSaying4);

        List<WiseSaying> savedList = List.of(saved1, saved2, saved3, saved4);

        List<WiseSaying> all = wiseSayingRepository.findAll();
        Assertions.assertThat(all).isEqualTo(savedList);
    }
    @Test
    @DisplayName("lastId 가져오기")
    void t5() {
        WiseSaying wiseSaying1 = new WiseSaying("content1", "author1");
        WiseSaying saved1 = wiseSayingRepository.insert(wiseSaying1);
        Long lastId = wiseSayingRepository.getLastId();
        Assertions.assertThat(lastId).isEqualTo(saved1.getId());
    }
    @Test
    @DisplayName("빌드")
    void t6() {
        WiseSaying wiseSaying1 = new WiseSaying("content1", "author1");
        WiseSaying wiseSaying2 = new WiseSaying("content2", "author2");
        wiseSayingRepository.insert(wiseSaying1);
        wiseSayingRepository.insert(wiseSaying2);
        wiseSayingRepository.build();

        String s = Util.File.readAsString(wiseSayingRepository.getBuildPath());

        Assertions.assertThat(s)
                .isEqualTo("""
                        [
                        \t{
                        \t\t"id" : "1",
                        \t\t"content" : "content1",
                        \t\t"author" : "author1"
                        \t},
                        \t{
                        \t\t"id" : "2",
                        \t\t"content" : "content2",
                        \t\t"author" : "author2"
                        \t}
                        ]""");
    }

    @Test
    @DisplayName("비어있을 때 빌드")
    void t7() {
        wiseSayingRepository.build();

        String s = Util.File.readAsString(wiseSayingRepository.getBuildPath());

        Assertions.assertThat(s)
                .isEqualTo("""
                        [
                        ]""");
    }
}
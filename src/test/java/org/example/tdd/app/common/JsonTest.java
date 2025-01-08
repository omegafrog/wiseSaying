package org.example.tdd.app.common;

import org.assertj.core.api.Assertions;
import org.example.tdd.app.domain.wiseSaying.entity.WiseSaying;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class JsonTest {

    @BeforeEach
    void setUp() {
        if (Files.exists(Path.of("test")))
            Util.File.deleteDir("test");
    }

    @AfterEach
    void tearDown() {
        if (Files.exists(Path.of("test")))
            Util.File.deleteDir("test");
    }

    @Test
    @DisplayName("속성이 2개, 숫자 포함")
    void t6() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("key1", "value1");
        map.put("key2", 3);
        String s = Util.Json.mapToJson(map);
        Assertions.assertThat(s).isEqualTo("""
                {
                \t"key1" : "value1",
                \t"key2" : "3"
                }""");
    }

    @Test
    @DisplayName("wisesaying을 map으로 변환 -> json file으로 변환")
    void t7() {
        WiseSaying wiseSaying = new WiseSaying(1L, "content1", "author1");
        Map<String, Object> map = wiseSaying.toMap();

        Util.File.createDir("test");
        String fileName = "test/%d.json".formatted(wiseSaying.getId());
        Util.Json.writeAsMap(fileName, map);

        Assertions.assertThat(Files.exists(Path.of(fileName))).isTrue();

        String s = Util.File.readAsString(fileName);
        Assertions.assertThat(s)
                .isEqualTo("""
                        {
                        \t"id" : "1",
                        \t"content" : "content1",
                        \t"author" : "author1"
                        }""");
    }

    @Test
    @DisplayName("json을 map으로 변환")
    void t8() {
        String json = """
                {
                \t"id" : "1",
                \t"content" : {
                \t\t"mycontent1" : "hi",
                \t\t"mycontent2" : "hello",
                \t},
                \t"author" : "author1"
                }""";
        Map<String, Object> stringObjectMap = Util.Json.readAsMap(json);
        System.out.println("stringObjectMap = " + stringObjectMap);

    }
    @Test
    @DisplayName("json file을 map으로 변환 -> wisesaying으로 변환")
    void t9() throws IOException {
        WiseSaying wiseSaying = new WiseSaying(1L, "content1", "author1");
        Map<String, Object> map = wiseSaying.toMap();

        Util.File.createDir("test");
        String fileName = "test/%d.json".formatted(wiseSaying.getId());
        Util.Json.writeAsMap(fileName, map);

        Map<String, Object> readAsMap = Util.Json.readAsMap(Util.File.readAsString(fileName));

        WiseSaying fromMap = WiseSaying.fromMap(readAsMap);

        Assertions.assertThat(fromMap.getId()).isEqualTo(wiseSaying.getId());
        Assertions.assertThat(fromMap.getContent()).isEqualTo("content1");
        Assertions.assertThat(fromMap.getAuthor()).isEqualTo("author1");
    }

    @Test
    @DisplayName("wisesaying list를 json file로 변환")
    void t10() {
        WiseSaying wiseSaying1 = new WiseSaying(1L, "content1", "author1");
        WiseSaying wiseSaying2 = new WiseSaying(2L, "content2", "author2");

        List<Map<String, Object>> wiseSayingList = Stream.of(wiseSaying1, wiseSaying2)
                .map(WiseSaying::toMap).toList();

        String s = Util.Json.listToJson(wiseSayingList);

        Assertions.assertThat(s).isEqualTo("""
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
}

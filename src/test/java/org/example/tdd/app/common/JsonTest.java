package org.example.tdd.app.common;

import org.assertj.core.api.Assertions;
import org.example.tdd.app.domain.wiseSaying.entity.WiseSaying;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonTest {

    @BeforeEach
    void setUp() {
        if(Files.exists(Path.of("test")))
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
        LinkedHashMap<String, Object> map = wiseSaying.toMap();

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
}

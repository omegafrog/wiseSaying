package org.example.tdd.app.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class JsonTest {

    @Test
    @DisplayName("속성이 2개, 숫자 포함")
    void t6() {
        Map<String, Object> map = Map.of("key1", "value1", "key2", 3);
        String s = Util.Json.mapToJson(map);
        Assertions.assertThat(s).isEqualTo("""
                {
                \t"key1" : "value1",
                \t"key2" : "3"
                }""");
    }
}

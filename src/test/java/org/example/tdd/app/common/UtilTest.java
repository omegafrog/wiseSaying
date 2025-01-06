package org.example.tdd.app.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    @BeforeEach
    void setUP() {
        if(Files.exists(Path.of("test.txt")))
            Util.File.deleteFile("test.txt");
    }

    @Test
    @DisplayName("파일 작성")
    void t1() {
        Util.File.writeAsString("test.txt", "test", StandardOpenOption.CREATE_NEW);

        Assertions.assertThat(Util.File.readAsString("test.txt")).isEqualTo("test");
    }

    @Test
    @DisplayName("파일 수정")
    void t2() {
        Util.File.writeAsString("test.txt", "test", StandardOpenOption.CREATE_NEW);
        Util.File.writeAsString("test.txt", "test2", StandardOpenOption.WRITE);
        Assertions.assertThat(Util.File.readAsString("test.txt")).isEqualTo("test2");
    }

    @Test
    @DisplayName("파일 조회")
    void t3() {
        Util.File.writeAsString("test.txt", "test", StandardOpenOption.CREATE_NEW);
        Assertions.assertThat(Util.File.readAsString("test.txt")).isEqualTo("test");

    }
    @Test
    @DisplayName("파일 삭제")
    void t4(){
        Util.File.writeAsString("test.txt", "test", StandardOpenOption.CREATE_NEW);
        Util.File.deleteFile("test.txt");

        Assertions.assertThat(Files.exists(Path.of("test.txt"))).isFalse();
    }
}
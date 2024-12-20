package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    private ByteArrayOutputStream outputStream;
    @BeforeEach
    void setUp() throws IOException {
        try(Stream<Path> list = Files.list(Path.of("db/WiseSaying"))){
            list.forEach(file -> file.toFile().delete());
            outputStream = TestUtil.setOutToByteArray();
        }
    }
    @AfterEach
    void tearDown() {
        TestUtil.clearSetOutToByteArray(outputStream);
    }

    @Test
    void addTest(){
        Scanner scanner = TestUtil.genScanner("""
                등록
                너 자신을 알라
                플라톤
                """);
        WiseSayingRepository repository = new WiseSayingRepository();
        WiseSayingService service = new WiseSayingService(repository);
        WiseSayingController controller = new WiseSayingController(scanner, service);
        App app = new App(controller, scanner);
        app.run();
        assertThat(outputStream.toString())
                .contains("명언 :")
                .contains("작가 :")
                .contains("1번 명언이 등록되었습니다.");
    }

    @Test
    void readAllTest(){
        String no1 = "1";
        String content1 = "\"너 자신을 알라\"";
        String author1 = "\"플라톤\"";
        Scanner scanner = TestUtil.genScanner("등록\n너 자신을 알라\n플라톤\n목록\n");

        WiseSayingRepository repository = new WiseSayingRepository();
        WiseSayingService service = new WiseSayingService(repository);
        WiseSayingController controller = new WiseSayingController(scanner, service);

        App app = new App(controller, scanner);
        app.run();

        assertThat(outputStream.toString())
                .contains("명령) 명언 : 작가 : 1번 명언이 등록되었습니다.")
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains(no1 + " / " + author1 + " / " + content1);
    }

    @Test
    void deleteTest(){
        Scanner scanner = TestUtil.genScanner("""
                등록
                너 자신을 알라
                플라톤
                삭제?id=1
                """);
        WiseSayingRepository repository = new WiseSayingRepository();
        WiseSayingService service = new WiseSayingService(repository);
        WiseSayingController controller = new WiseSayingController(scanner, service);

        App app = new App(controller, scanner);
        app.run();
        assertThat(outputStream.toString())
                .contains("1번 명언이 삭제되었습니다.");
    }

    @Test
    void updateTest(){
        Scanner scanner = TestUtil.genScanner("""
                등록
                너 자신을 알라
                플라톤
                수정?id=1
                너 자신을 몰라도 된다
                플라리스
                목록
                """);
        WiseSayingRepository repository = new WiseSayingRepository();
        WiseSayingService service = new WiseSayingService(repository);
        WiseSayingController controller = new WiseSayingController(scanner, service);

        App app = new App(controller, scanner);
        app.run();
        assertThat(outputStream.toString())
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains(1 + " / " + "\"플라리스\"" + " / " + "\"너 자신을 몰라도 된다\"");
    }

    @Test
    void buildTest() throws IOException {
        Scanner scanner = TestUtil.genScanner("""
                등록
                너 자신을 알라
                플라톤
                등록
                너 자신을 알라2
                플라톤2
                빌드
                """);
        WiseSayingRepository repository = new WiseSayingRepository();
        WiseSayingService service = new WiseSayingService(repository);
        WiseSayingController controller = new WiseSayingController(scanner, service);

        App app = new App(controller, scanner);
        app.run();
        assertThat(outputStream.toString())
                .contains("data.json 파일의 내용이 갱신되었습니다.");
        File file = new File("db/WiseSaying/build.json");
        assertThat(file.exists()).isTrue();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String s = reader.lines().collect(Collectors.joining("\n"));
        assertThat(s).contains("\"id\": 1")
                .contains("\"content\": \"너 자신을 알라\"")
                .contains("\"author\": \"플라톤\"")
                .contains("\"id\": 2")
                .contains("\"content\": \"너 자신을 알라2\"")
                .contains("\"author\": \"플라톤2\"");
    }
}
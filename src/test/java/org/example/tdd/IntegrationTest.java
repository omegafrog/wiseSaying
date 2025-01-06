package org.example.tdd;

import org.example.TestUtil;
import org.example.tdd.app.App;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class IntegrationTest {

    public static final String BASE_PATH = "db/WiseSaying";
    private ByteArrayOutputStream outputStream;


    @BeforeEach
    void setUp() throws IOException {
        try(Stream<Path> paths = Files.list(Path.of(BASE_PATH))){
            paths.forEach(path->path.toFile().delete());
        }
        outputStream = TestUtil.setOutToByteArray();
    }

    @AfterEach
    void tearDown() throws IOException {
        TestUtil.clearSetOutToByteArray(outputStream);
    }

    @Test
    void 종료_명령을_실행할_수_있어야_한다(){
        Scanner scanner = TestUtil.genScanner("종료");
        App app = new App(scanner);
        app.run();

        assertThat(app.isExisted()).isTrue();
    }
    @Test
    void 명언을_등록할_수_있다()  {
        prepare("""
                등록
                현재를 사랑하라
                작자미상
                종료
                """);

        List<File> files;
        try(Stream<Path> list = Files.list(Path.of(BASE_PATH))){
            files = list.map(Path::toFile).toList();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        assertThat(files).isNotEmpty();

        try(BufferedReader reader = new BufferedReader(new FileReader(files.get(0)))){
            Stream<String> lines = reader.lines();
            assertThat(lines)
                    .anyMatch(line -> line.contains("현재를 사랑하라"))
                    .anyMatch(line -> line.contains("작자미상"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 등록시_생성한_명언번호를_노출해야_한다(){
        prepare("""
                등록
                현재를 사랑하라
                작자미상
                종료
                """);

        assertThat(outputStream.toString())
                .contains("1번 명언이 등록되었습니다.");
    }

    @Test
    void 등록할_때마다_명언의_번호가_증가해야_한다(){
        prepare("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                현재를 사랑하라.
                작자미상
                종료
                """);
        assertThat(outputStream.toString())
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.");
    }

    @Test
    void 명언의_목록을_확인할_수_있어야_한다(){
        prepare("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록
                종료
                """);

        assertThat(outputStream.toString())
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .contains("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    void 명언을_삭제할_수_있어야_한다(){
        prepare("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록
                삭제?id=1
                종료
                """);
        assertThat(outputStream.toString())
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.")
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .contains("1 / 작자미상 / 현재를 사랑하라.")
                .contains("1번 명언이 삭제되었습니다.");
    }

    @Test
    void 존재하지_않는_명언을_삭제할_수_없다(){
        prepare("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록
                삭제?id=1
                삭제?id=1
                종료
                """);

        assertThat(outputStream.toString())
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.")
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .contains("1 / 작자미상 / 현재를 사랑하라.")
                .contains("1번 명언이 삭제되었습니다.")
                .contains("1번 명언이 존재하지 않습니다.");
    }

    @Test
    void 명언을_수정할_수_있어야_한다(){
        prepare("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                수정?id=2
                현재와 자신을 사랑하라.
                홍길동
                수정?id=3
                목록
                """);

        assertThat(outputStream.toString())
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.")
                .contains("3번 명언은 존재하지 않습니다.")
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("2 / 홍길동 / 현재와 자신을 사랑하라.")
                .contains("1 / 작자미상 / 현재를 사랑하라.");

        List<File> files;
        try(Stream<Path> list = Files.list(Path.of(BASE_PATH))){
            files = list.map(Path::toFile).toList();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        assertThat(files).isNotEmpty();

        Optional<File> updatedFile = files.stream().filter(file -> file.getName().equals("2.json")).findFirst();
        assertThat(updatedFile).isPresent();

        try(BufferedReader reader = new BufferedReader(new FileReader(updatedFile.get()))){
            assertThat(reader.lines())
                    .anyMatch(line -> line.contains("현재와 자신을 사랑하라."))
                    .anyMatch(line -> line.contains("홍길동"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void lastId_파일에_최신의_id가_저장되어야_한다(){
        prepare("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                현재를 사랑하라.
                작자미상
                """);

        File lastIdFile = new File(BASE_PATH+"/lastId.txt");
        assertThat(lastIdFile).exists();
        int id;
        try(BufferedReader reader = new BufferedReader(new FileReader(lastIdFile))){
            id = Integer.parseInt(reader.readLine());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThat(id).isEqualTo(2);
    }

    @Test
    void data_파일을_빌드할_수_있어야_한다(){
        prepare("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                빌드
                """);
        assertThat(outputStream.toString())
                .contains("data.json 파일의 내용이 갱신되었습니다.");

        File dataFile = new File(BASE_PATH+"/data.json");
        assertThat(dataFile).exists();
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            reader.lines().forEach(line -> builder.append(line).append("\n"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String dataFileContent = builder.toString();
        assertThat(dataFileContent).isNotEmpty();
        assertThat(dataFileContent)
                .contains("""
                        [
                        \t{
                        \t\t"id": 1,
                        \t\t"content": "현재를 사랑하라.",
                        \t\t"author": "작자미상"
                        \t},
                        \t{
                        \t\t"id": 2,
                        \t\t"content": "과거에 집착하지 마라.",
                        \t\t"author": "작자미상"
                        \t}
                        ]
                        """);
    }


    private static void prepare(String input){
        Scanner scanner = TestUtil.genScanner(input);
        App app = new App(scanner);
        app.run();
    }
}

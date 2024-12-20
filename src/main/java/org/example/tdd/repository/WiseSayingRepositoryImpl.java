package org.example.tdd.repository;

import org.example.tdd.JsonUtil;
import org.example.tdd.entity.WiseSaying;
import org.example.tdd.exception.EntityNotFoundException;
import org.example.tdd.service.WiseSayingRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.example.tdd.App.BASE_PATH;

public class WiseSayingRepositoryImpl implements WiseSayingRepository {
    private static final WiseSayingRepositoryImpl INSTANCE = new WiseSayingRepositoryImpl();
    public static WiseSayingRepository getInstance() {
        return INSTANCE;
    }

    private WiseSayingRepositoryImpl() {

    }

    @Override
    public WiseSaying insert(WiseSaying wiseSaying) {
        long id = JsonUtil.getLastId();

        wiseSaying.setId(++id);

        JsonUtil.setLastId(id);
        JsonUtil.serialize(BASE_PATH,wiseSaying, id);
        return wiseSaying;
    }

    @Override
    public WiseSaying findById(long id) {
        if(!existsById(id))
            throw new EntityNotFoundException(id+"번 명언은 존재하지 않습니다.");
        return JsonUtil.deserialize(BASE_PATH +"/"+ id + ".json");
    }

    @Override
    public List<WiseSaying> findAll() {
        try(Stream<Path> paths = Files.list(Paths.get(BASE_PATH))) {
            List<Path> filtered = paths.filter(path ->
                            !path.toString().contains("build.json") && !path.toString().contains("lastId.txt"))
                    .toList();
            return filtered.stream().map(path -> JsonUtil.deserialize(path.toString())).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(long id) {
        File file = new File(BASE_PATH + "/"+id + ".json");
        if(file.exists())
            file.delete();
        else{
            throw new EntityNotFoundException(id + "번 명언이 존재하지 않습니다.");
        }
    }

    @Override
    public WiseSaying update(long id, WiseSaying wiseSaying) {
        if(!existsById(id))
            throw new EntityNotFoundException(id + "번 명언이 존재하지 않습니다.");
        JsonUtil.serialize(BASE_PATH, wiseSaying, id);
        return wiseSaying;
    }

    @Override
    public boolean existsById(long id) {
        File file = new File(BASE_PATH +"/"+ id+  ".json");
        return file.exists();
    }

    @Override
    public void build() {
        File file = new File(BASE_PATH+"/" + "data.json");
        // 빌드 파일 객체 writer 생성
        try(FileWriter resultFileWriter = new FileWriter(file)){
            resultFileWriter.write("[\n");
            // 디렉토리 내부 파일명 모두 파싱
            try(Stream<Path> paths = Files.list(Paths.get(BASE_PATH))){
                // 파일명이 .json으로 끝나고 build.json이 아닌 파일들의 내용을 모두 빌드 파일 객체 writer에 씀
                StringBuilder builder = new StringBuilder();
                for(Path validPath : getEntityFilePathList(paths)){
                    try(BufferedReader entityReaderBuffer = new BufferedReader(new FileReader(validPath.toFile()))){
                        entityReaderBuffer.lines().forEach(line -> {
                            builder.append("\t").append(line);
                            builder.append("\n");
                        });
                        builder.deleteCharAt(builder.length()-1);
                    }
                    builder.append(",\n");
                }
                builder.delete(builder.length()-3, builder.length()-1);
                resultFileWriter.write(builder.toString());
            }
            resultFileWriter.write("]");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private List<Path> getEntityFilePathList(Stream<Path> paths) {
        List<Path> list = paths.toList();
        return list.stream()
                .filter(path -> path.toString().endsWith(".json") && !path.toString().endsWith("build.json"))
                .toList();
    }
}

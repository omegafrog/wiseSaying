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
        return (WiseSayingRepository) INSTANCE;
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
        return null;
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
        return null;
    }
}

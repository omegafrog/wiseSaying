package org.example.tdd.app.domain.wiseSaying.repository;

import org.example.nonTdd.Page;
import org.example.tdd.app.common.Util;
import org.example.tdd.app.common.exception.EntityNotFoundException;
import org.example.tdd.app.domain.wiseSaying.entity.WiseSaying;
import org.example.tdd.app.domain.wiseSaying.service.WiseSayingRepository;
import org.example.tdd.app.global.AppConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class WiseSayingFileRepository implements WiseSayingRepository {

    private final static String DB_PATH = AppConfig.getDbPath() + "/wiseSaying";
    public WiseSayingFileRepository() {
        init();
    }

    private void init() {
        try{
            if (!Files.exists(Path.of(DB_PATH))) {
                Files.createDirectories(Path.of(DB_PATH));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getFilePath(Long id) {
        return DB_PATH + "/%d.json".formatted(id);
    }

    @Override
    public WiseSaying insert(WiseSaying wiseSaying) {
        Long id = getLastId() + 1;
        String fileName = getFilePath(id);
        wiseSaying.setId(id);
        Util.File.write(fileName, Util.Json.mapToJson(wiseSaying.toMap()), StandardOpenOption.CREATE_NEW);
        setLastId(id);
        return wiseSaying;
    }

    @Override
    public Optional<WiseSaying> findById(long id) {
        String fileName = getFilePath(id);
        Map<String, Object> res = Util.Json.readAsMap(Util.File.readAsString(fileName));
        if (res.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(WiseSaying.fromMap(res));
    }

    @Override
    public List<WiseSaying> findAll() {
        List<WiseSaying> res = new ArrayList<>();
        List<String> fileNames = Util.File.getPaths(DB_PATH).stream().map(Path::toString).toList();
        for (String fileName : fileNames) {
            if (fileName.matches("\\S*\\d+.json"))
                res.add(WiseSaying.fromMap(Util.Json.readAsMap(Util.File.readAsString(fileName))));
        }
        return res;
    }

    @Override
    public void deleteById(long id) throws EntityNotFoundException {
        String fileName = getFilePath(id);
        if(!Util.File.isExist(fileName))
            throw new EntityNotFoundException(id+"번 명언이 존재하지 않습니다.");
        Util.File.deleteFile(fileName);
    }

    @Override
    public WiseSaying update(long id, WiseSaying wiseSaying) {
        return null;
    }

    @Override
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public void build() {
        List<WiseSaying> all = findAll();
        String s = Util.Json.listToJson(all.stream().map(WiseSaying::toMap).collect(Collectors.toList()));
        Util.File.write(getBuildPath(), s);
    }

    @Override
    public List<WiseSaying> findByPage(String keywordType, String keyword, int pageNum) {
        return List.of();
    }

    public Long getLastId() {
        String s = Util.File.readAsString(DB_PATH + "/lastId.txt");
        if (s.isEmpty()) {
            return 0L;
        }
        return Long.parseLong(s);
    }

    public void setLastId(Long id) {
        Util.File.write(DB_PATH + "/lastId.txt", Long.toString(id));
    }

    public String getBuildPath() {
        return DB_PATH + "/build.json";
    }

    @Override
    public Page<WiseSaying> findAllByPage(int pageNum) {
        List<WiseSaying> all = findAll();
        all.sort(new Comparator<WiseSaying>() {
            @Override
            public int compare(WiseSaying o1, WiseSaying o2) {
                return Long.compare(o1.getId(), o2.getId());
            }
        });

        return new Page<>(all, (int) count(), 5, pageNum);
    }
    public long count()  {
        try{
            return Files.walk(Path.of(DB_PATH), 1)
                    .filter(item->item.getFileName().toString().matches("\\d+.json")).count();
        }catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }
}

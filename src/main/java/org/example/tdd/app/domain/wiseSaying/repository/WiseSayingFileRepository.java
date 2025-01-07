package org.example.tdd.app.domain.wiseSaying.repository;

import org.example.tdd.app.common.Util;
import org.example.tdd.app.domain.wiseSaying.entity.WiseSaying;
import org.example.tdd.app.domain.wiseSaying.service.WiseSayingRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WiseSayingFileRepository implements WiseSayingRepository {
    private Long id = 0L;
    private static String BASE_PATH = "db/WiseSaying";
    private boolean testMode = false;

    public WiseSayingFileRepository(boolean testMode) {
        this.testMode = testMode;
        if(testMode) BASE_PATH = "db/test/WiseSaying";
    }

    @Override
    public WiseSaying insert(WiseSaying wiseSaying) {
        String fileName = getFileName(id);
        wiseSaying.setId(id++);
        Util.File.write(fileName, Util.Json.mapToJson(wiseSaying.toMap()));
        return wiseSaying;
    }

    @Override
    public Optional<WiseSaying> findById(long id) {
        String fileName = getFileName(id);
        Map<String, Object> res = Util.Json.readAsMap(Util.File.readAsString(fileName));
        if (res.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(WiseSaying.fromMap(res));
    }

    @Override
    public List<WiseSaying> findAll() {
        List<WiseSaying> res = new ArrayList<>();
        List<String> fileNames = Util.File.getPaths(BASE_PATH).stream().map(Path::toString).toList();
        for(String fileName : fileNames)
            res.add(WiseSaying.fromMap(Util.Json.readAsMap(Util.File.readAsString(fileName))));
        return res;
    }

    @Override
    public void deleteById(long id) {
        String fileName = getFileName(id);
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

    }

    @Override
    public List<WiseSaying> findByPage(String keywordType, String keyword, int pageNum) {
        return List.of();
    }
    private String getFileName(Long id){
        return BASE_PATH+"/%d.json".formatted(id);
    }
}

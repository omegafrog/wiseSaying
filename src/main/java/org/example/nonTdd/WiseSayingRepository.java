package org.example.nonTdd;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.example.nonTdd.JsonUtil.deserialize;
import static org.example.nonTdd.JsonUtil.serialize;

public class WiseSayingRepository {

    private static final String BASE_PATH = "db/wiseSaying/";

    public WiseSaying insert(WiseSaying element) {
        if(!element.validate())
            throw new IllegalArgumentException("특수문자가 포함되면 안됩니다.");
        int id = getLastId()+1;
        element.setId(id);

        serialize(BASE_PATH, element, id);
        return element;
    }

    public List<WiseSaying> findAll() {
        List<WiseSaying> quotes = new ArrayList<>();

        try(Stream<Path> paths = Files.list(Paths.get(BASE_PATH))){
            getEntityFilePathList(paths).forEach(path -> quotes.add(deserialize(path.toString())));
        }catch (IOException e){}
        return quotes;
    }

    @Deprecated
    public List<WiseSaying> findAll(int pageNum) {
        List<WiseSaying> quotes = new ArrayList<>();

        try(Stream<Path> paths = Files.list(Paths.get(BASE_PATH))){
            getEntityFilePathList(paths)
                    .forEach(path -> quotes.add(deserialize(path.toString())));
        }catch (IOException e){}
        quotes.sort(new Comparator<WiseSaying>() {
            @Override
            public int compare(WiseSaying o1, WiseSaying o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });
        int fromIdx = pageNum*5;
        int toIdx = (pageNum+1)*5<quotes.size()?pageNum+1:quotes.size()-1;
        return quotes.subList(fromIdx, toIdx);
    }

    private List<Path> getEntityFilePathList(Stream<Path> paths) {
        List<Path> list = paths.toList();
        return list.stream()
                .filter(path -> path.toString().endsWith(".json") && !path.toString().endsWith("build.json"))
                .toList();
    }

    private static int getLastId() {
        int id = 0;
        String idPath = BASE_PATH+"lastId.txt";
        try{
            File file = new File(idPath);
            if(!file.exists())
                createLastIdFile(file, id);
            else
                id = getLastId(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        return id;
    }

    public void remove(int id) {
        if(!exist(id))
            throw new IllegalArgumentException(id + "번 명언은 존재하지 않습니다.");
        try{
            Files.delete(Path.of(BASE_PATH + id + ".json"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean exist(int id){
        File file = new File(BASE_PATH + id + ".json");
        return file.exists();
    }

    public WiseSaying find(int id) {
        if(!exist(id))
            throw new IllegalArgumentException(id+"번 명언은 존재하지 않습니다.");

        return deserialize(BASE_PATH + id + ".json");
    }

    public void update(int id, WiseSaying wiseSaying) {
        if(!exist(id)) throw new IllegalArgumentException(id+"번 명언은 존재하지 않습니다.");
        serialize(BASE_PATH, wiseSaying, id);
    }

    public void build() {
        // 빌드 파일 객체 생성
        File file = new File(BASE_PATH + "build.json");
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
                builder.delete(builder.length()-2, builder.length()-1);
                resultFileWriter.write(builder.toString());
            }
            resultFileWriter.write("]");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<WiseSaying> findBy(String type, String word) {
        return findAll().stream().filter(
                ws -> {
                    if (type.equals("content")) {
                        return ws.getContent().contains(word);
                    } else if (type.equals("author")) {
                        return ws.getAuthor().contains(word);
                    }
                    return false;
                }
        ).toList();
    }

    private static int getLastId(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = br.readLine();
        int id = Integer.parseInt(s);
        br.close();
        return id;
    }
    private static void createLastIdFile(File file, int id) throws IOException {
        file.createNewFile();
        FileWriter bw = new FileWriter(file);
        bw.write(String.valueOf(id));
        bw.close();
    }






}



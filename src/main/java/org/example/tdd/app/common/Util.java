package org.example.tdd.app.common;

import org.example.tdd.app.domain.wiseSaying.entity.WiseSaying;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.example.tdd.app.App.BASE_PATH;

public class Util {
    public static class File {
        public static String readAsString(String path) {
            try {
                return Files.readString(Path.of(path));
            }catch (NoSuchFileException e){
                return "";
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static void write(String path, String content, OpenOption... options) {
            try {
                Files.writeString(Path.of(path), content, options);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static void deleteDir(String path) {
            try {
                Files.walkFileTree(Path.of(path), new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file); // 파일 삭제
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir); // 디렉토리 삭제
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static void createDir(String path) {
            try {
                Files.createDirectory(Path.of(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



        public static WiseSaying deserialize(String path) {
            try {
                java.io.File file = new java.io.File(path);
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    StringBuffer buffer = new StringBuffer();
                    br.lines().forEach(
                            line -> buffer.append(line).append("\n")
                    );
                    String s = buffer.toString();
                    br.close();
                    return parseJson(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static void serialize(String basePath, WiseSaying element, long id) {
            String filePath = basePath + "/" + id + ".json";
            try {
                java.io.File file = new java.io.File(filePath);
                file.createNewFile();

                FileWriter writer = new FileWriter(file);
                writer.write(toJson(element));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public static void deleteFile(String file) {
            try {
                Files.delete(Path.of(file));
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        public static List<Path> getPaths(String basePath) {
            try(Stream<Path> paths = Files.list(Path.of(basePath))){
                return paths.toList();
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static boolean isExist(String fileName) {
            return Files.exists(Path.of(fileName));
        }
    }

    public static class Json{
        public static String mapToJson(Map<String, Object> map){
            StringBuilder builder = new StringBuilder();
            builder.append("{\n");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                builder.append("\t").append("\"%s\" : \"%s\"".formatted(entry.getKey(), entry.getValue().toString()))
                        .append(",\n");
            }
            builder.delete(builder.length() - 2, builder.length()-1);
            builder.append("}");
            return builder.toString();
        }

        public static void writeAsMap(String fileName, Map<String, Object> map) {
            File.write(fileName, mapToJson(map));
        }

        public static Map<String, Object> readAsMap(String s) {
            if (s.isEmpty()) {
                return new HashMap<>();
            }
            Map<String, Object> res = new LinkedHashMap<>();
            s = s.substring(1, s.length() - 1).trim();

            int bracketCount = 0;
            int braceCount = 0;
            List<String> entry = new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            for (char c : s.toCharArray()) {
                if(c == '{')
                    braceCount++;
                if(c == '}')
                    braceCount--;
                if(c == '[')
                    bracketCount++;
                if(c == ']')
                    bracketCount--;
                if(braceCount == 0 && bracketCount == 0 && c==','){
                    entry.add(builder.toString());
                    builder.setLength(0);
                }else builder.append(c);
            }
            entry.add(builder.toString());
            Pattern DIGIT = Pattern.compile("\\d+");

            for (String keyValue:entry){
                keyValue = keyValue.replaceAll("\"","").trim();
                String key = keyValue.split(":")[0].trim();
                String value = keyValue.split(":")[1].trim();
                if(value.startsWith("{") && value.endsWith("}")){
                    res.put(key, readAsMap(value));
                } else if (value.startsWith("[") && value.endsWith("]")) {
                    res.put(key, readAsArray(value));
                }else if(value.startsWith("\"") && value.endsWith("\"")){
                    res.put(key, value.replaceAll("\"", ""));
                }else {
                    res.put(key, value);
                }
            }
            return res;
        }
        private static List<Object> readAsArray(String s) {
            List<Object> res = new ArrayList<>();
            String[] split = s.trim().split(",");
            Pattern DIGIT = Pattern.compile("\\d+");
            for(String item : split){
                if(item.startsWith("\"") && item.endsWith("\"")){ // 문자열
                    res.add(item.replaceAll("\"",""));
                }else if(DIGIT.matcher(item).matches()){
                    res.add(Integer.parseInt(item));
                }else{
                    res.add(item);
                }
            }
            return res;
        }

        public static String listToJson(List<Map<String, Object>> wiseSayingList) {
            StringBuilder builder = new StringBuilder();
            builder.append("[").append("\n");
            for (Map<String, Object> wiseSaying : wiseSayingList) {
                builder.append("\t");
                builder.append(mapToJson(wiseSaying).replaceAll("\n", "\n\t"));
                builder.append(",\n");
            }
            if(builder.length()>2){
                builder.delete(builder.length() - 2, builder.length()-1);
            }
            builder.append("]");
            return builder.toString();
        }
    }

    public static WiseSaying parseJson(String json) {
        String s = json.replaceAll("\t", "")
                .replaceAll("\n", "");
        String[] split = s.substring(1, s.length() - 1).split(",");
        long id = Long.parseLong(String.valueOf(split[0]).split(":")[1].strip());
        String content = String.valueOf(split[1]).split(":")[1].strip().replaceAll("\"", "");
        String author = String.valueOf(split[2]).split(":")[1].strip().replaceAll("\"", "");
        return new WiseSaying(id, content, author);
    }

    private static String toJson(WiseSaying e) {
        return "{\n" +
                "\t\"id\": " + e.getId() + ",\n" +
                "\t\"content\": " + "\"" + e.getContent() + "\"" + ",\n" +
                "\t\"author\": " + "\"" + e.getAuthor() + "\"\n" +
                "}";
    }


    public static long getLastId() {
        long id;
        java.io.File lastIdFile = new java.io.File(BASE_PATH + "/lastId.txt");
        if (!lastIdFile.exists()) {
            try {
                Files.createFile(lastIdFile.toPath());
                try (FileWriter writer = new FileWriter(BASE_PATH + "/lastId.txt")) {
                    writer.write(String.valueOf(0));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(BASE_PATH + "/lastId.txt"))) {
            id = Long.parseLong(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public static void setLastId(long id) {
        try (FileWriter writer = new FileWriter(BASE_PATH + "/lastId.txt")) {
            writer.write(String.valueOf(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

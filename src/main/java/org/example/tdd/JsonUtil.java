package org.example.tdd;

import org.example.tdd.app.domain.wiseSaying.entity.WiseSaying;

import java.io.*;
import java.nio.file.Files;

import static org.example.tdd.app.App.BASE_PATH;

public class JsonUtil {
    public static WiseSaying parseJson(String json){
        String s = json.replaceAll("\t", "")
                .replaceAll("\n", "");
        String[] split = s.substring(1, s.length() - 1).split(",");
        long id = Long.parseLong(String.valueOf(split[0]).split(":")[1].strip());
        String content = String.valueOf(split[1]).split(":")[1].strip().replaceAll("\"", "");
        String author = String.valueOf(split[2]).split(":")[1].strip().replaceAll("\"", "");
        return new WiseSaying(id, content, author);
    }
    public static WiseSaying deserialize(String path) {
        try{
            File file = new File(path);
            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                StringBuffer buffer = new StringBuffer();
                br.lines().forEach(
                        line -> buffer.append(line).append("\n")
                );
                String s = buffer.toString();
                br.close();
                return parseJson(s);
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    private static String toJson(WiseSaying e){
        return "{\n" +
                "\t\"id\": "+e.getId()+",\n"+
                "\t\"content\": "+"\""+e.getContent()+"\""+",\n"+
                "\t\"author\": "+"\""+e.getAuthor()+"\"\n"+
                "}";
    }
    public static void serialize(String basePath, WiseSaying element, long id) {
        String filePath = basePath + "/"+id + ".json";
        try{
            File file = new File(filePath);
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(toJson(element));
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static long getLastId(){
        long id;
        File lastIdFile = new File(BASE_PATH+"/lastId.txt");
        if(!lastIdFile.exists()){
            try {
                Files.createFile(lastIdFile.toPath());
                try( FileWriter writer = new FileWriter(BASE_PATH+"/lastId.txt"))
                {
                    writer.write(String.valueOf(0));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(BASE_PATH+"/lastId.txt"))){
            id = Long.parseLong(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return id;
    }
    public static void setLastId(long id){
        try(FileWriter writer = new FileWriter(BASE_PATH+"/lastId.txt")){
            writer.write(String.valueOf(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

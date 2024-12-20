package org.example;

import java.io.*;

public class JsonUtil {
    private static WiseSaying parseJson(String json){
        String s = json.replaceAll("\t", "")
                .replaceAll("\n", "");
        String[] split = s.substring(1, s.length() - 1).split(",");
        int id = Integer.parseInt(String.valueOf(split[0]).split(":")[1].strip());
        String content = String.valueOf(split[1]).split(":")[1].strip();
        String author = String.valueOf(split[2]).split(":")[1].strip();
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
    public static void serialize(String basePath, WiseSaying element, int id) {
        String filePath = basePath + id + ".json";
        try{
            File file = new File(filePath);
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(toJson(element));
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        String idPath = basePath+"lastId.txt";
        File file = new File(idPath);
        try(FileWriter fw = new FileWriter(file)){
            fw.write(String.valueOf(id));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

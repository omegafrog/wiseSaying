package org.example.tdd;

import org.example.tdd.controller.WiseSayingController;
import org.example.tdd.exception.UnsupportedCommandException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class App {
    private final Scanner scanner;
    private static boolean isExited = false;
    private final WiseSayingController controller;

    public static final String BASE_PATH = "db/WiseSaying";


    private enum COMMAND{
        EXIT("종료"),
        CREATE("등록"),
        LIST("목록"),
        DELETE("삭제"),
        UPDATE("수정"),
        BUILD("빌드")
        ;
        String name;
        COMMAND(String name) {
            this.name = name;
        }

        static COMMAND of(String name){
            switch (name){
                case "등록" -> {
                    return COMMAND.CREATE;
                }
                case "종료" -> {
                    return COMMAND.EXIT;
                }
                case "목록" -> {
                    return COMMAND.LIST;
                }
                case "수정" -> {
                    return COMMAND.UPDATE;
                }
                case "빌드" -> {
                    return COMMAND.BUILD;
                }
                case "삭제" -> {
                    return COMMAND.DELETE;
                }
                default -> throw new IllegalArgumentException("Unknown command: " + name);
            }
        }
    }
    public enum PARAMETERS{
        KEYWORD_TYPE("keywordType"),KEYWORD("keyword"),PAGE_NUM("page"),
        ID("id"), OPERATOR_SPLITTER("\\?"),PARAMETER_SPLITTER("&"), PARAMETER_VALUE_SPLITTER("="),;
        String value;
        PARAMETERS(String value) {
            this.value = value;
        }
    }

    static void exit(){
        isExited = true;
        System.out.flush();
    }

    public App(Scanner scanner) {
        this.scanner = scanner;
        controller = WiseSayingController.getInstance();
        controller.setScanner(scanner);
    }

    public boolean isExited() {
        return isExited;
    }

    public void run(){
        System.out.println("== 명언 앱 == ");
        while(true){
            System.out.print("명령) ");
            if(!scanner.hasNext()){
                System.out.flush();
                return;
            }
            String input = scanner.nextLine();
            try{
                route(input);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    private void route(String input){
        Map<String, String> parameterMap = getParsedParameterMap(input);
        String operator = parameterMap.get("operator");
        switch (operator){
            case "종료" -> exit();
            case "등록" -> controller.create();
            case "목록" -> controller.list();
            case "삭제" -> controller.delete(Long.parseLong(parameterMap.get(PARAMETERS.ID.value)));
            default -> throw new UnsupportedCommandException();
        }
    }


    private static Map<String, String> getParsedParameterMap(String input){
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put(PARAMETERS.PAGE_NUM.value, "1");


        String[] split = input.split(PARAMETERS.OPERATOR_SPLITTER.value);
        String operator = split[0];
        parameterMap.put("operator",operator);

        String parameter = split.length == 2?split[1]:"";

        if(parameter.isEmpty())
            return parameterMap;
        String[] parsedParams = parameter.split(PARAMETERS.PARAMETER_SPLITTER.value);

        for(String param : parsedParams)
            parameterMap.put(param.split(
                            PARAMETERS.PARAMETER_VALUE_SPLITTER.value)[0],
                    param.split(PARAMETERS.PARAMETER_VALUE_SPLITTER.value)[1]);

        return parameterMap;
    }
}

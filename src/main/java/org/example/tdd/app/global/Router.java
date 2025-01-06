package org.example.tdd.app.global;

import org.example.tdd.app.domain.wiseSaying.controller.WiseSayingController;
import org.example.tdd.app.common.exception.UnsupportedCommandException;

import java.util.HashMap;
import java.util.Map;

import static org.example.tdd.app.global.Router.PARAMETERS.*;

public  class Router {
    private enum COMMAND{
        EXIT("종료"),
        CREATE("등록"),
        LIST("목록"),
        DELETE("삭제"),
        UPDATE("수정"),
        BUILD("빌드")
        ;
        final String name;
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

    private final SystemController systemController;
    private final WiseSayingController wiseSayingController;

    public Router(SystemController systemController, WiseSayingController wiseSayingController) {
        this.systemController = systemController;
        this.wiseSayingController = wiseSayingController;
    }

    public void route(String input){
        Map<String, String> parameterMap = getParsedParameterMap(input);
        COMMAND operator = COMMAND.of(parameterMap.get("operator"));
        switch (operator){
            case EXIT -> systemController.exit();
            case CREATE-> wiseSayingController.create();
            case LIST -> wiseSayingController.list();
            case DELETE -> wiseSayingController.delete(Long.parseLong(parameterMap.get(ID.value)));
            case UPDATE -> wiseSayingController.update(Long.parseLong(parameterMap.get(ID.value)));
            case BUILD -> wiseSayingController.build();
            default -> throw new UnsupportedCommandException();
        }
    }

    private static Map<String, String> getParsedParameterMap(String input){
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put(PAGE_NUM.value, "1");

        String[] split = input.split(OPERATOR_SPLITTER.value);
        String operator = split[0];
        parameterMap.put("operator",operator);

        String parameter = split.length == 2?split[1]:"";

        if(parameter.isEmpty())
            return parameterMap;
        String[] parsedParams = parameter.split(PARAMETER_SPLITTER.value);

        for(String param : parsedParams)
            parameterMap.put(param.split(
                            PARAMETER_VALUE_SPLITTER.value)[0],
                    param.split(PARAMETER_VALUE_SPLITTER.value)[1]);

        return parameterMap;
    }
}

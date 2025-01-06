package org.example.tdd.app.global;

import org.example.tdd.app.domain.wiseSaying.controller.WiseSayingController;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum COMMAND{
    EXIT("종료"){
        public void route(Map<String, String> parameterMap) {
            SystemController.getInstance().exit();
        }
    },
    CREATE("등록"){
        public void route(Map<String, String> parameterMap) {
            WiseSayingController.getInstance().create();
        }
    },
    LIST("목록"){
        public void route( Map<String, String> parameterMap) {
            WiseSayingController.getInstance().list();
        }
    },
    DELETE("삭제"){
        public void route( Map<String, String> parameterMap) {
            WiseSayingController.getInstance().delete(parameterMap);

        }
    },
    UPDATE("수정"){
        public void route( Map<String, String> parameterMap) {
            WiseSayingController.getInstance().update(parameterMap);
        }
    },
    BUILD("빌드"){
        public void route( Map<String, String> parameterMap) {
            WiseSayingController.getInstance().build();

        }
    };
    final String name;
    COMMAND(String name) {
        this.name = name;
    }

    public abstract void route(Map<String, String> parameterMap);
    private static Map<String, COMMAND> COMMAND_MAP;
    static{
        COMMAND_MAP = Stream.of(values())
                .collect(Collectors.toMap(command -> command.name, command -> command));
    }

    static COMMAND of(String name){
        return COMMAND_MAP.getOrDefault(name, COMMAND.EXIT);
    }
}


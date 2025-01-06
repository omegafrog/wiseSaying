package org.example.tdd.app.global;

import org.example.tdd.app.domain.wiseSaying.controller.WiseSayingController;

import java.util.Map;

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

    static COMMAND of(String name){
        return switch (name) {
            case "등록" -> COMMAND.CREATE;
            case "종료" -> COMMAND.EXIT;
            case "목록" -> COMMAND.LIST;
            case "수정" -> COMMAND.UPDATE;
            case "빌드" -> COMMAND.BUILD;
            case "삭제" -> COMMAND.DELETE;
            default -> throw new IllegalArgumentException("Unknown command: " + name);
        };
    }

}


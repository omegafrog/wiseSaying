package org.example.tdd.app.global;

import org.example.tdd.app.domain.wiseSaying.controller.WiseSayingController;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.tdd.app.global.Parameters.*;

enum Command {
    EXIT("종료") {
        public void route(Parameters parameters) {
            SystemController.getInstance().exit();
        }
    },
    CREATE("등록") {
        public void route(Parameters parameters) {
            WiseSayingController.getInstance().create();
        }
    },
    LIST("목록") {
        public void route(Parameters parameters) {
            WiseSayingController.getInstance().list(parameters);
        }
    },
    DELETE("삭제") {
        public void route(Parameters parameters) {
            WiseSayingController.getInstance().delete(parameters);
        }
    },
    UPDATE("수정") {
        public void route(Parameters parameters) {
            WiseSayingController.getInstance().update(parameters);
        }
    },
    BUILD("빌드") {
        public void route(Parameters parameters) {
            WiseSayingController.getInstance().build();
        }
    };

    Command(String name) {
        this.name = name;
    }

    private final String name;

    public abstract void route(Parameters parameters) throws RuntimeException;

    private static final Map<String, Command> COMMAND_MAP;

    public String getName() {
        return name;
    }

    static {
        COMMAND_MAP = Stream.of(values())
                .collect(Collectors.toMap(command -> command.name, command -> command));
    }

    static Command of(String input) {
        String[] split = input.split(PARAMETER_SPLITTER);
        String commandName = split[0];
        return COMMAND_MAP.get(commandName);
    }
}


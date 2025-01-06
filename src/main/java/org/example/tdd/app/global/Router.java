package org.example.tdd.app.global;

import org.example.tdd.app.domain.wiseSaying.controller.WiseSayingController;
import org.example.tdd.app.common.exception.UnsupportedCommandException;

import java.util.HashMap;
import java.util.Map;

import static org.example.tdd.app.global.Parameters.*;
import static org.example.tdd.app.global.Command.*;

public  class Router {


    private final SystemController systemController;
    private final WiseSayingController wiseSayingController;

    public Router(SystemController systemController, WiseSayingController wiseSayingController) {
        this.systemController = systemController;
        this.wiseSayingController = wiseSayingController;
    }

    public void route(String input){
        Command command = new Command(input);
        COMMAND operator =COMMAND.of(command.commandName);
        Map<String, String> params = command.params;
        switch (operator){
            case EXIT -> systemController.exit();
            case CREATE-> wiseSayingController.create();
            case LIST -> {
                if(params.containsKey(KEYWORD_TYPE)) {
                    wiseSayingController.list(command);
                    break;
                }
                wiseSayingController.list();
            }
            case DELETE -> wiseSayingController.delete(command);
            case UPDATE -> wiseSayingController.update(command);
            case BUILD -> wiseSayingController.build();
            default -> throw new UnsupportedCommandException();
        }
    }


}

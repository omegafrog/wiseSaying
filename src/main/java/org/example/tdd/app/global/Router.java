package org.example.tdd.app.global;

import org.example.tdd.app.domain.wiseSaying.controller.WiseSayingController;
import org.example.tdd.app.common.exception.UnsupportedCommandException;

import java.util.HashMap;
import java.util.Map;

import static org.example.tdd.app.global.Parameters.*;

public  class Router {


    private final SystemController systemController;
    private final WiseSayingController wiseSayingController;

    public Router(SystemController systemController, WiseSayingController wiseSayingController) {
        this.systemController = systemController;
        this.wiseSayingController = wiseSayingController;
    }

    public void route(String input){
        Map<String, String> parameterMap = getParameterMap(input);
        COMMAND operator =COMMAND.of(parameterMap.get("operator"));
        operator.route(parameterMap);
    }
    private Map<String, String> getParameterMap(String input){
        Map<String, String> params = new HashMap<>();
        params.put(PAGE_NUM, "1");

        String[] split = input.split(OPERATOR_SPLITTER);

        String commandName = split[0];
        params.put("operator", commandName);
        String parameter = split.length == 2?split[1]:"";


        if(parameter.isEmpty())
            return params;
        String[] parsedParams = parameter.split(PARAMETER_SPLITTER);

        for(String param : parsedParams)
            params.put(param.split(PARAMETER_VALUE_SPLITTER)[0],
                    param.split(PARAMETER_VALUE_SPLITTER)[1]);

        return params;
    }


}

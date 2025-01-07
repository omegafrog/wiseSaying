package org.example.tdd.app.global;

import java.util.HashMap;
import java.util.Map;

public class Parameters {
    static final String OPERATOR = "operator";
    static final String KEYWORD_TYPE = "keywordType";
    static final String KEYWORD = "keyword";
    static final String PAGE_NUM = "page";
    static final String ID = "id";
    static final String OPERATOR_SPLITTER = "\\?";
    static final String PARAMETER_SPLITTER = "&";
    static final String PARAMETER_VALUE_SPLITTER = "=";

    private final Map<String, String> params;

    public Parameters(String input) {
        params = getParameterMap(input);
    }

    public static String PARAMETER_SPLITTER(){
        return PARAMETER_SPLITTER;
    }

    public String getOperator(){
        return params.get(OPERATOR);
    }

    public String getKeywordType(){
        return params.get(KEYWORD_TYPE);
    }
    public String getKeyword(){
        return params.get(KEYWORD);
    }
    public int getPageNum(){
        return Integer.parseInt(params.get(PAGE_NUM));
    }
    public Long getId(){
        return Long.parseLong(params.get(ID));
    }
    public boolean containsSearchParams(){
        return params.containsKey(KEYWORD_TYPE) && params.containsKey(KEYWORD);
    }

    private static Map<String, String> getParameterMap(String input){
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

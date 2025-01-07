package org.example.tdd.app.global;


public  class Router {

    public void route(String input){
        Parameters parameter = new Parameters(input);
        Command operator = Command.of(parameter.getOperator());
        operator.route(parameter);
    }
}

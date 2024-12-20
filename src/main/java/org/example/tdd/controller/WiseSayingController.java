package org.example.tdd.controller;


public class WiseSayingController {
    private static final WiseSayingController INSTANCE = new WiseSayingController();
    private WiseSayingController() {}

    public static WiseSayingController getInstance() {
        return INSTANCE;
    }

}

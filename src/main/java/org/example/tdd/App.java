package org.example.tdd;

import org.example.tdd.controller.WiseSayingController;
import org.example.tdd.exception.UnsupportedCommandException;

import java.util.Scanner;

public class App {
    private final Scanner scanner;
    private static boolean isExited = false;
    private final WiseSayingController controller;

    public static final String BASE_PATH = "db/WiseSaying";

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
            route(input);
        }
    }
    private void route(String input){
        switch (input){
            case "종료" -> exit();
            case "등록" -> controller.create();
            default -> throw new UnsupportedCommandException();
        }
    }
}

package org.example.tdd;

import org.example.tdd.controller.WiseSayingController;
import org.example.tdd.exception.UnsupportedCommandException;

import java.util.Scanner;

public class App {
    private final Scanner scanner;
    private boolean isExited = false;
    private final WiseSayingController controller;

    void exit(){
        isExited = true;
    }

    public boolean isExited() {
        return isExited;
    }

    public App(Scanner scanner, WiseSayingController controller) {
        this.scanner = scanner;
        this.controller = controller;
    }

    public void run(){
        System.out.println("== 명언 앱 == ");
        while(true){
            System.out.print("명령) ");
            if(!scanner.hasNext())
                return;
            String input = scanner.nextLine();
            route(input);
        }
    }
    private void route(String input){
        switch (input){
            case "종료" -> exit();
            default -> throw new UnsupportedCommandException();
        }
    }
}

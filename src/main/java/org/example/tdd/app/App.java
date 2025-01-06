package org.example.tdd.app;

import org.example.tdd.app.domain.wiseSaying.controller.WiseSayingController;
import org.example.tdd.app.global.SystemController;
import org.example.tdd.app.global.Router;

import java.util.Scanner;



public class App {
    private final Scanner scanner;
    private final WiseSayingController wiseSayingController;
    private final SystemController systemController;
    private final Router router;

    public static final String BASE_PATH = "db/WiseSaying";

    public App(Scanner scanner) {
        this.scanner = scanner;
        wiseSayingController = WiseSayingController.getInstance();
        systemController = new SystemController();
        router = new Router(systemController,wiseSayingController);
        wiseSayingController.setScanner(scanner);
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
            try{
                router.route(input);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    public boolean isExisted(){
        return systemController.isExited();
    }



}

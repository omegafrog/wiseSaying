package org.example;

import java.util.Scanner;

import static java.lang.System.exit;

public class App {
    private final WiseSayingController controller;
    private final Scanner scanner;

    public App(WiseSayingController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void run(){
        System.out.println("== 명언 앱 == ");
        while(true){
            System.out.print("명령) ");
            // 임시방편 처리. 스캐너의 입력이 비어있는 채로 nextLine을 만나면 오류를 뱉어서
            // 입력 버퍼가 비었으면 출력 버퍼를 다 비우고 종료시킴
            if(!scanner.hasNextLine()){
                System.out.flush();
                return;
            }
            String input = scanner.nextLine();


            String[] split = input.split("\\?");
            String operator = split[0];
            String parameter = "";
            if(split.length == 2)
                parameter = split[1];

            switch (operator) {
                case "종료" -> controller.exit();
                case "등록" -> controller.create();
                case "목록" -> {
                    if( parameter.length() > 0){
                        controller.findBy(parameter);
                        break;
                    }
                    controller.readAll(1);
                }
                case "삭제" -> controller.delete(parameter);
                case "수정" -> controller.update(parameter);
                case "빌드" -> controller.build();
                default -> throw new IllegalArgumentException("Unknown operator: " + input);
            }
        }
    }
}

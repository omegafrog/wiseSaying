package org.example.tdd.app.domain.wiseSaying.controller;


import org.example.nonTdd.Page;
import org.example.tdd.app.domain.wiseSaying.entity.WiseSaying;
import org.example.tdd.app.domain.wiseSaying.service.WiseSayingService;
import org.example.tdd.app.global.Parameters;

import java.util.List;
import java.util.Scanner;


public class WiseSayingController{
    private static final WiseSayingController INSTANCE = new WiseSayingController();
    private WiseSayingController() {}
    private Scanner scanner;

    private final WiseSayingService service = WiseSayingService.getInstance();

    public static WiseSayingController getInstance() {
        return INSTANCE;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void create() {
        System.out.print("명언 : ");
        String content = scanner.nextLine();

        System.out.print("작가 : ");
        String author = scanner.nextLine();

        WiseSaying saved = service.create(content, author);
        System.out.println(saved.getId() + "번 명언이 등록되었습니다.");
    }

//    public void list() {
////        printResult(service.findAll());
//        printResult(service.listPage(1));
//    }
    public void list(Parameters parameters){
        if(parameters.containsSearchParams()){
            List<WiseSaying> wiseSayingList = service.search(parameters.getKeywordType(),
                    parameters.getKeyword(), parameters.getPageNum());
            printResult(wiseSayingList);
        }else{
            printResult(service.listPage(parameters.getPageNum()));
        }

    }

    private static void printResult(List<WiseSaying> all) {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        for (WiseSaying ws : all)
            System.out.println(ws.getId()+" / "+ws.getAuthor()+" / "+ws.getContent());
    }
    private static void printResult(Page<WiseSaying> page) {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        for (WiseSaying ws : page.getContents())
            System.out.println(ws.getId() + " / " + ws.getAuthor() + " / " + ws.getContent());
        System.out.println(page);
    }

    public void delete(Parameters parameters) {
        Long id = parameters.getId();
        service.delete(id);
        System.out.println(id + "번 명언이 삭제되었습니다.");
    }

    public void update(Parameters parameters) {
        Long id = parameters.getId();
        WiseSaying wiseSaying = service.getItem(id);
        System.out.println("명언(기존) : "+wiseSaying.getContent() );
        System.out.print("명언 : ");
        String changedContent = scanner.nextLine();

        System.out.println("작가(기존) : "+wiseSaying.getAuthor() );
        System.out.print("작가 : ");
        String changedAuthor = scanner.nextLine();

        wiseSaying.update(changedContent, changedAuthor);
        service.update(id, wiseSaying);
    }

    public void build() {
        service.build();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}

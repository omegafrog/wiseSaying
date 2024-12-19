package org.example;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private final Scanner scanner;
    private final WiseSayingService service;

    public WiseSayingController(Scanner scanner, WiseSayingService service) {
        this.scanner = scanner;
        this.service = service;
    }

    public void exit() {
        scanner.close();

        System.exit(0);
    }

    public void create() {
        System.out.print("명언 : ");
        String content = scanner.nextLine();

        System.out.print("작가 : ");
        String author = scanner.nextLine();

        WiseSaying saved = service.create(content, author);

        System.out.println(saved.getId() + "번 명언이 등록되었습니다.");
    }

    public void readAll(int pageNum) {
        List<WiseSaying> all = service.findAll();

        printResult(all, pageNum );
    }

    private static void printResult(List<WiseSaying> all, int pageNum) {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
//        for (WiseSaying quote : all) {
//            System.out.println(quote.getId()+" / "+quote.getAuthor()+" / "+quote.getContent());
//        }
        List<WiseSaying> sorted = all.stream().sorted(Comparator.comparing(WiseSaying::getId).reversed()).toList();


        int fromIdx = (pageNum-1)*5;
        int toIdx = Math.min((pageNum) * 5, sorted.size());
        Page paged = new Page(sorted.subList(fromIdx, toIdx), sorted.size(), 5, pageNum);
        for (WiseSaying ws : paged.getContents()) {
            System.out.println(ws.getId()+" / "+ws.getAuthor()+" / "+ws.getContent());
        }
        System.out.println(paged);

    }


    public void delete(String parameter) {
        String[] parsedSplit = parameter.split("\\=");
        int id = Integer.parseInt(parsedSplit[1]);
        service.remove(id);
        System.out.println(id+"번 명언이 삭제되었습니다.");
    }

    public void update(String parameter) {
        // parse parameter
        String[] split = parameter.split("=");
        if (split.length == 1) throw new IllegalArgumentException("Wrong parameter:" + parameter);
        int id = Integer.parseInt(split[1]);

        //get quote
        WiseSaying wiseSaying = service.find(id);
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

    public void findBy(String parameter) {
        String[] parsedParams = parameter.split("&");
        String type = "";
        String word = "";
        int page = 1;
        for(String param : parsedParams){
            String operator = param.split("=")[0].trim();
            String operand = param.split("=")[1].trim();
            if(operator.equals("keywordType"))
                type = operand;
            if(operator.equals("keyword"))
                word = operand;
            if(operator.equals("page"))
                page = Integer.parseInt(operand);
        }

        if(type.isEmpty() && word.isEmpty()){
            List<WiseSaying> all = service.findAll();
            printResult(all, page);
        }else{
            System.out.println("----------------");
            System.out.println("검색타입 : " + type);
            System.out.println("검색어 : " + word);
            System.out.println("----------------");
            List<WiseSaying> founded = service.findBy(type, word);
            printResult(founded, page);
        }

    }
}

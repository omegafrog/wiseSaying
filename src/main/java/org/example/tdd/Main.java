package org.example.tdd;

import org.example.tdd.controller.WiseSayingController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        WiseSayingController controller = WiseSayingController.getInstance();
        App app = new App(new Scanner(System.in), controller);
        app.run();
    }
}
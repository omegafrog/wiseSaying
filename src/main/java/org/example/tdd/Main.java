package org.example.tdd;


import org.example.tdd.app.App;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        App app = new App(new Scanner(System.in));
        app.run();
    }
}
package org.example.tdd.app.global;

public class SystemController {
    private static boolean isExited = false;
    public boolean isExited() {
        return isExited;
    }

    public void exit(){
        isExited = true;
        System.out.flush();
    }
}

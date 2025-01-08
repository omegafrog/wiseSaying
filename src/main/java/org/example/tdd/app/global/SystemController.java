package org.example.tdd.app.global;

public class SystemController{
    private static final SystemController INSTANCE = new SystemController();

    public static SystemController getInstance(){
        return INSTANCE;
    }
    private boolean isExited = false;

    public boolean isExited() {
        return isExited;
    }

    private SystemController() {}
    public void exit() {
        isExited = true;
        System.out.flush();
    }

    public void setExit(boolean b) {
        isExited = b;
    }
}

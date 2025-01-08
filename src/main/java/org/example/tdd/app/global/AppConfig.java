package org.example.tdd.app.global;

import lombok.Getter;

public class AppConfig {
    @Getter
    public enum Mode{
        DEV("dev"),PROD("prod"), TEST("test");
        private String mode;
        Mode(String mode) {
            this.mode = mode;
        }
    }
    public enum DbMode{
        FILE("file"), MEM("mem"),;
        private String mode;
        DbMode(String mode) {
            this.mode = mode;
        }
    }
    @Getter private static Mode mode = Mode.DEV;
    @Getter  private static DbMode dbMode = DbMode.FILE;
    private static final String BASE_PATH = "db/";

    public static String getDbPath(){
        return BASE_PATH + mode.getMode();
    }
    public static void setDbMode(DbMode mode) {
        AppConfig.dbMode = mode;
    }
    public static void setMode(Mode mode) {
        AppConfig.mode = mode;
    }
}

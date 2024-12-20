package org.example.tdd.exception;

public class UnsupportedCommandException extends CommandException {
    private final String message = "지원하지 않는 명령어입니다.";

    public UnsupportedCommandException() {
        super();
    }
}

package org.example.tdd.app.common.exception;

public class CommandException extends RuntimeException {

    public CommandException(Throwable cause, String message) {
        super(message, cause);
    }
    public CommandException() {
        super();
    }
}

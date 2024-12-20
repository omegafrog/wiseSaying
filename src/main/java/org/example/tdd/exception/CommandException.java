package org.example.tdd.exception;

public class CommandException extends RuntimeException {

    public CommandException(Throwable cause, String message) {
        super(message, cause);
    }
    public CommandException() {
        super();
    }
}

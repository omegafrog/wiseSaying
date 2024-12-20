package org.example.tdd.exception;

public class PersistenceException extends RuntimeException {
    public PersistenceException(String message) {
        super(message);
    }
}

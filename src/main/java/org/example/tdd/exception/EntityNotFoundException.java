package org.example.tdd.exception;

public class EntityNotFoundException extends PersistenceException{
    public EntityNotFoundException(String message) {
        super(message);
    }
}

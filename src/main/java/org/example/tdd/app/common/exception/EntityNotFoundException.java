package org.example.tdd.app.common.exception;

public class EntityNotFoundException extends PersistenceException{
    public EntityNotFoundException(String message) {
        super(message);
    }
}

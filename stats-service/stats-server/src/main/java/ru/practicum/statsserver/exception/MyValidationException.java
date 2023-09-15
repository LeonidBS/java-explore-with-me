package ru.practicum.statsserver.exception;

public class MyValidationException extends RuntimeException {
    public MyValidationException(String message) {
        super(message);
    }
}
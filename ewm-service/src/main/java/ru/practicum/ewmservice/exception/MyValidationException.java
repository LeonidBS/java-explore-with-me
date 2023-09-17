package ru.practicum.ewmservice.exception;

public class MyValidationException extends RuntimeException {
    public MyValidationException(String message) {
        super(message);
    }
}
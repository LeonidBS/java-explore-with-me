package ru.practicum.statsclient.exception;

public class MyValidationException extends RuntimeException {
    public MyValidationException(String message) {
        super(message);
    }
}
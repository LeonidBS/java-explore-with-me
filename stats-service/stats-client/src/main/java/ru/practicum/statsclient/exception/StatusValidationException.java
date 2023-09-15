package ru.practicum.statsclient.exception;

public class StatusValidationException extends RuntimeException {
    public StatusValidationException(String message) {
        super(message);
    }
}

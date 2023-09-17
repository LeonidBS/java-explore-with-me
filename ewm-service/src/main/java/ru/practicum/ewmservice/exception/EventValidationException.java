package ru.practicum.ewmservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EventValidationException extends RuntimeException {
    public EventValidationException(String message) {
        super(message);
    }
}
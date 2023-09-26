package ru.practicum.statsclient.exception;

import lombok.Getter;

@Getter
public class ResponseEntityErrorException extends RuntimeException {

    public ResponseEntityErrorException(String message) {
        super(message);
    }
}
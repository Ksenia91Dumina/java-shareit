package ru.practicum.shareit.exception;

public class ValidateException extends IllegalArgumentException {
    public ValidateException(String message) {
        super(message);
    }
}
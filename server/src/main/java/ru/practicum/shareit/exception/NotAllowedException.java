package ru.practicum.shareit.exception;

public class NotAllowedException extends IllegalAccessError  {
    public NotAllowedException(String message) {
        super(message);
    }
}
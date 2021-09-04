package com.ivolodin.model.exceptions;

public class PathNotExistException extends RuntimeException {
    public PathNotExistException(String message) {
        super(message);
    }
}

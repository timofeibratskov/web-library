package com.example.demolibrary.exceptions;

public class BookNotReturnedException extends RuntimeException {
    public BookNotReturnedException(String message) {
        super(message);
    }
}

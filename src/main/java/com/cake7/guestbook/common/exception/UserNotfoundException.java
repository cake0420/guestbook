package com.cake7.guestbook.common.exception;

public class UserNotfoundException extends RuntimeException {
    public UserNotfoundException(String message) {
        super(message);
    }
}

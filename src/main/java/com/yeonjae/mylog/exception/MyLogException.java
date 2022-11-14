package com.yeonjae.mylog.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

// 비즈니스 최상위 exception
@Getter
public abstract class MyLogException extends RuntimeException{

    private final Map<String, String> validation = new HashMap<>();

    public MyLogException(String message) {
        super(message);
    }

    public MyLogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    };
}

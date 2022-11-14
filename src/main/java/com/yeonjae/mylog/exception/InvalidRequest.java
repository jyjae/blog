package com.yeonjae.mylog.exception;

import lombok.Getter;

/**
 * status = 400
 */
@Getter
public class InvalidRequest extends MyLogException {

    private static final String MESSAGE = "유효하지 않은 요청입니다.";


    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}

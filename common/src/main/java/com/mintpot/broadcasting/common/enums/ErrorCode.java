package com.mintpot.broadcasting.common.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    DATE_RANGE_INVALID(18002, HttpStatus.BAD_REQUEST, "상담사를 선택해주세요."),
    VALIDATION_FAILED(1006, HttpStatus.BAD_REQUEST, "확인에 실패했습니다. 다시 시도하십시오."),
    JWT_EXPIRED(12000, HttpStatus.BAD_REQUEST, "JWT가 만료되었습니다."),
    OBJECT_NOTFOUND(18002, HttpStatus.BAD_REQUEST, "Entry not found"),
    USER_NOTFOUND(18003, HttpStatus.BAD_REQUEST, "User not found"),
    AUTHENTICATE_INVALID(18004, HttpStatus.UNAUTHORIZED, "Authenticate failure");

    private final int code;

    private final HttpStatus httpStatus;

    private final String message;

    ErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}

package com.mintpot.broadcasting.common.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
    private String object;

    private String field;

    private Object rejectedValue;

    private String message;

    public ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}

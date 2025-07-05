package com.example.legacy.global.error;

import com.example.legacy.global.enums.BaseErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    final BaseErrorCode errorCode;

    public CustomException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

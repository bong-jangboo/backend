package com.example.jangboo.global.error;

import com.example.jangboo.global.enums.BaseErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    final BaseErrorCode errorCode;

    public CustomException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

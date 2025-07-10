package com.bongjangboo.legacy.global.error;

import com.bongjangboo.legacy.global.enums.BaseErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    final BaseErrorCode errorCode;

    public CustomException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

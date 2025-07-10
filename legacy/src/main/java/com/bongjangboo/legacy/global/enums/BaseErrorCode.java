package com.bongjangboo.legacy.global.enums;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    HttpStatus getHttpStatus();
    String getMessage();
    String getErrorCode();
}

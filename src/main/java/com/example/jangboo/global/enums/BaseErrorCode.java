package com.example.jangboo.global.enums;

public interface BaseErrorCode {
    Integer getHttpStatus();
    String getMessage();
    String getErrorCode();
}

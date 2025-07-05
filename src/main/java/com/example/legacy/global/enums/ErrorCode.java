package com.example.legacy.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode implements BaseErrorCode{
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청이 올바르지 않습니다.", "ERROR-BR-000"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다.", "ERROR-UA-000"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 요청 정보를 찾을 수 없습니다.", "ERROR-NF-000"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "금지된 요청입니다.", "ERROR-FB-000"),
    CONFLICT(HttpStatus.CONFLICT, "해당 요청에 대해 충돌이 일어났습니다.", "ERROR-CF-000"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다. 관리자에게 문의하세요", "ERROR-ISE-000");


    private final HttpStatus httpStatus;
    private final String message;
    private final String errorCode;
}

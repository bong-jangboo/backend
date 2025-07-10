package com.bongjangboo.legacy.receipt.error;

import com.bongjangboo.legacy.global.enums.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum OcrErrorCode implements BaseErrorCode {
    OCR_FAIL(HttpStatus.BAD_REQUEST, "OCR 이미지 데이터 처리에 실패했습니다.", "ERROR-OCR-000");

    private final HttpStatus httpStatus;
    private final String message;
    private final String errorCode;
}

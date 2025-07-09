package com.bongjangboo.shared.response;

import com.bongjangboo.shared.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {

    private ResponseUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 성공 응답시 사용
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        ApiResponse<T> response = ApiResponse.success(data);
        return ResponseEntity.ok(response);
    }

    /**
     * 실패 응답
     */
    public static ResponseEntity<ApiResponse<?>> failure(HttpStatus httpStatus, ErrorCode errorCode, String message) {
        ApiError error = ApiError.builder()
                .code(errorCode.getCode())
                .message(message)
                .build();

        ApiResponse<?> response = ApiResponse.failure(error);
        return ResponseEntity.status(httpStatus).body(response);
    }

    /**
     * 실패 응답 + 필드 에러
     */
    public static ResponseEntity<ApiResponse<?>> failureWithFieldErrors(HttpStatus httpStatus, ErrorCode errorCode, String message, List<ApiError.FieldError> fieldErrors) {
        ApiError error = ApiError.builder()
                .code(errorCode.getCode())
                .message(message)
                .fieldErrors(fieldErrors)
                .build();

        ApiResponse<?> response = ApiResponse.failure(error);
        return ResponseEntity.status(httpStatus).body(response);
    }




}

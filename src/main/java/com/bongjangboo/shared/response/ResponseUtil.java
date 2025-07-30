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
     * [성공 응답] 데이터를 포함한 성공 응답 반환
     *
     * @param data 응답 본문에 담길 데이터
     * @return 200 OK + ApiResponse(success=true, data)
     *
     * 사용 예:
     * return ResponseUtil.success(MemberResponse.from(member));
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        ApiResponse<T> response = ApiResponse.success(data);
        return ResponseEntity.ok(response);
    }

    /**
     * [실패 응답] 에러 코드 및 메시지를 포함한 실패 응답 반환
     *
     * @param httpStatus HTTP 응답 상태 (예: 400, 404, 500 등)
     * @param errorCode 에러 코드 (내부 ErrorCode enum 등)
     * @param message 에러 메시지
     * @return ResponseEntity<ApiResponse<null>> (success=false, error 포함)
     *
     * 사용 예:
     * return ResponseUtil.failure(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT, "입력값 오류");
     */
    public static ResponseEntity<ApiResponse<Void>> failure(HttpStatus httpStatus, ErrorCode errorCode, String message) {
        ApiError error = ApiError.builder()
                .code(errorCode.getCode())
                .message(message)
                .build();
        // <?> : 와일드 카드 사용 자제 -> 실패시 data에 값이 없기 때문에 void
        ApiResponse<Void> response = ApiResponse.failure(error);
        return ResponseEntity.status(httpStatus).body(response);
    }

    // 별도의 메시지 없이 error code의 메세지 사용
    public static ResponseEntity<ApiResponse<Void>> failure(HttpStatus httpStatus, ErrorCode errorCode) {
        return failure(httpStatus, errorCode, errorCode.getMessage());
    }

    /**
     * [실패 응답 + 필드 에러] 유효성 검증 오류 등 필드 에러까지 포함하는 실패 응답
     *
     * @param httpStatus HTTP 상태 코드
     * @param errorCode 시스템 내부 에러 코드
     * @param message 에러 메시지
     * @param fieldErrors 필드 단위 에러 목록 (ApiError.FieldError 구조)
     * @return ResponseEntity<ApiResponse<null>> (success=false, error + fieldErrors 포함)
     *
     * 사용 예:
     * return ResponseUtil.failureWithFieldErrors(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED, "입력 오류", fieldErrors);
     */
    public static ResponseEntity<ApiResponse<Void>> failureWithFieldErrors(HttpStatus httpStatus, ErrorCode errorCode, String message, List<ApiError.FieldError> fieldErrors) {
        ApiError error = ApiError.builder()
                .code(errorCode.getCode())
                .message(message)
                .fieldErrors(fieldErrors)
                .build();

        ApiResponse<Void> response = ApiResponse.failure(error);
        return ResponseEntity.status(httpStatus).body(response);
    }

    // 별도의 메시지 없이 error code의 메세지 사용
    public static ResponseEntity<ApiResponse<Void>> failureWithFieldErrors(HttpStatus httpStatus, ErrorCode errorCode, List<ApiError.FieldError> fieldErrors) {
        return failureWithFieldErrors(httpStatus, errorCode, errorCode.getMessage(), fieldErrors);
    }



}

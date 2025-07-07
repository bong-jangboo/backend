package com.example.jangboo.shared.exception;

import com.example.jangboo.shared.response.ApiError;
import com.example.jangboo.shared.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * GlobalExceptionHandler
 *
 * <p>애플리케이션 전역에서 발생하는 예외를 잡아서
 * 표준화된 JSON 형태의 응답으로 변환해주는 역할을 한다.
 * 주로 다음과 같은 예외들을 처리한다.
 *
 * <ul>
 *     <li>비즈니스 예외 (BusinessException)</li>
 *     <li>입력값 검증 실패 (MethodArgumentNotValidException)</li>
 *     <li>JSON 파싱 오류 (HttpMessageNotReadableException)</li>
 *     <li>존재하지 않는 URL 요청 (NoHandlerFoundException)
 *         <br/>※ 주의: application.yml 에
 *         spring.mvc.throw-exception-if-no-handler-found=true,
 *         spring.web.resources.add-mappings=false 설정이 필요하다.
 *     </li>
 *     <li>허용되지 않은 HTTP 메서드 (HttpRequestMethodNotSupportedException)</li>
 *     <li>그 외 처리되지 않은 예외 (Exception)</li>
 * </ul>
 *
 * <p>모든 예외는 ApiResponse.failure 형태로 표준화된
 * JSON 응답을 내려 클라이언트 일관성을 유지한다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 예외
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("""
                [BusinessException]
                Path   : {}
                Method : {}
                Error  : {} ({})
                """,
                request.getRequestURI(),
                request.getMethod(),
                e.getErrorCode().getCode(),
                e.getMessage()
        );

        ApiError error = ApiError.builder()
                .code(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(error));
    }


    /**
     * 검증 오류
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<ApiError.FieldError> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(field -> ApiError.FieldError.builder()
                        .field(field.getField())
                        .message(field.getDefaultMessage())
                        .build())
                .toList();

        log.warn("""
                [ValidationException]
                Path   : {}
                Method : {}
                FieldErrors: {}
                """,
                request.getRequestURI(),
                request.getMethod(),
                fieldErrors
        );

        ApiError error = ApiError.builder()
                .code(CommonErrorCode.INVALID_INPUT.getCode())
                .message(CommonErrorCode.INVALID_INPUT.getMessage())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(error));
    }

    /**
     * JSON 파싱 오류 (예: Enum 값 잘못됨, JSON 형식 불일치 등)
     * Jackson 이 바인딩 중에 실패한 경우 HttpMessageNotReadableException 발생
     * → 클라이언트의 잘못된 요청으로 판단하여 400 응답 처리
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(
            org.springframework.http.converter.HttpMessageNotReadableException e,
            HttpServletRequest request
    ) {
        log.warn("""
                [HttpMessageNotReadableException]
                Path   : {}
                Method : {}
                Message: {}
                """,
                request.getRequestURI(),
                request.getMethod(),
                e.getMessage()
        );

        ApiError error = ApiError.builder()
                .code(CommonErrorCode.JSON_PARSE_ERROR.getCode())
                .message(CommonErrorCode.JSON_PARSE_ERROR.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(error));
    }


    /**
     * 잘못된 URL 요청 (404)
     */
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandlerFoundException(
            org.springframework.web.servlet.NoHandlerFoundException e,
            HttpServletRequest request
    ) {
        log.warn("""
            [NoHandlerFoundException]
            Path   : {}
            Method : {}
            Message: {}
            """,
                request.getRequestURI(),
                request.getMethod(),
                e.getMessage()
        );

        ApiError error = ApiError.builder()
                .code(CommonErrorCode.NOT_FOUND.getCode())
                .message(CommonErrorCode.NOT_FOUND.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure(error));
    }


    /**
     * 잘못된 HTTP 메서드 (405)
     */
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotAllowedException(
            org.springframework.web.HttpRequestMethodNotSupportedException e,
            HttpServletRequest request
    ) {
        log.warn("""
            [HttpRequestMethodNotSupportedException]
            Path   : {}
            Method : {}
            Message: {}
            """,
                request.getRequestURI(),
                request.getMethod(),
                e.getMessage()
        );

        ApiError error = ApiError.builder()
                .code(CommonErrorCode.METHOD_NOT_ALLOWED.getCode())
                .message(CommonErrorCode.METHOD_NOT_ALLOWED.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.failure(error));
    }


    /**
     * 시스템 예외 (예상치 못한 에러)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e, HttpServletRequest request) {
        log.error("""
                [SystemException]
                Path   : {}
                Method : {}
                Message: {}
                """,
                request.getRequestURI(),
                request.getMethod(),
                e.getMessage(),
                e
        );

        ApiError error = ApiError.builder()
                .code(CommonErrorCode.INTERNAL_ERROR.getCode())
                .message(CommonErrorCode.INTERNAL_ERROR.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(error));
    }



}

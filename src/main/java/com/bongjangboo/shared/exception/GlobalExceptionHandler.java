package com.bongjangboo.shared.exception;

import com.bongjangboo.shared.response.ApiError;
import com.bongjangboo.shared.response.ApiResponse;
import com.bongjangboo.shared.response.ResponseUtil;
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
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e, HttpServletRequest request) {
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

        // 특정 에러 코드에 대해 다른 HTTP 상태 코드 반환
        HttpStatus status = switch (e.getErrorCode().getCode()) {
            case "MEMBER_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.BAD_REQUEST;
        };

        return ResponseUtil.failure(
                status,
                e.getErrorCode(),
                e.getMessage()
        );
    }


    /**
     * 검증 오류
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
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

        return ResponseUtil.failureWithFieldErrors(
                HttpStatus.BAD_REQUEST,
                CommonErrorCode.INVALID_INPUT,
                fieldErrors
        );
    }

    /**
     * JSON 파싱 오류 (예: Enum 값 잘못됨, JSON 형식 불일치 등)
     * Jackson 이 바인딩 중에 실패한 경우 HttpMessageNotReadableException 발생
     * → 클라이언트의 잘못된 요청으로 판단하여 400 응답 처리
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
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

        return ResponseUtil.failure(
                HttpStatus.BAD_REQUEST,
                CommonErrorCode.JSON_PARSE_ERROR
        );
    }


    /**
     * 잘못된 URL 요청 (404)
     */
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFoundException(
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

        return ResponseUtil.failure(
                HttpStatus.NOT_FOUND,
                CommonErrorCode.NOT_FOUND
        );
    }


    /**
     * 잘못된 HTTP 메서드 (405)
     */
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowedException(
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

        return ResponseUtil.failure(
                HttpStatus.METHOD_NOT_ALLOWED,
                CommonErrorCode.METHOD_NOT_ALLOWED
        );
    }

    /**
     * 지원하지 않는 Content-Type (415)
     */
    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMediaTypeNotSupportedException(
            org.springframework.web.HttpMediaTypeNotSupportedException e,
            HttpServletRequest request
    ) {
        log.warn("""
            [HttpMediaTypeNotSupportedException]
            Path   : {}
            Method : {}
            Message: {}
            """,
                request.getRequestURI(),
                request.getMethod(),
                e.getMessage()
        );

        return ResponseUtil.failure(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                CommonErrorCode.UNSUPPORTED_MEDIA_TYPE
        );
    }

    /**
     * 파라미터 타입 변환 실패 (400)
     */
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException e,
            HttpServletRequest request
    ) {
        log.warn("""
            [MethodArgumentTypeMismatchException]
            Path   : {}
            Method : {}
            Parameter: {}
            Message: {}
            """,
                request.getRequestURI(),
                request.getMethod(),
                e.getName(),
                e.getMessage()
        );

        return ResponseUtil.failure(
                HttpStatus.BAD_REQUEST,
                CommonErrorCode.INVALID_INPUT,
                "잘못된 파라미터 형식입니다: " + e.getName()
        );
    }


    /**
     * 시스템 예외 (예상치 못한 에러)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e, HttpServletRequest request) {
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

        return ResponseUtil.failure(
                HttpStatus.INTERNAL_SERVER_ERROR,
                CommonErrorCode.INTERNAL_ERROR
        );
    }



}

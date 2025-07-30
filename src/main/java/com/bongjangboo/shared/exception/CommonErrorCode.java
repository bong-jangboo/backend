package com.bongjangboo.shared.exception;

public enum CommonErrorCode implements ErrorCode{
    INTERNAL_ERROR("INTERNAL_ERROR", "서버 내부 오류가 발생했습니다."),
    INVALID_INPUT("INVALID_INPUT", "요청이 잘못되었습니다."),
    AUTH_REQUIRED("AUTH_REQUIRED", "로그인이 필요합니다."),
    PERMISSION_DENIED("PERMISSION_DENIED", "권한이 없습니다."),
    JSON_PARSE_ERROR("JSON_PARSE_ERROR", "잘못된 JSON 형식입니다. 파싱에 실패했습니다."),
    NOT_FOUND("NOT_FOUND", "요청하신 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "허용되지 않은 HTTP 메서드입니다."),
    UNSUPPORTED_MEDIA_TYPE("UNSUPPORTED_MEDIA_TYPE", "지원하지 않는 Content-Type입니다.");

    private final String code;
    private final String message;

    CommonErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() { return code; }

    @Override
    public String getMessage() { return message; }

}

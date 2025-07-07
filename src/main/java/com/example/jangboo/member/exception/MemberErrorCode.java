package com.example.jangboo.member.exception;

import com.example.jangboo.shared.exception.ErrorCode;

public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "회원이 존재하지 않습니다."),
    DUPLICATE_SOCIAL_ID("DUPLICATE_SOCIAL_ID", "이미 가입된 소셜 계정입니다."),
    CANNOT_UPDATE_DEACTIVATED("CANNOT_UPDATE_DEACTIVATED", "DEACTIVATED 상태에서는 프로필 수정이 불가합니다."),
    EMAIL_ALREADY_REGISTERED("EMAIL_ALREADY_REGISTERED", "이미 이메일이 등록된 회원입니다."),
    PHONE_ALREADY_REGISTERED("PHONE_ALREADY_REGISTERED", "이미 전화번호가 등록된 회원입니다."),
    SLEEP_ONLY_ACTIVE("SLEEP_ONLY_ACTIVE", "휴면 전환은 ACTIVE 상태에서만 가능합니다.");

    private final String code;
    private final String message;

    MemberErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() { return code; }
    @Override
    public String getMessage() { return message; }
}

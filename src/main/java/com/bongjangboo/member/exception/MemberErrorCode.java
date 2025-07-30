package com.bongjangboo.member.exception;

import com.bongjangboo.shared.exception.ErrorCode;

public enum MemberErrorCode implements ErrorCode {

    // 조회 도메인
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "회원이 존재하지 않습니다."),

    // 소셜 ID 도메인
    DUPLICATE_SOCIAL_ID("DUPLICATE_SOCIAL_ID", "이미 가입된 소셜 계정입니다."),

    // 이메일 도메인
    EMAIL_ALREADY_REGISTERED("EMAIL_ALREADY_REGISTERED", "이미 이메일이 등록된 회원입니다."),
    EMAIL_ALREADY_IN_USE("EMAIL_ALREADY_IN_USE", "이미 사용 중인 이메일입니다."),
    MEMBER_EMAIL_NOT_REGISTERED("USER_EMAIL_NOT_REGISTERED", "사용자의 이메일이 등록되지 않았습니다. 먼저 이메일을 등록해주세요."),
    EMAIL_INVALID_FORMAT("EMAIL_INVALID_FORMAT", "이메일 형식이 올바르지 않습니다"),

    // 전화번호 도메인
    PHONE_ALREADY_REGISTERED("PHONE_ALREADY_REGISTERED", "이미 전화번호가 등록된 회원입니다."),
    PHONE_ALREADY_IN_USE("PHONE_ALREADY_IN_USE", "이미 사용 중인 전화번호입니다."),
    MEMBER_PHONE_NOT_REGISTERED("USER_PHONE_NOT_REGISTERED", "사용자의 전화번호가 등록되지 않았습니다. 먼저 전화번호를 등록해주세요."),
    PHONE_INVALID_FORMAT("PHONE_INVALID_FORMAT", "전화번호 형식이 올바르지 않습니다."),

    // 닉네임 도메인
    NICKNAME_ALREADY_IN_USE("NICKNAME_ALREADY_IN_USE", "이미 사용 중인 닉네임입니다."),
    NICKNAME_INVALID_FORMAT("NICKNAME_INVALID_FORMAT", "닉네임 형식이 올바르지 않습니다"),

    // 상태 제약 도메인
    CANNOT_UPDATE_DEACTIVATED("CANNOT_UPDATE_DEACTIVATED", "DEACTIVATED 상태에서는 프로필을 수정할 수 없습니다."),
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

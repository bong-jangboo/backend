package com.bongjangboo.member.presentation.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class MemberActionResponse {
    private String message;
    private LocalDateTime timestamp;

    public static MemberActionResponse success(String message) {
        return MemberActionResponse.builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

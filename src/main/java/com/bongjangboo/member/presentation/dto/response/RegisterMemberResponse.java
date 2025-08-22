package com.bongjangboo.member.presentation.dto.response;

import com.bongjangboo.member.domain.Member;
import com.bongjangboo.member.domain.MemberStatus;
import com.bongjangboo.auth.domain.oauth.SocialProvider;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RegisterMemberResponse {
    private Long id;
    private String name;
    private String nickname;
    private SocialProvider socialProvider;
    private MemberStatus status;
    private LocalDateTime createdAt;

    public static RegisterMemberResponse from(Member member) {
        return RegisterMemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .socialProvider(member.getSocialProvider())
                .status(member.getStatus())
                .createdAt(member.getCreatedAt())
                .build();
    }
}

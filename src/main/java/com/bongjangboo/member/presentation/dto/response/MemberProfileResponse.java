package com.bongjangboo.member.presentation.dto.response;

import com.bongjangboo.member.domain.Member;
import com.bongjangboo.member.domain.MemberStatus;
import com.bongjangboo.member.domain.SocialProvider;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberProfileResponse {
    private Long id;
    private String email;        // nullable
    private String phoneNumber;  // nullable
    private String name;
    private String nickname;
    private MemberStatus status;
    private SocialProvider socialProvider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemberProfileResponse from(Member member) {
        return MemberProfileResponse.builder()
                .id(member.getId())
                .email(member.getEmail() != null ? member.getEmail().getValue() : null)
                .phoneNumber(member.getPhoneNumber() != null ? member.getPhoneNumber().getValue() : null)
                .name(member.getName())
                .nickname(member.getNickname())
                .status(member.getStatus())
                .socialProvider(member.getSocialProvider())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

}

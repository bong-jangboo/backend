package com.example.jangboo.member.domain.event;

import com.example.jangboo.member.domain.SocialProvider;
import com.example.jangboo.member.domain.vo.Email;
import com.example.jangboo.shared.event.DomainEvent;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class MemberRegisteredEvent implements DomainEvent {
    Long memberId;
    SocialProvider socialProvider;
    String socialId;
    Email email;
    LocalDateTime registeredAt;
}

package com.bongjangboo.member.domain.event;

import com.bongjangboo.auth.domain.oauth.SocialProvider;
import com.bongjangboo.member.domain.vo.Email;
import com.bongjangboo.shared.event.DomainEvent;
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

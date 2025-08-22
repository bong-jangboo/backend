package com.bongjangboo.member.application.command;

import com.bongjangboo.member.domain.SocialProvider;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisterMemberFromSocialCommand {
    String name;
    String email;
    SocialProvider socialProvider;
    String socialId;
}

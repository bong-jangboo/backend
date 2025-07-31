package com.bongjangboo.member.application.command;

import com.bongjangboo.shared.identity.SocialProvider;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisterMemberCommand {
    String name;
    String nickname;
    SocialProvider socialProvider;
    String socialId;
}

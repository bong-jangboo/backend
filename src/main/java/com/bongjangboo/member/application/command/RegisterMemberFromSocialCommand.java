package com.bongjangboo.member.application.command;

import com.bongjangboo.auth.domain.oauth.SocialProvider;
import com.bongjangboo.member.domain.vo.Email;
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

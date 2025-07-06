package com.example.jangboo.member.application.command;

import com.example.jangboo.member.domain.SocialProvider;
import com.example.jangboo.member.domain.vo.Email;
import com.example.jangboo.member.domain.vo.PhoneNumber;
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

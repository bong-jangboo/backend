package com.bongjangboo.member.application.command;

import com.bongjangboo.member.domain.vo.Email;
import com.bongjangboo.member.domain.vo.PhoneNumber;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateProfileCommand {
    Long memberId;
    String nickname;
    Email email;
    PhoneNumber phoneNumber;
}

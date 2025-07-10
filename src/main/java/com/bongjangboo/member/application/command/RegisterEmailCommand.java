package com.bongjangboo.member.application.command;

import com.bongjangboo.member.domain.vo.Email;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisterEmailCommand {
    Long memberId;
    Email email;
}

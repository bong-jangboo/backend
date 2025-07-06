package com.example.jangboo.member.application.command;

import com.example.jangboo.member.domain.vo.Email;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisterEmailCommand {
    Long memberId;
    Email email;
}

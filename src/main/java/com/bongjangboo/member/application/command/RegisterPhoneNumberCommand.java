package com.bongjangboo.member.application.command;

import com.bongjangboo.member.domain.vo.PhoneNumber;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisterPhoneNumberCommand {
    Long memberId;
    PhoneNumber phoneNumber;
}

package com.example.jangboo.member.application.command;

import com.example.jangboo.member.domain.vo.PhoneNumber;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateProfileCommand {
    Long memberId;
    String nickName;
    PhoneNumber phoneNumber;
}

package com.example.jangboo.member.application.command;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeactivateMemberCommand {
    Long memberId;
}

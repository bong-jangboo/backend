package com.bongjangboo.member.presentation.dto.request;

import com.bongjangboo.member.domain.MemberStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 상태 변경 요청
@Getter
@NoArgsConstructor
public class UpdateMemberStatusRequest {
    @NotNull(message = "변경할 상태는 필수입니다")
    private MemberStatus status; // ACTIVE, SLEEP, DEACTIVATED
}

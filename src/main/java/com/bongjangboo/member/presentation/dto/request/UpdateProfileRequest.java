package com.bongjangboo.member.presentation.dto.request;

import com.bongjangboo.member.application.command.UpdateProfileCommand;
import com.bongjangboo.member.domain.vo.PhoneNumber;
import jakarta.validation.constraints.*;
import com.bongjangboo.member.domain.vo.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProfileRequest {

    @Size(min = 2, max = 15, message = "닉네임은 2-15자 사이여야 합니다")
    private String nickname;

    @jakarta.validation.constraints.Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @Pattern(regexp = "^010-?\\d{4}-?\\d{4}$", message = "올바른 전화번호 형식이 아닙니다")
    private String phoneNumber;


    // 최소 하나의 필드는 있어야 함
    @AssertTrue(message = "수정할 정보를 최소 하나는 입력해주세요")
    private boolean isAtLeastOneFieldPresent() {
        return nickname != null || email != null || phoneNumber != null;
    }

    public UpdateProfileCommand toCommand(Long memberId) {
        return UpdateProfileCommand.builder()
                .memberId(memberId)
                .nickname(this.nickname)
                .email(this.email != null ? new Email(this.email) : null)
                .phoneNumber(this.phoneNumber != null ? new PhoneNumber(this.phoneNumber) : null)
                .build();
    }

}

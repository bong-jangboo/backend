package com.bongjangboo.member.presentation.dto.request;

import com.bongjangboo.member.domain.SocialProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterMemberRequest {
    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 2, max = 20, message = "이름은 2-20자 사이여야 합니다")
    private String name;

    @NotBlank(message = "닉네임은 필수입니다")
    @Size(min = 2, max = 15, message = "닉네임은 2-15자 사이어야 합니다")
    private String nickName;

    @NotNull(message = "소셜 제공자는 필수입니다")
    private SocialProvider socialProvider;

    @NotNull(message = "소셜 ID는 필수입니다")
    private String socailId;
}

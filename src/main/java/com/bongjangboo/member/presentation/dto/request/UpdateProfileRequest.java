package com.bongjangboo.member.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {
    @NotBlank(message = "닉네임은 필수입니다")
    @Size(min = 2, max = 15, message = "닉네임은 2-15자 사이여야 합니다")
    private String nickName;
}

package com.bongjangboo.member.presentation.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterEmailRequest {
    @NotBlank
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
}

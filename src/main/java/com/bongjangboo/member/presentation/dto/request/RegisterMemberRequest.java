package com.bongjangboo.member.presentation.dto.request;

import com.bongjangboo.member.application.command.RegisterMemberCommand;
import com.bongjangboo.member.domain.SocialProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;


// 클라이언트 요청 바디(JSON)을 역직렬화해서 매핑하는
// 표현 계층의 DTO는 Jackson이 정상적으로 역직렬화를 하기 위해서는
// @Getter, @NoArgsConstructor 필요함
@Getter
@NoArgsConstructor
public class RegisterMemberRequest {

    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 2, max = 20, message = "이름은 2-20자 사이여야 합니다")
    private String name;

    @NotBlank(message = "닉네임은 필수입니다")
    @Size(min = 2, max = 15, message = "닉네임은 2-15자 사이어야 합니다")
    private String nickname;

    @NotNull(message = "소셜 제공자는 필수입니다")
    private SocialProvider socialProvider;

    @NotNull(message = "소셜 ID는 필수입니다")
    private String socialId;


    // Command 변환 메서드 (별도의 Mapper로 분리하거나 Controller에서 직접 변환이 계층분리 측면에서 깔끔하지만 우선은 이렇게)
    public RegisterMemberCommand toCommand() {
        return RegisterMemberCommand.builder()
                .name(this.name)
                .nickname(this.nickname)
                .socialProvider(this.socialProvider)
                .socialId(this.socialId)
                .build();
    }
}

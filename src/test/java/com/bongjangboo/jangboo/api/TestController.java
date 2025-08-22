package com.bongjangboo.jangboo.api;

import com.bongjangboo.member.domain.SocialProvider;
import com.bongjangboo.member.exception.MemberErrorCode;
import com.bongjangboo.shared.exception.BusinessException;
import com.bongjangboo.shared.response.ApiResponse;
import com.bongjangboo.shared.response.ResponseUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
public class TestController {

    @Getter
    public static class DummyRequest {
        @NotBlank(message = "이름은 필수 입력값입니다.")
        private String name;

        @Email
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        private String email;
    }

    @Getter
    @Builder
    public static class DummyResponse {
        private Long id;
        private String name;
        private String email;
        private List<String> roles;
        private LocalDateTime createdAt;
    }

    @Getter
    public static class EnumTestRequest {
        @NotBlank
        private String name;

        @NotBlank
        private String nickname;

        private SocialProvider socialProvider;

        @NotBlank
        private String socialId;
    }



    @GetMapping("/test/business-error")
    public void businessError() {
        throw new BusinessException(MemberErrorCode.DUPLICATE_SOCIAL_ID);
    }

    @GetMapping("/test/system-error")
    public void systemError() {
        throw new RuntimeException("시스템 에러 테스트");
    }

    @GetMapping("/test/dummy-response-success")
    public ResponseEntity<ApiResponse<DummyResponse>> getDummyDetail() {
        DummyResponse dummy = DummyResponse.builder()
                .id(1L)
                .name("홍길동")
                .email("hong@example.com")
                .roles(List.of("USER", "MANAGER"))
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseUtil.success(dummy);
    }

    @PostMapping("/test/validation-error")
    public ResponseEntity<ApiResponse<String>> validationError(@Valid @RequestBody DummyRequest req) {
        return ResponseUtil.success("OK");
    }


    @PostMapping("/test/enum-error")
    public ResponseEntity<ApiResponse<String>> enumError(@Valid @RequestBody EnumTestRequest req) {
        return ResponseUtil.success("OK");
    }


    /**
     * Email_Already_Registered 예외 발생시키는 엔드 포인트
     */
    @GetMapping("/test/custom-business-error")
    public void customBusinessError() {
        throw new BusinessException(MemberErrorCode.EMAIL_ALREADY_REGISTERED);
    }


    /**
     * null 을 응답하는 엔드포인트 (ResponseUtil 사용)
     */
    @GetMapping("/test/success-null")
    public ResponseEntity<ApiResponse<Objects>> successNull() {
        return ResponseUtil.success(null);
    }
}

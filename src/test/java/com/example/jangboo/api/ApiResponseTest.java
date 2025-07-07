package com.example.jangboo.api;

import com.example.jangboo.shared.response.ApiError;
import com.example.jangboo.shared.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiResponseTest {
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 직렬화용

    @Test
    @DisplayName("성공 응답 직렬화: success=true 와 data 필드를 포함해야 한다")
    void 성공_응답_직렬화_테스트() throws Exception {
        // given
        ApiResponse<String> response = ApiResponse.success("ok");

        // when
        String json = objectMapper.writeValueAsString(response);

        // then
        assertThat(json).contains("\"success\":true");     // 성공 여부
        assertThat(json).contains("\"data\":\"ok\"");      // 데이터
    }

    @Test
    @DisplayName("실패 응답 직렬화: success=false 와 error 필드를 포함해야 한다")
    void 실패_응답_직렬화_테스트() throws Exception {
        // given
        var error = ApiError.builder()
                .code("ERROR_CODE")
                .message("에러 발생")
                .build();
        ApiResponse<?> response = ApiResponse.failure(error);

        // when
        String json = objectMapper.writeValueAsString(response);

        // then
        assertThat(json).contains("\"success\":false");    // 실패 여부
        assertThat(json).contains("\"error\"");            // 에러 필드
        assertThat(json).contains("ERROR_CODE");           // 에러 코드
    }
}

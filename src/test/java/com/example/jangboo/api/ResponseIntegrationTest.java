package com.example.jangboo.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = TestController.class)
@AutoConfigureMockMvc(addFilters = false) // 스프링 시큐리티 떄문에 필터 꺼야함
public class ResponseIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    /**
     * BusinessException 발생 시
     * GlobalExceptionHandler 가 BAD_REQUEST(400) 과
     * 표준 JSON 응답을 내려주는지 테스트
     */
    @Test
    @DisplayName("비즈니스 예외 발생 시 400과 표준 JSON을 반환한다")
    void businessExceptionShouldReturnBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/test/business-error"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("DUPLICATE_SOCIAL_ID"));
    }

    /**
     * 예상치 못한 Exception 발생 시
     * GlobalExceptionHandler 가 INTERNAL_SERVER_ERROR(500) 과
     * 표준 JSON 응답을 내려주는지 테스트
     */
    @Test
    @DisplayName("시스템 예외 발생 시 500과 표준 JSON을 반환한다")
    void systemExceptionShouldReturnInternalServerError() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/test/system-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INTERNAL_ERROR"));
    }


    /**
     * ResponseUtil 을 사용하여 성공 응답을 만들었을 때
     * 정상적으로 표준에 맞게 값을 내려주는지 테스트
     */
    @Test
    @DisplayName("ResponseEntity 응답 성공 테스트")
    void bigSuccessApiResponse() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/test/dummy-response-success"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("홍길동"))
                .andExpect(jsonPath("$.data.email").value("hong@example.com"))
                .andExpect(jsonPath("$.data.roles").isArray());
    }

    /**
     * 값 검증 실패시 ExceptionHandler 가 잘 잡는지 테스트
     */
    @Test
    @DisplayName("값 검증 실패시 GlobalExceptionHandler 에서 예외를 잡어서 처리하는지 테스트")
    void 값_검증_실패_테스트() throws Exception {
        mockMvc.perform(post("/test/validation-error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))  // name 필드 누락 => validation 실패
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.error.fieldErrors[*].field").value(org.hamcrest.Matchers.hasItems("email", "name")))
                .andExpect(jsonPath("$.error.fieldErrors[0].message").exists());

    }


    /**
     * 필드 validation 다중 실패 테스트
     */
    @Test
    @DisplayName("여러 필드 값 검증 실패 시 GlobalExceptionHandler 가 잘 처리하는지 테스트")
    void 여러_필드_검증_실패_테스트() throws Exception {
        // name, email 두 필드 모두 빠뜨림
        mockMvc.perform(post("/test/validation-error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.error.fieldErrors").isArray())
                .andExpect(jsonPath("$.error.fieldErrors.length()").value(2)); // 현재 DummyRequest 에는 name 하나만 검증중
    }



    /**
     * 커스텀 에러 잡는지 확인
     */
    @Test
    @DisplayName("커스텀 예외 발생시 에러를 정상적으로 잡아서 응답하는지 테스트")
    public void 비즈니스_예외_테스트() throws Exception {
        mockMvc.perform(get("/test/custom-business-error"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("EMAIL_ALREADY_REGISTERED"))
                .andExpect(jsonPath("$.error.message").value("이미 이메일이 등록된 회원입니다."))
                .andExpect(jsonPath("$.success").value(false));

    }

    /**
     * JsonInclude.NON_NULL 옵션이 잘 동작하는지 확인
     * null 이 들어간 필드는 json 에서 무시하고 출력
     */
    @Test
    @DisplayName("null 필드는 json에서 무시하고 응답하는지 테스트")
    public void null_필드_무시() throws Exception {
        mockMvc.perform(get("/test/success-null"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    /**
     * enum 값 파싱 실패 시 GlobalExceptionHandler 가 잘 처리하는지 테스트
     */
    @Test
    @DisplayName("Enum 타입 파싱 실패 시 GlobalExceptionHandler 가 잘 처리하는지 테스트 (JSON 파싱 실패 시나리오)")
    void enum_타입_파싱_실패_테스트() throws Exception {
        // socialProvider 필드에 잘못된 enum 값
        String invalidJson = """
        {
            "name": "채원",
            "nickname": "백일평냉",
            "socialProvider": "FACEBOKK", 
            "socialId": "fb-123"
        }
        """;

        mockMvc.perform(post("/test/enum-error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("JSON_PARSE_ERROR"))
                .andExpect(jsonPath("$.error.message").exists());
    }


    /**
     * 존재하지 않는 URL 요청 시 GlobalExceptionHandler 가 잘 처리하는지 테스트
     */
    @Test
    @DisplayName("존재하지 않는 URL 요청 시 404 응답을 표준 JSON 으로 반환하는지 테스트")
    void 존재하지_않는_URL_요청_테스트() throws Exception {
        mockMvc.perform(get("/test/not-exists"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.error.message").exists());
    }


    /**
     * 잘못된 HTTP 메서드 요청 시 GlobalExceptionHandler 가 405를 표준 JSON 으로 반환하는지 테스트
     */
    @Test
    @DisplayName("잘못된 HTTP 메서드 요청 시 405 응답을 표준 JSON 으로 반환하는지 테스트")
    void 잘못된_HTTP_메서드_요청_테스트() throws Exception {
        mockMvc.perform(post("/test/dummy-response-success")) // GET만 허용된 엔드포인트에 POST 시도
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.error.message").exists());
    }





}

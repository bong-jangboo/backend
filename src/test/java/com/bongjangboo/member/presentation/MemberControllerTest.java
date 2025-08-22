package com.bongjangboo.member.presentation;

import com.bongjangboo.member.application.service.MemberApplicationService;
import com.bongjangboo.member.domain.Member;
import com.bongjangboo.member.domain.MemberStatus;
import com.bongjangboo.member.domain.SocialProvider;
import com.bongjangboo.member.domain.vo.Email;
import com.bongjangboo.member.domain.vo.PhoneNumber;
import com.bongjangboo.member.exception.MemberErrorCode;
import com.bongjangboo.shared.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MemberController 웹 계층 테스트
 * 
 * 테스트 대상:
 * - HTTP 요청/응답 처리
 * - Validation 검증
 * - 예외 처리 및 에러 응답
 * - API 스펙 준수
 * 
 * @WebMvcTest를 사용하여 웹 계층만 테스트
 */
@WebMvcTest(MemberController.class)
@DisplayName("MemberController 웹 계층 테스트")
@WithMockUser
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberApplicationService memberApplicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .defaultRequest(get("/").with(csrf()))
                .build();
    }

    private Member createTestMember() {
        return Member.builder()
                .id(1L)
                .name("홍길동")
                .nickname("길동이")
                .email(new Email("test@example.com"))
                .phoneNumber(new PhoneNumber("010-1234-5678"))
                .socialProvider(SocialProvider.KAKAO)
                .socialId("kakao123")
                .status(MemberStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("회원 등록 API 테스트")
    class RegisterMemberApiTest {

        @Test
        @DisplayName("성공: 유효한 요청으로 회원을 등록한다")
        void registerMember_ValidRequest_Success() throws Exception {
            // given
            Member savedMember = createTestMember();
            given(memberApplicationService.registerMember(any())).willReturn(savedMember);

            String requestBody = """
                    {
                        "name": "홍길동",
                        "nickname": "길동이",
                        "socialProvider": "KAKAO",
                        "socialId": "kakao123"
                    }
                    """;

            // when & then
            mockMvc.perform(post("/api/v1/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.name").value("홍길동"))
                    .andExpect(jsonPath("$.data.nickname").value("길동이"))
                    .andExpect(jsonPath("$.data.socialProvider").value("KAKAO"))
                    .andExpect(jsonPath("$.data.status").value("ACTIVE"));
        }

        @Test
        @DisplayName("실패: 필수 필드 누락 시 400 에러를 반환한다")
        void registerMember_MissingRequiredFields_Returns400() throws Exception {
            // given
            String requestBody = """
                    {
                        "nickname": "길동이"
                    }
                    """;

            // when & then
            mockMvc.perform(post("/api/v1/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("실패: 닉네임 길이 제한 위반 시 400 에러를 반환한다")
        void registerMember_InvalidNicknameLength_Returns400() throws Exception {
            // given
            String requestBody = """
                    {
                        "name": "홍길동",
                        "nickname": "일이삼사오육칠팔구십일이삼사오육칠팔구십일",
                        "socialProvider": "KAKAO",
                        "socialId": "kakao123"
                    }
                    """;

            // when & then
            mockMvc.perform(post("/api/v1/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("실패: 중복된 소셜 계정으로 등록 시 비즈니스 예외를 반환한다")
        void registerMember_DuplicateSocialId_ReturnsBusinessException() throws Exception {
            // given
            given(memberApplicationService.registerMember(any()))
                    .willThrow(new BusinessException(MemberErrorCode.DUPLICATE_SOCIAL_ID));

            String requestBody = """
                    {
                        "name": "홍길동",
                        "nickname": "길동이",
                        "socialProvider": "KAKAO",
                        "socialId": "kakao123"
                    }
                    """;

            // when & then
            mockMvc.perform(post("/api/v1/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.error.code").value(MemberErrorCode.DUPLICATE_SOCIAL_ID.getCode()));
        }
    }

    @Nested
    @DisplayName("회원 프로필 조회 API 테스트")
    class GetMemberProfileApiTest {

        @Test
        @DisplayName("성공: 존재하는 회원의 프로필을 조회한다")
        void getMemberProfile_ExistingMember_Success() throws Exception {
            // given
            Member member = createTestMember();
            given(memberApplicationService.getMemberProfile(1L)).willReturn(member);

            // when & then
            mockMvc.perform(get("/api/v1/members/profile/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.name").value("홍길동"))
                    .andExpect(jsonPath("$.data.nickname").value("길동이"))
                    .andExpect(jsonPath("$.data.email").value("test@example.com"))
                    .andExpect(jsonPath("$.data.phoneNumber").value("01012345678"));
        }

        @Test
        @DisplayName("실패: 존재하지 않는 회원 조회 시 404 에러를 반환한다")
        void getMemberProfile_NotExistingMember_Returns404() throws Exception {
            // given
            given(memberApplicationService.getMemberProfile(999L))
                    .willThrow(new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

            // when & then
            mockMvc.perform(get("/api/v1/members/profile/999"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.error.code").value(MemberErrorCode.MEMBER_NOT_FOUND.getCode()));
        }

        @Test
        @DisplayName("실패: 잘못된 ID 형식으로 요청 시 400 에러를 반환한다")
        void getMemberProfile_InvalidIdFormat_Returns400() throws Exception {
            // when & then
            mockMvc.perform(get("/api/v1/members/profile/invalid"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("회원 프로필 업데이트 API 테스트")
    class UpdateMemberProfileApiTest {

        @Test
        @DisplayName("성공: 닉네임만 업데이트한다")
        void updateMemberProfile_NicknameOnly_Success() throws Exception {
            // given
            Member updatedMember = createTestMember();
            given(memberApplicationService.updateProfile(any())).willReturn(updatedMember);

            String requestBody = """
                    {
                        "nickname": "새로운닉네임"
                    }
                    """;

            // when & then
            mockMvc.perform(patch("/api/v1/members/profile/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.id").value(1));
        }

        @Test
        @DisplayName("성공: 이메일과 전화번호를 동시에 업데이트한다")
        void updateMemberProfile_EmailAndPhone_Success() throws Exception {
            // given
            Member updatedMember = createTestMember();
            given(memberApplicationService.updateProfile(any())).willReturn(updatedMember);

            String requestBody = """
                    {
                        "email": "newemail@example.com",
                        "phoneNumber": "010-9876-5432"
                    }
                    """;

            // when & then
            mockMvc.perform(patch("/api/v1/members/profile/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("실패: 모든 필드가 null인 경우 400 에러를 반환한다")
        void updateMemberProfile_AllFieldsNull_Returns400() throws Exception {
            // given
            String requestBody = "{}";

            // when & then
            mockMvc.perform(patch("/api/v1/members/profile/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }


        // 중복되는 테스트 구조 : 여기서는 입력 형식이 맞지 않을 때 400 에러 반환 -> @ParameterizedTest로 리팩 가능
        @ParameterizedTest
        @DisplayName("실패: 잘못된 입력 값으로 요청 시 400 에러를 반환한다")
        @MethodSource("invalidProfileRequests")
        void updateMemberProfile_InvalidInput_Returns400(String requestBody) throws Exception {
            mockMvc.perform(patch("/api/v1/members/profile/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isBadRequest()); // 400 반환
        }

        private static Stream<String> invalidProfileRequests() {
            return Stream.of(
                    """
                            { "email": "invalid-email-format" }
                            """,
                    """
                            { "phoneNumber": "invalid-phone" }
                            """,
                    """
                            { "nickname": "일이삼사오육칠팔구십일이삼사오육칠팔구십일" }
                            """,
                    """
                            {  }
                            """
            );
        }

        @Test
        @DisplayName("실패: 이메일 중복 시 비즈니스 예외를 반환한다")
        void updateMemberProfile_DuplicateEmail_ReturnsBusinessException() throws Exception {
            // given
            given(memberApplicationService.updateProfile(any()))
                    .willThrow(new BusinessException(MemberErrorCode.EMAIL_ALREADY_IN_USE));

            String requestBody = """
                    {
                        "email": "duplicate@example.com"
                    }
                    """;

            // when & then
            mockMvc.perform(patch("/api/v1/members/profile/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.error.code").value(MemberErrorCode.EMAIL_ALREADY_IN_USE.getCode()));
        }

        @Test
        @DisplayName("실패: 전화번호 중복 시 비즈니스 예외를 반환한다")
        void updateMemberProfile_DuplicatePhone_ReturnsBusinessException() throws Exception {
            // given
            given(memberApplicationService.updateProfile(any()))
                    .willThrow(new BusinessException(MemberErrorCode.PHONE_ALREADY_IN_USE));

            String requestBody = """
                    {
                        "phoneNumber": "010-1111-2222"
                    }
                    """;

            // when & then
            mockMvc.perform(patch("/api/v1/members/profile/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.error.code").value(MemberErrorCode.PHONE_ALREADY_IN_USE.getCode()));
        }


        @Nested
        @DisplayName("Content-Type 및 HTTP 메서드 테스트")
        class HttpMethodAndContentTypeTest {

            @Test
            @DisplayName("실패: 잘못된 Content-Type으로 요청 시 415 에러를 반환한다")
            void postRequest_WrongContentType_Returns415() throws Exception {
                // given
                String requestBody = """
                        {
                            "name": "홍길동",
                            "nickname": "길동이",
                            "socialProvider": "KAKAO",
                            "socialId": "kakao123"
                        }
                        """;

                // when & then
                mockMvc.perform(post("/api/v1/members")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content(requestBody))
                        .andDo(print())
                        .andExpect(status().isUnsupportedMediaType());
            }

            @Test
            @DisplayName("실패: 지원하지 않는 HTTP 메서드로 요청 시 405 에러를 반환한다")
            void wrongHttpMethod_Returns405() throws Exception {
                // when & then
                mockMvc.perform(put("/api/v1/members"))
                        .andDo(print())
                        .andExpect(status().isMethodNotAllowed());
            }
        }
    }
}
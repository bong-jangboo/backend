package com.example.jangboo.RestDocsTest;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.example.legacy.auth.JwtTokenProvider;
import com.example.legacy.auth.controller.dto.Info.CurrentUserInfo;
import com.example.legacy.auth.controller.dto.request.LoginRequest;
import com.example.legacy.auth.service.AuthService;
import com.example.legacy.role.domain.Role;
import com.example.legacy.role.domain.RoleRepository;
import com.example.legacy.role.domain.RoleType;
import com.example.legacy.users.domain.User;
import com.example.legacy.users.domain.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class AuthRestDocsTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthService authService;

	@SpyBean
	private JwtTokenProvider jwtTokenProvider;

	private User testUser;

	private Role testRole;

	private String mockToken;

	private ObjectMapper objectMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() throws Exception {
		objectMapper = new ObjectMapper();

		// 사용자 생성 및 토큰 처리 모킹
		mockToken = "jwtToken";
		testUser = userRepository.save(
			User.builder()
				.name("강지원")
				.number("202011288")
				.loginId("rkdwldnjs30")
				.password(passwordEncoder.encode("1234"))
				.deptId(1L).build()
		);
		testRole = roleRepository.save(
			Role.builder()
				.studentId(testUser.getId())
				.role(RoleType.STUDENT)
				.build()
		);
		doReturn(new CurrentUserInfo(testUser.getId(), testUser.getDeptId())).when(jwtTokenProvider).extractUserInfo(mockToken);
	}

	@Test
	@DisplayName("API - 로그인 성공시")
	void loginTest() throws Exception {
		//given
		LoginRequest request = new LoginRequest("rkdwldnjs30","1234");

		//when & then
		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(document("api-auth-test/login-user",
				requestFields(
					fieldWithPath("loginId").description("유저의 소속대학명"),
					fieldWithPath("password").description("유저의 소속 단과대학명")
				),
				responseFields(
					fieldWithPath("code").description("상태코드"),
					fieldWithPath("message").description("메시지"),
					fieldWithPath("data").description("jwt 토큰 정보"),
					fieldWithPath("data.grantType").description("권한 코두 부여 유형"),
					fieldWithPath("data.accessToken").description("유저의 accessToken"),
					fieldWithPath("data.refreshToken").description("유저의 refreshToken")
				)
			));
	}
}

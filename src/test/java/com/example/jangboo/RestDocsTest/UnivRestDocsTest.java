package com.example.jangboo.RestDocsTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;

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
import com.example.legacy.auth.service.AuthService;
import com.example.legacy.role.domain.Role;
import com.example.legacy.role.domain.RoleRepository;
import com.example.legacy.role.domain.RoleType;
import com.example.legacy.univ.controller.dto.request.RegisterRequest;
import com.example.legacy.univ.domain.Univ;
import com.example.legacy.univ.domain.UnivRepository;
import com.example.legacy.users.domain.User;
import com.example.legacy.users.domain.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class UnivRestDocsTest {
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

	private User testPresident;

	private Univ testCollege;

	private Univ testDeparture;

	private Role testRole;

	private Role testPresidentRole;

	private String mockToken;

	private ObjectMapper objectMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UnivRepository univRepository;

	private Principal testPrincipal;

	@BeforeEach
	void setUp() throws Exception {
		objectMapper = new ObjectMapper();

		// 사용자 생성 및 토큰 처리 모킹
		mockToken = "jwtToken";

		testCollege = univRepository.save(
			Univ.builder()
				.name("정보융합대학")
				.orgType("COLLEGE")
				.parent(null)
				.build()
		);
		testDeparture = univRepository.save(
			Univ.builder()
				.name("컴퓨터공학부")
				.orgType("DEPARTURE")
				.parent(testCollege)
				.build()
		);
		testUser = userRepository.save(
			User.builder()
				.name("강지원")
				.number("202011288")
				.loginId("rkdwldnjs30")
				.password(passwordEncoder.encode("1234"))
				.deptId(testDeparture.getId()).build()
		);

		testPresident = userRepository.save(
			User.builder()
				.name("정채원")
				.number("201900000")
				.loginId("1234")
				.password(passwordEncoder.encode("1234"))
				.deptId(testDeparture.getId()).build()
		);

		testRole = roleRepository.save(
			Role.builder()
				.studentId(testUser.getId())
				.role(RoleType.STUDENT)
				.build()
		);

		testPresidentRole = roleRepository.save(
			Role.builder()
				.studentId(testPresident.getId())
				.role(RoleType.PRESIDENT)
				.build()
		);


		doReturn(new CurrentUserInfo(testPresident.getId(), testPresident.getDeptId())).when(jwtTokenProvider)
			.extractUserInfo(mockToken);
	}

	@Test
	@DisplayName("API - 회원가입")
	void signupTest() throws Exception {
		//given
		RegisterRequest request = new RegisterRequest("부경대학교", "정보융합대학", "컴퓨터공학부", "강지원", "202011288", "1234",
			"jiwon1923");

		//when & then
		mockMvc.perform(post("/api/univ/register/{role}?parentId=" + testDeparture.getId(), "STUDENT")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(document("api-univ-test/register-user",
				pathParameters(
					parameterWithName("role").description("가입할 유저의 역할")
				),
				queryParameters(
					parameterWithName("parentId").description("가입할 유저의 상위 조직 id(단과대 = null, 회장 = 단과대Id , 학생 = 학과Id )")
				),
				requestFields(
					fieldWithPath("univ").description("유저의 소속대학명"),
					fieldWithPath("college").description("유저의 소속 단과대학명"),
					fieldWithPath("dept").description("유저의 소속 학과"),
					fieldWithPath("name").description("유저의 이름"),
					fieldWithPath("number").description("유저의 학번"),
					fieldWithPath("loginId").description("유저의 아이디"),
					fieldWithPath("password").description("유저의 비밀번호")
				)
			));
	}
}

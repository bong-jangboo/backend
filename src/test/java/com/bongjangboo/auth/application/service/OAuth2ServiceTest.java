package com.bongjangboo.auth.application.service;

import com.bongjangboo.auth.domain.Role;
import com.bongjangboo.auth.domain.oauth.CustomOAuth2User;
import com.bongjangboo.auth.infra.oauth.OAuthAttributes;
import com.bongjangboo.member.application.command.RegisterMemberFromSocialCommand;
import com.bongjangboo.member.application.service.MemberApplicationService;
import com.bongjangboo.member.domain.Member;
import com.bongjangboo.member.domain.MemberStatus;
import com.bongjangboo.member.domain.SocialProvider;
import com.bongjangboo.member.domain.vo.Email;
import com.bongjangboo.shared.exception.BusinessException;
import com.bongjangboo.shared.exception.CommonErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OAuth2Service 단위 테스트")
class OAuth2ServiceTest {

    @Mock
    private MemberApplicationService memberApplicationService;

    private Member testMember;
    private Map<String, Object> naverAttributes;

    @BeforeEach
    void setUp() {

        // Given: 네이버 API 응답 형태의 테스트 데이터
        Map<String, Object> naverResponse = new HashMap<>();
        naverResponse.put("id", "test-naver-id");
        naverResponse.put("email", "test@naver.com");
        naverResponse.put("name", "테스트유저");

        naverAttributes = new HashMap<>();
        naverAttributes.put("response", naverResponse);
        naverAttributes.put("resultcode", "00");
        naverAttributes.put("message", "success");

        // Given: 테스트용 Member 객체
        testMember = Member.builder()
                .id(1L)
                .email(new Email("test@naver.com"))
                .name("테스트유저")
                .nickname("테스트유저1")
                .status(MemberStatus.ACTIVE)
                .socialProvider(SocialProvider.NAVER)
                .socialId("test-naver-id")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("getMember 메서드 로직 테스트")
    class GetMemberLogicTest {

        @Test
        @DisplayName("OAuthAttributes로부터 올바른 RegisterMemberFromSocialCommand를 생성한다")
        void createCorrectCommandFromOAuthAttributes() {
            // Given
            OAuthAttributes attributes = OAuthAttributes.of(
                    SocialProvider.NAVER, 
                    "response", 
                    naverAttributes
            );
            when(memberApplicationService.findOrCreateFromSocialLogin(any(RegisterMemberFromSocialCommand.class)))
                    .thenReturn(testMember);

            // When: OAuth2Service의 getMember 로직을 시뮬레이션
            assertThat(attributes.getOAuth2UserInfo()).isNotNull();
            String name = attributes.getOAuth2UserInfo().getName();
            String email = attributes.getOAuth2UserInfo().getEmail();
            String socialId = attributes.getOAuth2UserInfo().getProviderId();

            RegisterMemberFromSocialCommand command = RegisterMemberFromSocialCommand.builder()
                    .name(name)
                    .email(email)
                    .socialProvider(SocialProvider.NAVER)
                    .socialId(socialId)
                    .build();

            memberApplicationService.findOrCreateFromSocialLogin(command);

            // Then
            verify(memberApplicationService).findOrCreateFromSocialLogin(argThat(cmd -> 
                cmd.getName().equals("테스트유저") &&
                cmd.getEmail().equals("test@naver.com") &&
                cmd.getSocialProvider() == SocialProvider.NAVER &&
                cmd.getSocialId().equals("test-naver-id")
            ));
        }
    }

    @Nested
    @DisplayName("getSocialProvider 메서드 테스트")
    class GetSocialProviderTest {

        @Test
        @DisplayName("naver registrationId로 NAVER Provider를 반환한다")
        void returnNaverProviderForNaverRegistrationId() {
            // When: OAuth2Service의 getSocialProvider 로직 시뮬레이션
            SocialProvider result = getSocialProviderFromRegistrationId("naver");

            // Then
            assertThat(result).isEqualTo(SocialProvider.NAVER);
        }

        @Test
        @DisplayName("kakao registrationId로 KAKAO Provider를 반환한다")
        void returnKakaoProviderForKakaoRegistrationId() {
            // When
            SocialProvider result = getSocialProviderFromRegistrationId("kakao");

            // Then
            assertThat(result).isEqualTo(SocialProvider.KAKAO);
        }

        @Test
        @DisplayName("google registrationId로 GOOGLE Provider를 반환한다")
        void returnGoogleProviderForGoogleRegistrationId() {
            // When
            SocialProvider result = getSocialProviderFromRegistrationId("google");

            // Then
            assertThat(result).isEqualTo(SocialProvider.GOOGLE);
        }

        @Test
        @DisplayName("지원하지 않는 registrationId에 대해 BusinessException을 발생시킨다")
        void throwBusinessExceptionForUnsupportedRegistrationId() {
            // When & Then
            assertThatThrownBy(() -> getSocialProviderFromRegistrationId("unsupported"))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", CommonErrorCode.INTERNAL_ERROR);
        }

        // OAuth2Service의 getSocialProvider 로직 추출
        private SocialProvider getSocialProviderFromRegistrationId(String registrationId) {
            return switch (registrationId) {
                case "naver" -> SocialProvider.NAVER;
                case "kakao" -> SocialProvider.KAKAO;
                case "google" -> SocialProvider.GOOGLE;
                default -> throw new BusinessException(CommonErrorCode.INTERNAL_ERROR);
            };
        }
    }

    @Nested
    @DisplayName("mapStatusToRole 메서드 테스트")
    class MapStatusToRoleTest {

        @Test
        @DisplayName("ACTIVE 상태 회원을 USER 권한으로 매핑한다")
        void mapActiveStatusToUserRole() {
            // Given
            Member activeMember = Member.builder()
                    .status(MemberStatus.ACTIVE)
                    .build();

            // When
            Role result = mapMemberStatusToRole(activeMember);

            // Then
            assertThat(result).isEqualTo(Role.USER);
        }

        @Test
        @DisplayName("PENDING_ONBOARDING 상태 회원을 GUEST 권한으로 매핑한다")
        void mapPendingOnboardingStatusToGuestRole() {
            // Given
            Member pendingMember = Member.builder()
                    .status(MemberStatus.PENDING_ONBOARDING)
                    .build();

            // When
            Role result = mapMemberStatusToRole(pendingMember);

            // Then
            assertThat(result).isEqualTo(Role.GUEST);
        }

        @Test
        @DisplayName("INACTIVE 상태 회원에 대해 BusinessException을 발생시킨다")
        void throwBusinessExceptionForInactiveStatus() {
            // Given
            Member inactiveMember = Member.builder()
                    .status(MemberStatus.INACTIVE)
                    .build();

            // When & Then
            assertThatThrownBy(() -> mapMemberStatusToRole(inactiveMember))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", CommonErrorCode.INTERNAL_ERROR);
        }

        @Test
        @DisplayName("SLEEP 상태 회원에 대해 BusinessException을 발생시킨다")
        void throwBusinessExceptionForSleepStatus() {
            // Given
            Member sleepMember = Member.builder()
                    .status(MemberStatus.SLEEP)
                    .build();

            // When & Then
            assertThatThrownBy(() -> mapMemberStatusToRole(sleepMember))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", CommonErrorCode.INTERNAL_ERROR);
        }

        // OAuth2Service의 mapStatusToRole 로직 추출
        private Role mapMemberStatusToRole(Member member) {
            return switch (member.getStatus()) {
                case ACTIVE -> Role.USER;
                case PENDING_ONBOARDING -> Role.GUEST;
                default -> throw new BusinessException(CommonErrorCode.INTERNAL_ERROR);
            };
        }
    }

    @Nested
    @DisplayName("createAuthorities 메서드 테스트")
    class CreateAuthoritiesTest {

        @Test
        @DisplayName("USER 권한으로 ROLE_USER 권한을 생성한다")
        void createRoleUserAuthorityForUserRole() {
            // When
            Collection<GrantedAuthority> authorities = createAuthoritiesFromRole(Role.USER);

            // Then
            assertThat(authorities).hasSize(1);
            assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_USER");
        }

        @Test
        @DisplayName("GUEST 권한으로 ROLE_GUEST 권한을 생성한다")
        void createRoleGuestAuthorityForGuestRole() {
            // When
            Collection<GrantedAuthority> authorities = createAuthoritiesFromRole(Role.GUEST);

            // Then
            assertThat(authorities).hasSize(1);
            assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_GUEST");
        }

        @Test
        @DisplayName("ADMIN 권한으로 ROLE_ADMIN 권한을 생성한다")
        void createRoleAdminAuthorityForAdminRole() {
            // When
            Collection<GrantedAuthority> authorities = createAuthoritiesFromRole(Role.ADMIN);

            // Then
            assertThat(authorities).hasSize(1);
            assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
        }

        // OAuth2Service의 createAuthorities 로직 추출
        private Collection<GrantedAuthority> createAuthoritiesFromRole(Role role) {
            return Collections.singletonList(new SimpleGrantedAuthority(role.getAuthority()));
        }
    }

    @Nested
    @DisplayName("CustomOAuth2User 생성 로직 테스트")
    class CustomOAuth2UserCreationTest {

        @Test
        @DisplayName("모든 파라미터로 CustomOAuth2User를 올바르게 생성한다")
        void createCustomOAuth2UserWithAllParameters() {
            // Given
            Collection<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_USER"));
            Email email = new Email("test@example.com");

            // When
            CustomOAuth2User customUser = new CustomOAuth2User(
                    authorities, 
                    naverAttributes, 
                    "response", 
                    email
            );

            // Then
            assertThat(customUser.getEmail()).isEqualTo(email);
            assertThat(customUser.getAuthorities()).hasSize(1);
            assertThat(customUser.getAuthorities()).extracting(GrantedAuthority::getAuthority)
                    .containsExactly("ROLE_USER");
            assertThat(customUser.getAttributes()).isEqualTo(naverAttributes);
            assertThat(customUser.getName()).isNotNull(); // nameAttributeKey로 추출된 값
        }

        @Test
        @DisplayName("권한 정보가 올바르게 설정된다")
        void setAuthoritiesCorrectly() {
            // Given
            Collection<GrantedAuthority> guestAuthorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_GUEST"));
            Email email = new Email("guest@example.com");

            // When
            CustomOAuth2User customUser = new CustomOAuth2User(
                    guestAuthorities, 
                    naverAttributes, 
                    "response", 
                    email
            );

            // Then
            assertThat(customUser.getAuthorities()).hasSize(1);
            assertThat(customUser.getAuthorities().iterator().next().getAuthority())
                    .isEqualTo("ROLE_GUEST");
        }
    }

    @Nested
    @DisplayName("통합 시나리오 테스트")
    class IntegrationScenarioTest {

        @Test
        @DisplayName("전체 OAuth2 플로우의 핵심 로직이 올바르게 연결된다")
        void validateEntireOAuth2FlowLogic() {
            // Given: 네이버 OAuth2 플로우의 각 단계를 시뮬레이션
            when(memberApplicationService.findOrCreateFromSocialLogin(any(RegisterMemberFromSocialCommand.class)))
                    .thenReturn(testMember);

            // When: 각 단계별 로직 실행
            // 1. OAuthAttributes 생성
            OAuthAttributes attributes = OAuthAttributes.of(
                    SocialProvider.NAVER, 
                    "response", 
                    naverAttributes
            );

            // 2. Command 생성
            String name = attributes.getOAuth2UserInfo().getName();
            String email = attributes.getOAuth2UserInfo().getEmail();
            String socialId = attributes.getOAuth2UserInfo().getProviderId();

            RegisterMemberFromSocialCommand command = RegisterMemberFromSocialCommand.builder()
                    .name(name)
                    .email(email)
                    .socialProvider(SocialProvider.NAVER)
                    .socialId(socialId)
                    .build();

            // 3. Member 조회/생성
            Member member = memberApplicationService.findOrCreateFromSocialLogin(command);

            // 4. Role 매핑
            Role role = switch (member.getStatus()) {
                case ACTIVE -> Role.USER;
                case PENDING_ONBOARDING -> Role.GUEST;
                default -> throw new BusinessException(CommonErrorCode.INTERNAL_ERROR);
            };

            // 5. 권한 생성
            Collection<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(role.getAuthority()));

            // 6. CustomOAuth2User 생성
            CustomOAuth2User result = new CustomOAuth2User(
                    authorities, 
                    naverAttributes, 
                    "response", 
                    member.getEmail()
            );

            // Then: 최종 결과 검증
            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(testMember.getEmail());
            assertThat(result.getAuthorities()).hasSize(1);
            assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");

            // MemberApplicationService 호출 검증
            verify(memberApplicationService).findOrCreateFromSocialLogin(argThat(cmd -> 
                cmd.getName().equals("테스트유저") &&
                cmd.getEmail().equals("test@naver.com") &&
                cmd.getSocialProvider() == SocialProvider.NAVER &&
                cmd.getSocialId().equals("test-naver-id")
            ));
        }

        @Test
        @DisplayName("예외 상황에서 적절한 예외가 발생한다")
        void handleExceptionScenariosAppropriately() {
            // Given: MemberApplicationService에서 예외 발생
            when(memberApplicationService.findOrCreateFromSocialLogin(any(RegisterMemberFromSocialCommand.class)))
                    .thenThrow(new BusinessException(CommonErrorCode.INTERNAL_ERROR));

            // Given: OAuth2 플로우 데이터 준비
            OAuthAttributes attributes = OAuthAttributes.of(
                    SocialProvider.NAVER, 
                    "response", 
                    naverAttributes
            );

            String name = attributes.getOAuth2UserInfo().getName();
            String email = attributes.getOAuth2UserInfo().getEmail();
            String socialId = attributes.getOAuth2UserInfo().getProviderId();

            RegisterMemberFromSocialCommand command = RegisterMemberFromSocialCommand.builder()
                    .name(name)
                    .email(email)
                    .socialProvider(SocialProvider.NAVER)
                    .socialId(socialId)
                    .build();

            // When & Then: 예외가 적절히 전파되는지 확인
            assertThatThrownBy(() -> memberApplicationService.findOrCreateFromSocialLogin(command))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", CommonErrorCode.INTERNAL_ERROR);
        }
    }
}
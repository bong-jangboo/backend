package com.bongjangboo.member.application.service;

import com.bongjangboo.member.application.command.*;
import com.bongjangboo.member.domain.Member;
import com.bongjangboo.member.domain.MemberRepository;
import com.bongjangboo.member.domain.MemberStatus;
import com.bongjangboo.shared.identity.SocialProvider;
import com.bongjangboo.member.domain.vo.Email;
import com.bongjangboo.member.domain.vo.PhoneNumber;
import com.bongjangboo.member.exception.MemberErrorCode;
import com.bongjangboo.shared.event.DomainEventPublisher;
import com.bongjangboo.shared.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

/**
 * MemberApplicationService 단위 테스트
 * 
 * 테스트 대상:
 * - 비즈니스 로직 검증
 * - 의존성과의 상호작용 검증
 * - 예외 상황 처리
 * - 도메인 이벤트 발행 검증
 */
@DisplayName("MemberApplicationService 테스트")
@ExtendWith(MockitoExtension.class)
class MemberApplicationServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private MemberApplicationService memberApplicationService;

    private Member createTestMember() {
        return Member.builder()
                .id(1L)
                .name("홍길동")
                .nickname("길동이")
                .email(new Email("test@example.com"))  // 이메일이 이미 등록된 상태
                .phoneNumber(new PhoneNumber("010-1234-5678"))  // 전화번호가 이미 등록된 상태
                .socialProvider(SocialProvider.KAKAO)
                .socialId("kakao123")
                .status(MemberStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    private Member createTestMemberWithoutEmail() {
        return Member.builder()
                .id(1L)
                .name("홍길동")
                .nickname("길동이")
                // email은 null
                .socialProvider(SocialProvider.KAKAO)
                .socialId("kakao123")
                .status(MemberStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("회원 등록 테스트")
    class RegisterMemberTest {

        @Test
        @DisplayName("성공: 새로운 회원을 등록한다")
        void registerMember_NewMember_Success() {
            // given
            RegisterMemberCommand command = RegisterMemberCommand.builder()
                    .name("홍길동")
                    .nickname("길동이")
                    .socialProvider(SocialProvider.KAKAO)
                    .socialId("kakao123")
                    .build();

            Member savedMember = createTestMember();

            given(memberRepository.existsBySocial(SocialProvider.KAKAO, "kakao123")).willReturn(false);
            given(memberRepository.save(any(Member.class))).willReturn(savedMember);

            // when
            Member result = memberApplicationService.registerMember(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("홍길동");
            assertThat(result.getNickname()).isEqualTo("길동이");
            assertThat(result.getSocialProvider()).isEqualTo(SocialProvider.KAKAO);
            assertThat(result.getSocialId()).isEqualTo("kakao123");

            // 저장소 호출 검증
            then(memberRepository).should().existsBySocial(SocialProvider.KAKAO, "kakao123");
            then(memberRepository).should().save(any(Member.class));

            // 도메인 이벤트 발행 검증
            then(eventPublisher).should().publish(any());
        }

        @Test
        @DisplayName("실패: 이미 존재하는 소셜 계정으로 등록 시 예외를 발생시킨다")
        void registerMember_DuplicateSocialId_ThrowsException() {
            // given
            RegisterMemberCommand command = RegisterMemberCommand.builder()
                    .name("홍길동")
                    .nickname("길동이")
                    .socialProvider(SocialProvider.KAKAO)
                    .socialId("kakao123")
                    .build();

            given(memberRepository.existsBySocial(SocialProvider.KAKAO, "kakao123")).willReturn(true);

            // when & then
            assertThatThrownBy(() -> memberApplicationService.registerMember(command))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(MemberErrorCode.DUPLICATE_SOCIAL_ID.getMessage());

            // 저장은 호출되지 않음
            then(memberRepository).should(never()).save(any(Member.class));
        }
    }

    @Nested
    @DisplayName("프로필 업데이트 테스트")
    class UpdateProfileTest {

        private Member testMember;

        @BeforeEach
        void setUp() {
            testMember = createTestMember();
        }

        @Test
        @DisplayName("성공: 닉네임만 업데이트한다")
        void updateProfile_NicknameOnly_Success() {
            // given
            UpdateProfileCommand command = UpdateProfileCommand.builder()
                    .memberId(1L)
                    .nickname("새닉네임")
                    .build();

            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(memberRepository.save(any(Member.class))).willReturn(testMember);

            // when
            Member result = memberApplicationService.updateProfile(command);

            // then
            assertThat(result).isNotNull();
            then(memberRepository).should().findById(1L);
            then(memberRepository).should().save(testMember);
            assertThat(result.getNickname()).isEqualTo("새닉네임");
        }

        @Test
        @DisplayName("성공: 이메일을 업데이트한다 (중복 없음)")
        void updateProfile_EmailUpdate_Success() {
            // given
            Email newEmail = new Email("new@example.com");
            UpdateProfileCommand command = UpdateProfileCommand.builder()
                    .memberId(1L)
                    .email(newEmail)
                    .build();

            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(memberRepository.existsByEmailAndIdNot(newEmail, 1L)).willReturn(false);
            given(memberRepository.save(any(Member.class))).willReturn(testMember);

            // when
            Member result = memberApplicationService.updateProfile(command);

            // then
            assertThat(result).isNotNull();
            then(memberRepository).should().existsByEmailAndIdNot(newEmail, 1L);
            then(memberRepository).should().save(testMember);
        }

        @Test
        @DisplayName("실패: 이메일 중복 시 예외를 발생시킨다")
        void updateProfile_DuplicateEmail_ThrowsException() {
            // given
            Email duplicateEmail = new Email("duplicate@example.com");
            UpdateProfileCommand command = UpdateProfileCommand.builder()
                    .memberId(1L)
                    .email(duplicateEmail)
                    .build();

            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(memberRepository.existsByEmailAndIdNot(duplicateEmail, 1L)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> memberApplicationService.updateProfile(command))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(MemberErrorCode.EMAIL_ALREADY_IN_USE.getMessage());

            // 저장은 호출되지 않음
            then(memberRepository).should(never()).save(any(Member.class));
        }

        @Test
        @DisplayName("성공: 전화번호를 업데이트한다 (중복 없음)")
        void updateProfile_PhoneNumberUpdate_Success() {
            // given
            PhoneNumber newPhone = new PhoneNumber("010-9876-5432");
            UpdateProfileCommand command = UpdateProfileCommand.builder()
                    .memberId(1L)
                    .phoneNumber(newPhone)
                    .build();

            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(memberRepository.existsByPhoneNumberAndIdNot(newPhone, 1L)).willReturn(false);
            given(memberRepository.save(any(Member.class))).willReturn(testMember);

            // when
            Member result = memberApplicationService.updateProfile(command);

            // then
            assertThat(result).isNotNull();
            then(memberRepository).should().existsByPhoneNumberAndIdNot(newPhone, 1L);
            then(memberRepository).should().save(testMember);
        }

        @Test
        @DisplayName("실패: 전화번호 중복 시 예외를 발생시킨다")
        void updateProfile_DuplicatePhoneNumber_ThrowsException() {
            // given
            PhoneNumber duplicatePhone = new PhoneNumber("010-1111-2222");
            UpdateProfileCommand command = UpdateProfileCommand.builder()
                    .memberId(1L)
                    .phoneNumber(duplicatePhone)
                    .build();

            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(memberRepository.existsByPhoneNumberAndIdNot(duplicatePhone, 1L)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> memberApplicationService.updateProfile(command))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(MemberErrorCode.PHONE_ALREADY_IN_USE.getMessage());

            // 저장은 호출되지 않음
            then(memberRepository).should(never()).save(any(Member.class));
        }

        @Test
        @DisplayName("실패: 존재하지 않는 회원 ID로 업데이트 시 예외를 발생시킨다")
        void updateProfile_MemberNotFound_ThrowsException() {
            // given
            UpdateProfileCommand command = UpdateProfileCommand.builder()
                    .memberId(999L)
                    .nickname("새닉네임")
                    .build();

            given(memberRepository.findById(999L)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberApplicationService.updateProfile(command))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("이메일 등록 테스트")
    class RegisterEmailTest {

        @Test
        @DisplayName("성공: 이메일을 등록한다")
        void registerEmail_ValidEmail_Success() {
            // given
            Member testMember = createTestMemberWithoutEmail();  // 이메일이 없는 Member 사용
            Email email = new Email("test@example.com");
            RegisterEmailCommand command = RegisterEmailCommand.builder()
                    .memberId(1L)
                    .email(email)
                    .build();

            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(memberRepository.existsByEmail(email)).willReturn(false);

            // when
            memberApplicationService.registerEmail(command);

            // then
            then(memberRepository).should().existsByEmail(email);
            then(memberRepository).should().save(testMember);
        }

        @Test
        @DisplayName("실패: 이미 사용 중인 이메일 등록 시 예외를 발생시킨다")
        void registerEmail_DuplicateEmail_ThrowsException() {
            // given
            Member testMember = createTestMemberWithoutEmail();  // 이메일이 없는 Member 사용
            Email email = new Email("duplicate@example.com");
            RegisterEmailCommand command = RegisterEmailCommand.builder()
                    .memberId(1L)
                    .email(email)
                    .build();

            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(memberRepository.existsByEmail(email)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> memberApplicationService.registerEmail(command))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(MemberErrorCode.EMAIL_ALREADY_IN_USE.getMessage());
        }
    }

    @Nested
    @DisplayName("회원 조회 테스트")
    class GetMemberTest {

        @Test
        @DisplayName("성공: 회원 ID로 프로필을 조회한다")
        void getMemberProfile_ExistingMember_Success() {
            // given
            Member testMember = createTestMember();
            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));

            // when
            Member result = memberApplicationService.getMemberProfile(1L);

            // then
            assertThat(result).isEqualTo(testMember);
            then(memberRepository).should().findById(1L);
        }

        @Test
        @DisplayName("성공: 소셜 정보로 회원을 조회한다")
        void getMemberBySocial_ExistingMember_Success() {
            // given
            Member testMember = createTestMember();
            given(memberRepository.findBySocial(SocialProvider.KAKAO, "kakao123"))
                    .willReturn(Optional.of(testMember));

            // when
            Member result = memberApplicationService.getMemberBySocial(SocialProvider.KAKAO, "kakao123");

            // then
            assertThat(result).isEqualTo(testMember);
            then(memberRepository).should().findBySocial(SocialProvider.KAKAO, "kakao123");
        }

        @Test
        @DisplayName("실패: 존재하지 않는 소셜 정보로 조회 시 예외를 발생시킨다")
        void getMemberBySocial_NotFound_ThrowsException() {
            // given
            given(memberRepository.findBySocial(SocialProvider.KAKAO, "nonexistent"))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> 
                    memberApplicationService.getMemberBySocial(SocialProvider.KAKAO, "nonexistent"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("회원 상태 변경 테스트")
    class MemberStatusChangeTest {

        @Test
        @DisplayName("성공: 회원을 탈퇴 처리한다")
        void deactivateMember_ExistingMember_Success() {
            // given
            Member testMember = createTestMember();
            DeactivateMemberCommand command = DeactivateMemberCommand.builder()
                    .memberId(1L)
                    .build();

            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));

            // when
            memberApplicationService.deactivateMember(command);

            // then
            then(memberRepository).should().save(testMember);
            assertThat(testMember.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        }

        @Test
        @DisplayName("성공: 회원을 휴면 처리한다")
        void sleepMember_ExistingMember_Success() {
            // given
            Member testMember = createTestMember();
            SleepMemberCommand command = SleepMemberCommand.builder()
                    .memberId(1L)
                    .build();

            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));

            // when
            memberApplicationService.sleepMember(command);

            // then
            then(memberRepository).should().save(testMember);
            assertThat(testMember.getStatus()).isEqualTo(MemberStatus.SLEEP);
        }
    }
}
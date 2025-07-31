package com.bongjangboo.member.domain;

import com.bongjangboo.member.domain.vo.Email;
import com.bongjangboo.member.domain.vo.PhoneNumber;
import com.bongjangboo.member.exception.MemberErrorCode;
import com.bongjangboo.shared.exception.BusinessException;
import com.bongjangboo.shared.identity.SocialProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Member 도메인 단위 테스트
 * 
 * 테스트 대상:
 * - 도메인 팩토리 메서드
 * - 비즈니스 로직 메서드
 * - 도메인 규칙 검증
 * - 예외 상황 처리
 */
@DisplayName("Member 도메인 테스트")
class MemberTest {

    @Nested
    @DisplayName("학습 및 리팩토링 검증")
    class TempTest {


        @Test
        @DisplayName("빌더로 객체 생성시 값이 비면??")
        void temp() {
            Member member = Member.builder()
                    .id(1L).build();
            System.out.println(member.getStatus());
            assertThat(member.getStatus()).isNull();
        }
        // null로 자동으로 빌더가 채우나보네


        @Test
        @DisplayName("애그리거트 루트에다가 @EqualsAndHashCode 사용")
        void equalsAndHash() {
            // 서로 다름
            Member member1 = Member.createNewMember("홍길동", "길동이", SocialProvider.KAKAO, "kakao123");
            Member member2 = Member.createNewMember("고길동", "길동이", SocialProvider.KAKAO, "kakao123");

            // 영속화 이전에 사용하면 null 이라서 같은 객체로 처리됨
            // 컬렉션 사용등 문제 발생 따라서 (@EqualsAndHashCode 삭제해야함)
            // 삭제한 상태로 테스트 통과
            assertThat(member1).isNotEqualTo(member2);
        }
    }




    @Nested
    @DisplayName("회원 생성 테스트")
    class CreateNewMemberTest {

        @Test
        @DisplayName("성공: 유효한 정보로 새 회원을 생성한다")
        void createNewMember_ValidInfo_Success() {
            // given
            String name = "홍길동";
            String nickname = "길동이";
            SocialProvider provider = SocialProvider.KAKAO;
            String socialId = "kakao123";

            // when
            Member member = Member.createNewMember(name, nickname, provider, socialId);

            // then
            assertThat(member.getName()).isEqualTo(name);
            assertThat(member.getNickname()).isEqualTo(nickname);
            assertThat(member.getSocialProvider()).isEqualTo(provider);
            assertThat(member.getSocialId()).isEqualTo(socialId);
            assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
            assertThat(member.getEmail()).isNull(); // 초기에는 null
            assertThat(member.getPhoneNumber()).isNull(); // 초기에는 null
            assertThat(member.getCreatedAt()).isNotNull();
            assertThat(member.getUpdatedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("이메일 관리 테스트")
    class EmailManagementTest {

        private Member createActiveMember() {
            return Member.createNewMember("홍길동", "길동이", SocialProvider.KAKAO, "kakao123");
        }

        @Test
        @DisplayName("성공: 활성 상태 회원이 이메일을 등록한다")
        void registerEmail_ActiveMember_Success() {
            // given
            Member member = createActiveMember();
            Email email = new Email("test@example.com");

            // when
            member.registerEmail(email);

            // then
            assertThat(member.getEmail()).isEqualTo(email);
        }

        @Test
        @DisplayName("실패: 이미 이메일이 등록된 회원은 중복 등록할 수 없다")
        void registerEmail_AlreadyRegistered_ThrowsException() {
            // given
            Member member = createActiveMember();
            Email firstEmail = new Email("first@example.com");
            Email secondEmail = new Email("second@example.com");
            member.registerEmail(firstEmail);

            // when & then
            assertThatThrownBy(() -> member.registerEmail(secondEmail))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(MemberErrorCode.EMAIL_ALREADY_REGISTERED.getMessage());
        }

        @Test
        @DisplayName("성공: 이메일이 등록된 회원이 이메일을 업데이트한다")
        void updateEmail_RegisteredEmail_Success() {
            // given
            Member member = createActiveMember();
            Email oldEmail = new Email("old@example.com");
            Email newEmail = new Email("new@example.com");
            member.registerEmail(oldEmail);

            // when
            member.updateEmail(newEmail);

            // then
            assertThat(member.getEmail()).isEqualTo(newEmail);
        }

        @Test
        @DisplayName("실패: 이메일이 등록되지 않은 회원은 업데이트할 수 없다")
        void updateEmail_NotRegistered_ThrowsException() {
            // given
            Member member = createActiveMember();
            Email email = new Email("test@example.com");

            // when & then
            assertThatThrownBy(() -> member.updateEmail(email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(MemberErrorCode.MEMBER_EMAIL_NOT_REGISTERED.getMessage());
        }

        @Test
        @DisplayName("실패: 비활성 상태 회원은 이메일을 등록할 수 없다")
        void registerEmail_DeactivatedMember_ThrowsException() {
            // given
            Member member = createActiveMember();
            member.deactivate();
            Email email = new Email("test@example.com");

            // when & then
            assertThatThrownBy(() -> member.registerEmail(email))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(MemberErrorCode.CANNOT_UPDATE_DEACTIVATED.getMessage());
        }
    }

    @Nested
    @DisplayName("전화번호 관리 테스트")
    class PhoneNumberManagementTest {

        private Member createActiveMember() {
            return Member.createNewMember("홍길동", "길동이", SocialProvider.KAKAO, "kakao123");
        }

        @Test
        @DisplayName("성공: 활성 상태 회원이 전화번호를 등록한다")
        void registerPhoneNumber_ActiveMember_Success() {
            // given
            Member member = createActiveMember();
            PhoneNumber phoneNumber = new PhoneNumber("010-1234-5678");

            // when
            member.registerPhoneNumber(phoneNumber);

            // then
            assertThat(member.getPhoneNumber()).isEqualTo(phoneNumber);
        }

        @Test
        @DisplayName("실패: 이미 전화번호가 등록된 회원은 중복 등록할 수 없다")
        void registerPhoneNumber_AlreadyRegistered_ThrowsException() {
            // given
            Member member = createActiveMember();
            PhoneNumber firstPhone = new PhoneNumber("010-1234-5678");
            PhoneNumber secondPhone = new PhoneNumber("010-9876-5432");
            member.registerPhoneNumber(firstPhone);

            // when & then
            assertThatThrownBy(() -> member.registerPhoneNumber(secondPhone))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(MemberErrorCode.PHONE_ALREADY_REGISTERED.getMessage());
        }

        @Test
        @DisplayName("성공: 전화번호가 등록된 회원이 전화번호를 업데이트한다")
        void updatePhoneNumber_RegisteredPhone_Success() {
            // given
            Member member = createActiveMember();
            PhoneNumber oldPhone = new PhoneNumber("010-1234-5678");
            PhoneNumber newPhone = new PhoneNumber("010-9876-5432");
            member.registerPhoneNumber(oldPhone);

            // when
            member.updatePhoneNumber(newPhone);

            // then
            assertThat(member.getPhoneNumber()).isEqualTo(newPhone);
        }
    }

    @Nested
    @DisplayName("닉네임 관리 테스트")
    class NicknameManagementTest {

        private Member createActiveMember() {
            return Member.createNewMember("홍길동", "길동이", SocialProvider.KAKAO, "kakao123");
        }

        @Test
        @DisplayName("성공: 활성 상태 회원이 닉네임을 업데이트한다")
        void updateNickname_ActiveMember_Success() {
            // given
            Member member = createActiveMember();
            String newNickname = "새로운닉네임";

            // when
            member.updateNickname(newNickname);

            // then
            assertThat(member.getNickname()).isEqualTo(newNickname);
        }

        @Test
        @DisplayName("실패: 비활성 상태 회원은 닉네임을 업데이트할 수 없다")
        void updateNickname_DeactivatedMember_ThrowsException() {
            // given
            Member member = createActiveMember();
            member.deactivate();
            String newNickname = "새로운닉네임";

            // when & then
            assertThatThrownBy(() -> member.updateNickname(newNickname))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(MemberErrorCode.CANNOT_UPDATE_DEACTIVATED.getMessage());
        }
    }

    @Nested
    @DisplayName("회원 상태 관리 테스트")
    class MemberStatusManagementTest {

        private Member createActiveMember() {
            return Member.createNewMember("홍길동", "길동이", SocialProvider.KAKAO, "kakao123");
        }

        @Test
        @DisplayName("성공: 활성 상태 회원이 휴면 상태로 전환한다")
        void sleep_ActiveMember_Success() {
            // given
            Member member = createActiveMember();

            // when
            member.sleep();

            // then
            assertThat(member.getStatus()).isEqualTo(MemberStatus.SLEEP);
            assertThat(member.isActive()).isFalse();
        }

        @Test
        @DisplayName("실패: 비활성 상태 회원은 휴면 전환할 수 없다")
        void sleep_DeactivatedMember_ThrowsException() {
            // given
            Member member = createActiveMember();
            member.deactivate();

            // when & then
            assertThatThrownBy(member::sleep)
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(MemberErrorCode.SLEEP_ONLY_ACTIVE.getMessage());
        }

        @Test
        @DisplayName("성공: 회원이 탈퇴 처리된다")
        void deactivate_AnyStatus_Success() {
            // given
            Member member = createActiveMember();

            // when
            member.deactivate();

            // then
            assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
            assertThat(member.isActive()).isFalse();
        }

        @Test
        @DisplayName("성공: 휴면 상태 회원도 탈퇴 처리된다")
        void deactivate_SleepMember_Success() {
            // given
            Member member = createActiveMember();
            member.sleep();

            // when
            member.deactivate();

            // then
            assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        }
    }

}
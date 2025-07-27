package com.bongjangboo.member.infra.persistence;

import com.bongjangboo.member.domain.Member;
import com.bongjangboo.member.domain.MemberStatus;
import com.bongjangboo.member.domain.SocialProvider;
import com.bongjangboo.member.domain.vo.Email;
import com.bongjangboo.member.domain.vo.PhoneNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * MemberRepositoryImpl 통합 테스트
 * 
 * 테스트 대상:
 * - JPA 엔티티와 도메인 모델 간의 변환
 * - 데이터베이스 연산 (저장, 조회, 존재 여부 확인)
 * - 복잡한 쿼리 메서드 검증
 * 
 * @DataJpaTest를 사용하여 JPA 관련 컴포넌트만 로드
 */
@DataJpaTest
@Import(MemberRepositoryImpl.class) // 실제 구현체를 테스트 컨텍스트에 포함
@DisplayName("MemberRepositoryImpl 통합 테스트")
class MemberRepositoryImplTest {

    @Autowired
    private MemberRepositoryImpl memberRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Member createTestMember(String socialId, String email, String phoneNumber) {
        return Member.builder()
                .name("홍길동")
                .nickname("길동이")
                .email(email != null ? new Email(email) : null)
                .phoneNumber(phoneNumber != null ? new PhoneNumber(phoneNumber) : null)
                .socialProvider(SocialProvider.KAKAO)
                .socialId(socialId)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    @Nested
    @DisplayName("회원 저장 및 조회 테스트")
    class SaveAndFindTest {

        @Test
        @DisplayName("성공: 회원을 저장하고 ID로 조회한다")
        void save_AndFindById_Success() {
            // given
            Member member = createTestMember("kakao123", "test@example.com", "010-1234-5678");

            // when
            Member savedMember = memberRepository.save(member);
            Optional<Member> foundMember = memberRepository.findById(savedMember.getId());

            // then
            assertThat(foundMember).isPresent();
            assertThat(foundMember.get().getName()).isEqualTo("홍길동");
            assertThat(foundMember.get().getEmail().getValue()).isEqualTo("test@example.com");
            assertThat(foundMember.get().getPhoneNumber().getValue()).isEqualTo("010-1234-5678");
            assertThat(foundMember.get().getSocialProvider()).isEqualTo(SocialProvider.KAKAO);
            assertThat(foundMember.get().getSocialId()).isEqualTo("kakao123");
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID로 조회 시 빈 Optional을 반환한다")
        void findById_NotExisting_ReturnsEmpty() {
            // when
            Optional<Member> foundMember = memberRepository.findById(999L);

            // then
            assertThat(foundMember).isEmpty();
        }

        @Test
        @DisplayName("성공: 소셜 정보로 회원을 조회한다")
        void findBySocial_ExistingMember_Success() {
            // given
            Member member = createTestMember("kakao456", null, null);
            memberRepository.save(member);
            entityManager.flush(); // 즉시 DB에 반영

            // when
            Optional<Member> foundMember = memberRepository.findBySocial(SocialProvider.KAKAO, "kakao456");

            // then
            assertThat(foundMember).isPresent();
            assertThat(foundMember.get().getSocialId()).isEqualTo("kakao456");
            assertThat(foundMember.get().getSocialProvider()).isEqualTo(SocialProvider.KAKAO);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 소셜 정보로 조회 시 빈 Optional을 반환한다")
        void findBySocial_NotExisting_ReturnsEmpty() {
            // when
            Optional<Member> foundMember = memberRepository.findBySocial(SocialProvider.KAKAO, "nonexistent");

            // then
            assertThat(foundMember).isEmpty();
        }
    }

    @Nested
    @DisplayName("소셜 계정 중복 검사 테스트")
    class SocialDuplicateCheckTest {

        @Test
        @DisplayName("성공: 존재하는 소셜 계정의 중복 여부를 확인한다")
        void existsBySocial_ExistingAccount_ReturnsTrue() {
            // given
            Member member = createTestMember("kakao789", null, null);
            memberRepository.save(member);
            entityManager.flush();

            // when
            boolean exists = memberRepository.existsBySocial(SocialProvider.KAKAO, "kakao789");

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("성공: 존재하지 않는 소셜 계정의 중복 여부를 확인한다")
        void existsBySocial_NotExistingAccount_ReturnsFalse() {
            // when
            boolean exists = memberRepository.existsBySocial(SocialProvider.KAKAO, "nonexistent");

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("이메일 중복 검사 테스트")
    class EmailDuplicateCheckTest {

        @Test
        @DisplayName("성공: 존재하는 이메일의 중복 여부를 확인한다")
        void existsByEmail_ExistingEmail_ReturnsTrue() {
            // given
            Member member = createTestMember("kakao001", "existing@example.com", null);
            memberRepository.save(member);
            entityManager.flush();

            // when
            boolean exists = memberRepository.existsByEmail(new Email("existing@example.com"));

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("성공: 존재하지 않는 이메일의 중복 여부를 확인한다")
        void existsByEmail_NotExistingEmail_ReturnsFalse() {
            // when
            boolean exists = memberRepository.existsByEmail(new Email("notexisting@example.com"));

            // then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("성공: 자기 자신을 제외한 이메일 중복 검사를 한다")
        void existsByEmailAndIdNot_ExcludingSelf_ReturnsCorrectResult() {
            // given
            Member member1 = createTestMember("kakao002", "user1@example.com", null);
            Member member2 = createTestMember("kakao003", "user2@example.com", null);
            Member savedMember1 = memberRepository.save(member1);
            Member savedMember2 = memberRepository.save(member2);
            entityManager.flush();

            // when
            boolean existsExcludingSelf = memberRepository.existsByEmailAndIdNot(
                    new Email("user1@example.com"), savedMember1.getId());
            boolean existsIncludingOther = memberRepository.existsByEmailAndIdNot(
                    new Email("user2@example.com"), savedMember1.getId());

            // then
            assertThat(existsExcludingSelf).isFalse(); // 자기 자신은 제외되므로 false
            assertThat(existsIncludingOther).isTrue(); // 다른 사용자가 사용 중이므로 true
        }
    }

    @Nested
    @DisplayName("전화번호 중복 검사 테스트")
    class PhoneNumberDuplicateCheckTest {

        @Test
        @DisplayName("성공: 존재하는 전화번호의 중복 여부를 확인한다")
        void existsByPhoneNumber_ExistingPhone_ReturnsTrue() {
            // given
            Member member = createTestMember("kakao004", null, "010-1111-2222");
            memberRepository.save(member);
            entityManager.flush();

            // when
            boolean exists = memberRepository.existsByPhoneNumber(new PhoneNumber("010-1111-2222"));

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("성공: 존재하지 않는 전화번호의 중복 여부를 확인한다")
        void existsByPhoneNumber_NotExistingPhone_ReturnsFalse() {
            // when
            boolean exists = memberRepository.existsByPhoneNumber(new PhoneNumber("010-9999-8888"));

            // then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("성공: 자기 자신을 제외한 전화번호 중복 검사를 한다")
        void existsByPhoneNumberAndIdNot_ExcludingSelf_ReturnsCorrectResult() {
            // given
            Member member1 = createTestMember("kakao005", null, "010-3333-4444");
            Member member2 = createTestMember("kakao006", null, "010-5555-6666");
            Member savedMember1 = memberRepository.save(member1);
            Member savedMember2 = memberRepository.save(member2);
            entityManager.flush();

            // when
            boolean existsExcludingSelf = memberRepository.existsByPhoneNumberAndIdNot(
                    new PhoneNumber("010-3333-4444"), savedMember1.getId());
            boolean existsIncludingOther = memberRepository.existsByPhoneNumberAndIdNot(
                    new PhoneNumber("010-5555-6666"), savedMember1.getId());

            // then
            assertThat(existsExcludingSelf).isFalse(); // 자기 자신은 제외되므로 false
            assertThat(existsIncludingOther).isTrue(); // 다른 사용자가 사용 중이므로 true
        }
    }

    @Nested
    @DisplayName("데이터 변환 테스트")
    class DataConversionTest {

        @Test
        @DisplayName("성공: 도메인 모델이 JPA 엔티티로 정확히 변환된다")
        void domainToEntity_ConversionCorrect() {
            // given
            Member member = createTestMember("kakao007", "convert@example.com", "010-7777-8888");

            // when
            Member savedMember = memberRepository.save(member);

            // then
            assertThat(savedMember.getId()).isNotNull(); // JPA가 ID를 생성함
            assertThat(savedMember.getName()).isEqualTo("홍길동");
            assertThat(savedMember.getEmail().getValue()).isEqualTo("convert@example.com");
            assertThat(savedMember.getPhoneNumber().getValue()).isEqualTo("010-7777-8888");
            assertThat(savedMember.getCreatedAt()).isNotNull();
            assertThat(savedMember.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("성공: null 값이 포함된 도메인 모델도 정확히 처리된다")
        void domainWithNullValues_ConversionCorrect() {
            // given
            Member member = createTestMember("kakao008", null, null);

            // when
            Member savedMember = memberRepository.save(member);
            Optional<Member> foundMember = memberRepository.findById(savedMember.getId());

            // then
            assertThat(foundMember).isPresent();
            assertThat(foundMember.get().getEmail()).isNull();
            assertThat(foundMember.get().getPhoneNumber()).isNull();
            assertThat(foundMember.get().getName()).isEqualTo("홍길동");
        }
    }
}
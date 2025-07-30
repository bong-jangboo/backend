package com.bongjangboo.member.domain.vo;

import com.bongjangboo.member.exception.MemberErrorCode;
import com.bongjangboo.shared.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Email Value Object 단위 테스트
 * 
 * 테스트 대상:
 * - 이메일 형식 검증
 * - 불변성 보장
 * - 동등성 비교
 */
@DisplayName("Email VO 테스트")
class EmailTest {

    @Nested
    @DisplayName("이메일 생성 테스트")
    class EmailCreationTest {

        @ParameterizedTest
        @ValueSource(strings = {
                "test@example.com",
                "user123@domain.co.kr",
                "admin@sub.domain.org",
                "valid.email+tag@example.com",
                "user_name@test-domain.com"
        })
        @DisplayName("성공: 유효한 이메일 형식으로 Email을 생성한다")
        void createEmail_ValidFormat_Success(String validEmail) {
            // when
            Email email = new Email(validEmail);

            // then
            assertThat(email.getValue()).isEqualTo(validEmail);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "invalid-email",
                "@example.com",
                "test@",
                "test@@example.com",
                "test.example.com",
                "",
                " ",
                "test @example.com"
        })
        @DisplayName("실패: 유효하지 않은 이메일 형식은 예외를 발생시킨다")
        void createEmail_InvalidFormat_ThrowsException(String invalidEmail) {
            // when & then
            assertThatThrownBy(() -> new Email(invalidEmail))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(MemberErrorCode.EMAIL_INVALID_FORMAT.getMessage());
        }

        @Test
        @DisplayName("실패: null 값으로 Email을 생성할 수 없다")
        void createEmail_NullValue_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> new Email(null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(MemberErrorCode.EMAIL_INVALID_FORMAT.getMessage());
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("성공: 같은 값의 Email 객체는 동등하다")
        void equals_SameValue_ReturnsTrue() {
            // given
            String emailValue = "test@example.com";
            Email email1 = new Email(emailValue);
            Email email2 = new Email(emailValue);

            // when & then
            assertThat(email1).isEqualTo(email2).hasSameHashCodeAs(email2);
        }

        @Test
        @DisplayName("성공: 다른 값의 Email 객체는 동등하지 않다")
        void equals_DifferentValue_ReturnsFalse() {
            // given
            Email email1 = new Email("test1@example.com");
            Email email2 = new Email("test2@example.com");

            // when & then
            assertThat(email1).isNotEqualTo(email2);
        }

        @Test
        @DisplayName("성공: Email 객체와 null은 동등하지 않다")
        void equals_WithNull_ReturnsFalse() {
            // given
            Email email = new Email("test@example.com");

            // when & then
            assertThat(email).isNotEqualTo(null);
        }

        @Test
        @DisplayName("성공: Email 객체와 다른 타입은 동등하지 않다")
        void equals_WithDifferentType_ReturnsFalse() {
            // given
            Email email = new Email("test@example.com");
            String string = "test@example.com";

            // when & then
            assertThat(email).isNotEqualTo(string);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("성공: Email 객체는 불변 객체이다")
        void email_IsImmutable() {
            // given
            String originalValue = "test@example.com";
            Email email = new Email(originalValue);

            // when
            String retrievedValue = email.getValue();

            // then
            assertThat(retrievedValue).isEqualTo(originalValue);
            // getValue()로 반환된 값을 수정해도 원본에 영향을 주지 않음을 확인
            // (String은 불변이므로 이 테스트는 개념적 확인)
        }
    }
}
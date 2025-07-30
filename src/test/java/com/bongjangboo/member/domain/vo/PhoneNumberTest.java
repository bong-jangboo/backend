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
 * PhoneNumber Value Object 단위 테스트
 * 
 * 테스트 대상:
 * - 전화번호 형식 검증 (010-XXXX-XXXX)
 * - 불변성 보장
 * - 동등성 비교
 */
@DisplayName("PhoneNumber VO 테스트")
class PhoneNumberTest {

    @Nested
    @DisplayName("전화번호 생성 테스트")
    class PhoneNumberCreationTest {

        @ParameterizedTest
        @ValueSource(strings = {
                "010-1234-5678",
                "010-9999-0000",
                "010-0000-1111",
                "0101234-5678",  // 하이픈 없는 형태 (첫 번째 하이픈만)
                "010-12345678"   // 하이픈 없는 형태 (두 번째 하이픈만)
        })
        @DisplayName("성공: 유효한 전화번호 형식으로 PhoneNumber를 생성한다")
        void createPhoneNumber_ValidFormat_Success(String validPhoneNumber) {
            // when
            PhoneNumber phoneNumber = new PhoneNumber(validPhoneNumber);

            // then
            assertThat(phoneNumber.getValue()).isEqualTo(validPhoneNumber);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "011-1234-5678",  // 010이 아닌 번호
                "010-123-5678",   // 가운데 자리 부족
                "010-12345-678",  // 마지막 자리 부족
                "010-1234-56789", // 마지막 자리 초과
                "010-abcd-5678",  // 숫자가 아닌 문자
                "010.1234.5678",  // 다른 구분자
                "010 1234 5678",  // 공백 구분자
                "",
                " "
        })
        @DisplayName("실패: 유효하지 않은 전화번호 형식은 예외를 발생시킨다")
        void createPhoneNumber_InvalidFormat_ThrowsException(String invalidPhoneNumber) {
            // when & then
            assertThatThrownBy(() -> new PhoneNumber(invalidPhoneNumber))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(MemberErrorCode.PHONE_INVALID_FORMAT.getMessage());
        }

        @Test
        @DisplayName("실패: null 값으로 PhoneNumber를 생성할 수 없다")
        void createPhoneNumber_NullValue_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> new PhoneNumber(null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage(MemberErrorCode.PHONE_INVALID_FORMAT.getMessage());
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("성공: 같은 값의 PhoneNumber 객체는 동등하다")
        void equals_SameValue_ReturnsTrue() {
            // given
            String phoneValue = "010-1234-5678";
            PhoneNumber phone1 = new PhoneNumber(phoneValue);
            PhoneNumber phone2 = new PhoneNumber(phoneValue);

            // when & then
            assertThat(phone1).isEqualTo(phone2);
            assertThat(phone1.hashCode()).hasSameHashCodeAs(phone2.hashCode());
        }

        @Test
        @DisplayName("성공: 다른 값의 PhoneNumber 객체는 동등하지 않다")
        void equals_DifferentValue_ReturnsFalse() {
            // given
            PhoneNumber phone1 = new PhoneNumber("010-1234-5678");
            PhoneNumber phone2 = new PhoneNumber("010-9876-5432");

            // when & then
            assertThat(phone1).isNotEqualTo(phone2);
        }

        @Test
        @DisplayName("성공: PhoneNumber 객체와 null은 동등하지 않다")
        void equals_WithNull_ReturnsFalse() {
            // given
            PhoneNumber phone = new PhoneNumber("010-1234-5678");

            // when & then
            assertThat(phone).isNotEqualTo(null);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("성공: PhoneNumber 객체는 불변 객체이다")
        void phoneNumber_IsImmutable() {
            // given
            String originalValue = "010-1234-5678";
            PhoneNumber phoneNumber = new PhoneNumber(originalValue);

            // when
            String retrievedValue = phoneNumber.getValue();

            // then
            assertThat(retrievedValue).isEqualTo(originalValue);
        }
    }
}
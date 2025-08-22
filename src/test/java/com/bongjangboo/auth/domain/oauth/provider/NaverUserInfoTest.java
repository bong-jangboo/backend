package com.bongjangboo.auth.domain.oauth.provider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("NaverUserInfo 단위 테스트")
class NaverUserInfoTest {

    private Map<String, Object> validNaverAttributes;
    private Map<String, Object> validResponseData;

    @BeforeEach
    void setUp() {
        // Given: 네이버 API 응답 형태의 정상적인 데이터
        validResponseData = new HashMap<>();
        validResponseData.put("id", "naver-user-id-123");
        validResponseData.put("email", "test@naver.com");
        validResponseData.put("name", "홍길동");

        validNaverAttributes = new HashMap<>();
        validNaverAttributes.put("response", validResponseData);
        validNaverAttributes.put("resultcode", "00");
        validNaverAttributes.put("message", "success");
    }

    @Nested
    @DisplayName("정상적인 네이버 응답 데이터로")
    class WithValidNaverResponse {

        @Test
        @DisplayName("Provider 정보를 올바르게 반환한다")
        void returnCorrectProvider() {
            // Given
            NaverUserInfo naverUserInfo = new NaverUserInfo(validNaverAttributes);

            // When
            String provider = naverUserInfo.getProvider();

            // Then
            assertThat(provider).isEqualTo("naver");
        }

        @Test
        @DisplayName("Provider ID를 올바르게 반환한다")
        void returnCorrectProviderId() {
            // Given
            NaverUserInfo naverUserInfo = new NaverUserInfo(validNaverAttributes);

            // When
            String providerId = naverUserInfo.getProviderId();

            // Then
            assertThat(providerId).isEqualTo("naver-user-id-123");
        }

        @Test
        @DisplayName("이메일을 올바르게 반환한다")
        void returnCorrectEmail() {
            // Given
            NaverUserInfo naverUserInfo = new NaverUserInfo(validNaverAttributes);

            // When
            String email = naverUserInfo.getEmail();

            // Then
            assertThat(email).isEqualTo("test@naver.com");
        }

        @Test
        @DisplayName("이름을 올바르게 반환한다")
        void returnCorrectName() {
            // Given
            NaverUserInfo naverUserInfo = new NaverUserInfo(validNaverAttributes);

            // When
            String name = naverUserInfo.getName();

            // Then
            assertThat(name).isEqualTo("홍길동");
        }
    }

    @Nested
    @DisplayName("예외적인 데이터에 대해")
    class WithExceptionalData {

//        @Test
//        @DisplayName("response 키가 없는 경우 NullPointerException을 발생시킨다")
//        void throwNullPointerExceptionWhenResponseKeyMissing() {
//            // Given
//            Map<String, Object> attributesWithoutResponse = new HashMap<>();
//            attributesWithoutResponse.put("resultcode", "00");
//            attributesWithoutResponse.put("message", "success");
//
//            // When & Then
//            assertThatThrownBy(() -> new NaverUserInfo(attributesWithoutResponse))
//                    .isInstanceOf(NullPointerException.class);
//        }

//        @Test
//        @DisplayName("response 값이 null인 경우 NullPointerException을 발생시킨다")
//        void throwNullPointerExceptionWhenResponseValueIsNull() {
//            // Given
//            Map<String, Object> attributesWithNullResponse = new HashMap<>();
//            attributesWithNullResponse.put("response", null);
//            attributesWithNullResponse.put("resultcode", "00");
//
//            // When & Then
//            assertThatThrownBy(() -> new NaverUserInfo(attributesWithNullResponse))
//                    .isInstanceOf(NullPointerException.class);
//        }

        @Test
        @DisplayName("id 필드가 없는 경우 getProviderId()에서 NullPointerException을 발생시킨다")
        void throwNullPointerExceptionWhenIdFieldMissing() {
            // Given
            Map<String, Object> responseWithoutId = new HashMap<>();
            responseWithoutId.put("email", "test@naver.com");
            responseWithoutId.put("name", "홍길동");

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("response", responseWithoutId);

            NaverUserInfo naverUserInfo = new NaverUserInfo(attributes);

            // When & Then
            assertThatThrownBy(naverUserInfo::getProviderId)
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("email 필드가 없는 경우 getEmail()에서 NullPointerException을 발생시킨다")
        void throwNullPointerExceptionWhenEmailFieldMissing() {
            // Given
            Map<String, Object> responseWithoutEmail = new HashMap<>();
            responseWithoutEmail.put("id", "naver-user-id-123");
            responseWithoutEmail.put("name", "홍길동");

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("response", responseWithoutEmail);

            NaverUserInfo naverUserInfo = new NaverUserInfo(attributes);

            // When & Then
            assertThatThrownBy(naverUserInfo::getEmail)
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("name 필드가 없는 경우 getName()에서 NullPointerException을 발생시킨다")
        void throwNullPointerExceptionWhenNameFieldMissing() {
            // Given
            Map<String, Object> responseWithoutName = new HashMap<>();
            responseWithoutName.put("id", "naver-user-id-123");
            responseWithoutName.put("email", "test@naver.com");

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("response", responseWithoutName);

            NaverUserInfo naverUserInfo = new NaverUserInfo(attributes);

            // When & Then
            assertThatThrownBy(naverUserInfo::getName)
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("빈 문자열 필드값들을 올바르게 처리한다")
        void handleEmptyStringFields() {
            // Given
            Map<String, Object> responseWithEmptyStrings = new HashMap<>();
            responseWithEmptyStrings.put("id", "");
            responseWithEmptyStrings.put("email", "");
            responseWithEmptyStrings.put("name", "");

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("response", responseWithEmptyStrings);

            NaverUserInfo naverUserInfo = new NaverUserInfo(attributes);

            // When & Then
            assertThat(naverUserInfo.getProviderId()).isEmpty();
            assertThat(naverUserInfo.getEmail()).isEmpty();
            assertThat(naverUserInfo.getName()).isEmpty();
        }

        @Test
        @DisplayName("숫자 타입의 id 필드를 문자열로 변환한다")
        void convertNumericIdToString() {
            // Given
            Map<String, Object> responseWithNumericId = new HashMap<>();
            responseWithNumericId.put("id", 123456789L);
            responseWithNumericId.put("email", "test@naver.com");
            responseWithNumericId.put("name", "홍길동");

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("response", responseWithNumericId);

            NaverUserInfo naverUserInfo = new NaverUserInfo(attributes);

            // When
            String providerId = naverUserInfo.getProviderId();

            // Then
            assertThat(providerId).isEqualTo("123456789");
        }
    }

    @Nested
    @DisplayName("실제 네이버 API 응답 형태 테스트")
    class RealNaverApiResponseTest {

        @Test
        @DisplayName("실제 네이버 API 응답 형태를 올바르게 파싱한다")
        void parseRealNaverApiResponse() {
            // Given: 실제 네이버 API 응답과 유사한 형태
            Map<String, Object> realNaverResponse = new HashMap<>();
            realNaverResponse.put("id", "32742776");
            realNaverResponse.put("nickname", "OpenAPI");
            realNaverResponse.put("name", "홍길동");
            realNaverResponse.put("email", "openapi@naver.com");
            realNaverResponse.put("gender", "F");
            realNaverResponse.put("age", "40-49");
            realNaverResponse.put("birthday", "05-16");
            realNaverResponse.put("profile_image", "https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif");
            realNaverResponse.put("birthyear", "1900");
            realNaverResponse.put("mobile", "010-0000-0000");

            Map<String, Object> realAttributes = new HashMap<>();
            realAttributes.put("resultcode", "00");
            realAttributes.put("message", "success");
            realAttributes.put("response", realNaverResponse);

            NaverUserInfo naverUserInfo = new NaverUserInfo(realAttributes);

            // When & Then
            assertThat(naverUserInfo.getProvider()).isEqualTo("naver");
            assertThat(naverUserInfo.getProviderId()).isEqualTo("32742776");
            assertThat(naverUserInfo.getEmail()).isEqualTo("openapi@naver.com");
            assertThat(naverUserInfo.getName()).isEqualTo("홍길동");
        }
    }
}
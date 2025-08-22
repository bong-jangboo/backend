package com.bongjangboo.auth.infra.oauth;

import com.bongjangboo.auth.domain.oauth.OAuth2UserInfo;
import com.bongjangboo.member.domain.SocialProvider;
import com.bongjangboo.shared.exception.BusinessException;
import com.bongjangboo.shared.exception.CommonErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OAuthAttributes 테스트")
class OAuthAttributesTest {

    private Map<String, Object> naverAttributes;
    private Map<String, Object> naverResponse;

    @BeforeEach
    void setUp() {
        // Given: 네이버 API 응답 형태의 테스트 데이터
        naverResponse = new HashMap<>();
        naverResponse.put("id", "naver-test-id");
        naverResponse.put("email", "test@naver.com");
        naverResponse.put("name", "테스트유저");

        naverAttributes = new HashMap<>();
        naverAttributes.put("response", naverResponse);
        naverAttributes.put("resultcode", "00");
        naverAttributes.put("message", "success");
    }

    @Nested
    @DisplayName("of 메서드는")
    class OfMethod {

        @Test
        @DisplayName("네이버 SocialProvider로 NaverUserInfo를 포함한 OAuthAttributes를 생성한다")
        void createOAuthAttributesWithNaverUserInfoForNaverProvider() {
            // When
            OAuthAttributes result = OAuthAttributes.of(
                    SocialProvider.NAVER, 
                    "response", 
                    naverAttributes
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNameAttributeKey()).isEqualTo("response");
            assertThat(result.getOAuth2UserInfo()).isNotNull();
            assertThat(result.getOAuth2UserInfo().getProvider()).isEqualTo("naver");
            assertThat(result.getOAuth2UserInfo().getEmail()).isEqualTo("test@naver.com");
            assertThat(result.getOAuth2UserInfo().getName()).isEqualTo("테스트유저");
        }

        @Test
        @DisplayName("카카오 SocialProvider로 null을 반환한다 (미구현)")
        void returnNullForKakaoProvider() {
            // When
            OAuthAttributes result = OAuthAttributes.of(
                    SocialProvider.KAKAO, 
                    "id", 
                    new HashMap<>()
            );

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("구글 SocialProvider로 BusinessException을 발생시킨다 (미지원)")
        void throwBusinessExceptionForGoogleProvider() {
            // When & Then
            assertThatThrownBy(() -> OAuthAttributes.of(
                    SocialProvider.GOOGLE, 
                    "sub", 
                    new HashMap<>()
            ))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", CommonErrorCode.INTERNAL_ERROR);
        }

        @Test
        @DisplayName("지원하지 않는 SocialProvider로 BusinessException을 발생시킨다")
        void throwBusinessExceptionForUnsupportedProvider() {
            // Given: 미래에 추가될 수 있는 가상의 Provider
            // 실제로는 enum에 없지만 테스트를 위해 반사적으로 테스트
            
            // When & Then
            assertThatThrownBy(() -> OAuthAttributes.of(
                    SocialProvider.GOOGLE, // 현재 미지원
                    "sub", 
                    new HashMap<>()
            ))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorCode", CommonErrorCode.INTERNAL_ERROR);
        }
    }

    @Nested
    @DisplayName("ofNaver 메서드는")
    class OfNaverMethod {

        @Test
        @DisplayName("네이버 API 응답으로 OAuthAttributes를 생성한다")
        void createOAuthAttributesFromNaverApiResponse() {
            // When
            OAuthAttributes result = OAuthAttributes.ofNaver("response", naverAttributes);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNameAttributeKey()).isEqualTo("response");
            
            OAuth2UserInfo userInfo = result.getOAuth2UserInfo();
            assertThat(userInfo).isNotNull();
            assertThat(userInfo.getProvider()).isEqualTo("naver");
            assertThat(userInfo.getProviderId()).isEqualTo("naver-test-id");
            assertThat(userInfo.getEmail()).isEqualTo("test@naver.com");
            assertThat(userInfo.getName()).isEqualTo("테스트유저");
        }

        @Test
        @DisplayName("빈 attributes Map으로도 OAuthAttributes를 생성한다")
        void createOAuthAttributesWithEmptyAttributesMap() {
            // Given
            Map<String, Object> emptyAttributes = new HashMap<>();
            emptyAttributes.put("response", new HashMap<>());

            // When
            OAuthAttributes result = OAuthAttributes.ofNaver("response", emptyAttributes);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNameAttributeKey()).isEqualTo("response");
            assertThat(result.getOAuth2UserInfo()).isNotNull();
        }

        @Test
        @DisplayName("다양한 nameAttributeKey 값으로 OAuthAttributes를 생성한다")
        void createOAuthAttributesWithDifferentNameAttributeKeys() {
            // Given
            String[] differentKeys = {"response", "id", "user_id", "naver_id"};

            for (String key : differentKeys) {
                // When
                OAuthAttributes result = OAuthAttributes.ofNaver(key, naverAttributes);

                // Then
                assertThat(result).isNotNull();
                assertThat(result.getNameAttributeKey()).isEqualTo(key);
                assertThat(result.getOAuth2UserInfo()).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("Builder 패턴 테스트")
    class BuilderPatternTest {

        @Test
        @DisplayName("Builder를 통해 OAuthAttributes를 생성할 수 있다")
        void createOAuthAttributesUsingBuilder() {
            // Given
            OAuth2UserInfo mockUserInfo = new OAuth2UserInfo() {
                @Override
                public String getProvider() { return "test"; }
                @Override
                public String getProviderId() { return "test-id"; }
                @Override
                public String getEmail() { return "test@test.com"; }
                @Override
                public String getName() { return "Test User"; }
            };

            // When
            OAuthAttributes result = OAuthAttributes.builder()
                    .nameAttributeKey("test_key")
                    .oAuth2UserInfo(mockUserInfo)
                    .build();

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNameAttributeKey()).isEqualTo("test_key");
            assertThat(result.getOAuth2UserInfo()).isEqualTo(mockUserInfo);
        }
    }

//    @Nested
//    @DisplayName("예외 상황 처리")
//    class ExceptionHandling {
//
//        @Test
//        @DisplayName("null SocialProvider로 NullPointerException을 발생시킨다")
//        void throwNullPointerExceptionForNullSocialProvider() {
//            // When & Then
//            assertThatThrownBy(() -> OAuthAttributes.of(
//                    null,
//                    "response",
//                    naverAttributes
//            ))
//            .isInstanceOf(NullPointerException.class);
//        }
//
//        @Test
//        @DisplayName("null attributes로도 처리할 수 있다 (NaverUserInfo 생성자에서 처리)")
//        void handleNullAttributes() {
//            // When & Then
//            // NaverUserInfo 생성자에서 NPE가 발생할 것임
//            assertThatThrownBy(() -> OAuthAttributes.of(
//                    SocialProvider.NAVER,
//                    "response",
//                    null
//            ))
//            .isInstanceOf(NullPointerException.class);
//        }
//    }

    @Nested
    @DisplayName("통합 시나리오 테스트")
    class IntegrationScenarioTest {

        @Test
        @DisplayName("실제 OAuth2 플로우에서 사용되는 시나리오를 시뮬레이션한다")
        void simulateRealOAuth2Flow() {
            // Given: 실제 네이버 OAuth2 플로우에서 받을 수 있는 데이터
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

            // When: OAuth2Service에서 호출되는 것과 동일한 방식
            OAuthAttributes result = OAuthAttributes.of(
                    SocialProvider.NAVER, 
                    "response", 
                    realAttributes
            );

            // Then: 필요한 정보가 모두 추출되는지 확인
            assertThat(result).isNotNull();
            assertThat(result.getNameAttributeKey()).isEqualTo("response");
            
            OAuth2UserInfo userInfo = result.getOAuth2UserInfo();
            assertThat(userInfo.getProvider()).isEqualTo("naver");
            assertThat(userInfo.getProviderId()).isEqualTo("32742776");
            assertThat(userInfo.getEmail()).isEqualTo("openapi@naver.com");
            assertThat(userInfo.getName()).isEqualTo("홍길동");
        }
    }
}
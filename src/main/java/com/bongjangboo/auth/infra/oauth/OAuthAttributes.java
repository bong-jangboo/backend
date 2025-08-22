package com.bongjangboo.auth.infra.oauth;

import com.bongjangboo.auth.domain.oauth.OAuth2UserInfo;
import com.bongjangboo.auth.domain.oauth.provider.NaverUserInfo;
import com.bongjangboo.auth.domain.oauth.SocialProvider;
import com.bongjangboo.shared.exception.BusinessException;
import com.bongjangboo.shared.exception.CommonErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Slf4j
public class OAuthAttributes {

    private String nameAttributeKey;  // Oauth2 로그인 진행시 키가 되는 값
    private OAuth2UserInfo oAuth2UserInfo; // provider 별 로그인 유저 정보를 담아야함


    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }


    public static OAuthAttributes of(SocialProvider socialProvider, String usernameAttributeKey, Map<String, Object> attributes) {

        if(socialProvider == SocialProvider.NAVER){
            return ofNaver(usernameAttributeKey, attributes);
        } else if (socialProvider == SocialProvider.KAKAO) {
            // TODO
            log.debug("미구현");
            return null;
        } else {
            throw new BusinessException(CommonErrorCode.INTERNAL_ERROR);
        }
    }


    public static OAuthAttributes ofNaver(String usernameAttributeKey, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(usernameAttributeKey)
                .oAuth2UserInfo(new NaverUserInfo(attributes))
                .build();
    }


}

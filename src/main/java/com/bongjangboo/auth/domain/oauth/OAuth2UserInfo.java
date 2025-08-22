package com.bongjangboo.auth.domain.oauth;

/**
 * Provider 별로 받은 json 데이터에서 원하는 값을 추출하는 메서드
 */
public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}

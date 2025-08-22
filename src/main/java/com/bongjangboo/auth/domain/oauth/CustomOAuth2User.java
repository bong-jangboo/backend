package com.bongjangboo.auth.domain.oauth;

import com.bongjangboo.member.domain.vo.Email;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * OAuth2 인증 후 Spring Security에서 사용할 사용자 정보를 담는 클래스
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private Email email;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey,
                            Email email) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
    }

}
package com.bongjangboo.auth.application.service;

import com.bongjangboo.auth.domain.Role;
import com.bongjangboo.auth.domain.oauth.CustomOAuth2User;
import com.bongjangboo.auth.infra.oauth.OAuthAttributes;
import com.bongjangboo.member.application.command.RegisterMemberFromSocialCommand;
import com.bongjangboo.member.application.service.MemberApplicationService;
import com.bongjangboo.member.domain.Member;
import com.bongjangboo.auth.domain.oauth.SocialProvider;
import com.bongjangboo.member.domain.MemberStatus;
import com.bongjangboo.shared.exception.BusinessException;
import com.bongjangboo.shared.exception.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2Service extends DefaultOAuth2UserService {

    private final MemberApplicationService memberService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        
        // 1. OAuth2 Provider에서 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2 User loaded with attributes: {}", oAuth2User.getAttributes());

        // 2. 조회를 위한 정보 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialProvider socialProvider = getSocialProvider(registrationId);

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String,Object> attributes = oAuth2User.getAttributes();

        // OAuth2UserInfo 가 저장된 OAuthAttributes 객체 생성
        OAuthAttributes extractedAttributes = OAuthAttributes.of(socialProvider, userNameAttributeName, attributes);

        // 3. 추출한 정보로 Member 조회
        Member member = getMember(extractedAttributes, socialProvider);

        // 4. Security 객체 생성 후 반환
        Role role = mapStatusToRole(member);
        Collection<GrantedAuthority> authorities = createAuthorities(role);

        return new CustomOAuth2User(authorities, attributes, userNameAttributeName,member.getEmail());
    }

    private Member getMember(OAuthAttributes attributes, SocialProvider socialProvider) {
        String name = attributes.getOAuth2UserInfo().getName();
        String email = attributes.getOAuth2UserInfo().getEmail();
        String socialId = attributes.getOAuth2UserInfo().getProviderId();

        var command = RegisterMemberFromSocialCommand.builder()
                .name(name)
                .email(email)
                .socialProvider(socialProvider)
                .socialId(socialId)
                .build();

        return memberService.findOrCreateFromSocialLogin(command);
    }

    private SocialProvider getSocialProvider(String registrationId) {
        return switch (registrationId) {
            case "naver" -> SocialProvider.NAVER;
            case "kakao" -> SocialProvider.KAKAO;
            case "google" -> SocialProvider.GOOGLE;
            default -> throw new BusinessException(CommonErrorCode.INTERNAL_ERROR);
        };
    }

    private Role mapStatusToRole(Member member) {
        return switch (member.getStatus()){
            case ACTIVE -> Role.USER;
            case PENDING_ONBOARDING -> Role.GUEST;
            default -> throw new BusinessException(CommonErrorCode.INTERNAL_ERROR);
        };

    }

    private Collection<GrantedAuthority> createAuthorities(Role role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getAuthority()));
    }

}

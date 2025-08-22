package com.bongjangboo.jangboo.member;

import com.bongjangboo.member.application.command.RegisterMemberCommand;
import com.bongjangboo.member.application.service.MemberApplicationService;
import com.bongjangboo.member.domain.Member;
import com.bongjangboo.member.domain.MemberRepository;
import com.bongjangboo.auth.domain.oauth.SocialProvider;
import com.bongjangboo.member.exception.MemberErrorCode;
import com.bongjangboo.shared.event.DomainEventPublisher;
import com.bongjangboo.shared.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberApplicationServiceTest {

    private MemberApplicationService service;
    private DomainEventPublisher eventPublisher;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        eventPublisher = mock(DomainEventPublisher.class);
        service = new MemberApplicationService(memberRepository, eventPublisher);
    }

    @Test
    @DisplayName("회원가입을 정상적으로 수행한다")
    void register() {
        // Given
        RegisterMemberCommand command = RegisterMemberCommand.builder()
                .name("채원")
                .nickname("백일평냉")
                .socialProvider(SocialProvider.KAKAO)
                .socialId("kakao-123")
                .build();
        when(memberRepository.existsBySocial(any(),any())).thenReturn(false);

        // save() mock에서 id 세팅
        when(memberRepository.save(any())).thenAnswer(invocation -> {
            Member member = invocation.getArgument(0);
            Field idField = Member.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, 1L);
            return member;
        });

        // When
        Member registerMember = service.registerMember(command);

        // Then
        assertThat(registerMember.getName()).isEqualTo(command.getName());
        verify(eventPublisher).publish(any());
    }

    @Test
    @DisplayName("중복 소셜계정 가입실패")
    void register_duplicate() {
        // given
        RegisterMemberCommand command = RegisterMemberCommand.builder()
                .name("채원")
                .nickname("백일평냉")
                .socialProvider(SocialProvider.KAKAO)
                .socialId("kakao-123")
                .build();

        // 첫 번째 가입 시 mock
        when(memberRepository.existsBySocial(any(), any()))
                .thenReturn(false)   // 첫번째 시도는 중복 아님
                .thenReturn(true);   // 두번째 시도는 중복

        when(memberRepository.save(any())).thenAnswer(invocation -> {
            Member member = invocation.getArgument(0);
            // 테스트 편의상 id 채우기
            Field idField = Member.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, 1L);
            return member;
        });

        // when
        Member registerMember = service.registerMember(command);

        // then
        assertThat(registerMember.getId()).isEqualTo(1L);

        // when - 두 번째 가입 시도
        // then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                service.registerMember(command)
        );
        assertThat(ex.getErrorCode()).isEqualTo(MemberErrorCode.DUPLICATE_SOCIAL_ID);
    }


}

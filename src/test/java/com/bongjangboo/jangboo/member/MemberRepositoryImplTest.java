package com.bongjangboo.jangboo.member;

import com.bongjangboo.member.domain.Member;
import com.bongjangboo.member.domain.MemberRepository;
import com.bongjangboo.member.domain.MemberStatus;
import com.bongjangboo.auth.domain.oauth.SocialProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Member 저장 및 조회 통합 테스트")
    void save_and_find() {
        // given
        Member member = Member.createNewMember(
                "홍길동",
                "길동이",
                SocialProvider.GOOGLE,
                "social-123"
        );
        // when
        Member saved = memberRepository.save(member);

        // then
        assertThat(saved.getId()).isNotNull();

        Member found = memberRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getNickname()).isEqualTo("길동이");
        assertThat(found.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("Member 프로필 등록 통합 테스트")
    void change_profile() {
        //given
        Member member = Member.createNewMember(
                "홍길동",
                "길동이",
                SocialProvider.GOOGLE,
                "social-123"
        );
    }
}

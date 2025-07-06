package com.example.jangboo.member.infra.persistence;

import com.example.jangboo.member.domain.Member;
import com.example.jangboo.member.domain.MemberRepository;
import com.example.jangboo.member.domain.SocialProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;


    @Override
    public Member save(Member member) {
        MemberJpaEntity memberJpaEntity = MemberJpaEntity.from(member);
        MemberJpaEntity saved = memberJpaRepository.save(memberJpaEntity);
        return saved.toDomain();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id)
                .map(MemberJpaEntity::toDomain);
    }

    @Override
    public boolean existsBySocial(SocialProvider socialProvider, String socialId) {
        return memberJpaRepository.existsBySocialProviderAndSocialId(socialProvider, socialId);
    }
}

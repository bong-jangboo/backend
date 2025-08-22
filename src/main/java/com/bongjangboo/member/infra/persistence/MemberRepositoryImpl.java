package com.bongjangboo.member.infra.persistence;

import com.bongjangboo.member.domain.Member;
import com.bongjangboo.member.domain.MemberRepository;
import com.bongjangboo.auth.domain.oauth.SocialProvider;
import com.bongjangboo.member.domain.vo.Email;
import com.bongjangboo.member.domain.vo.PhoneNumber;
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

    @Override
    public Optional<Member> findBySocial(SocialProvider socialProvider, String socialId) {
        return memberJpaRepository.findBySocialProviderAndSocialId(socialProvider,socialId)
                .map(MemberJpaEntity::toDomain);

    }

    @Override
    public boolean existsByEmail(Email email) {
        return memberJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhoneNumber(PhoneNumber phoneNumber) {
        return memberJpaRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean existsByEmailAndIdNot(Email email, Long id) {
        return memberJpaRepository.existsByEmailAndIdNot(email, id);
    }

    @Override
    public boolean existsByPhoneNumberAndIdNot(PhoneNumber phoneNumber, Long id) {
        return memberJpaRepository.existsByPhoneNumberAndIdNot(phoneNumber,id);
    }
}

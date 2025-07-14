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


    /**
     * Persists a Member domain object and returns the saved instance.
     *
     * Converts the given Member to a JPA entity, saves it using the JPA repository, and maps the persisted entity back to the domain model.
     *
     * @param member the Member domain object to save
     * @return the saved Member domain object
     */
    @Override
    public Member save(Member member) {
        MemberJpaEntity memberJpaEntity = MemberJpaEntity.from(member);
        MemberJpaEntity saved = memberJpaRepository.save(memberJpaEntity);
        return saved.toDomain();
    }

    /**
     * Retrieves a member by its unique identifier and maps it to the domain model.
     *
     * @param id the unique identifier of the member
     * @return an {@code Optional} containing the member if found, or empty if not present
     */
    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id)
                .map(MemberJpaEntity::toDomain);
    }

    /**
     * Checks whether a member exists with the specified social provider and social ID.
     *
     * @param socialProvider the social authentication provider
     * @param socialId the unique identifier from the social provider
     * @return true if a member exists with the given social provider and social ID, false otherwise
     */
    @Override
    public boolean existsBySocial(SocialProvider socialProvider, String socialId) {
        return memberJpaRepository.existsBySocialProviderAndSocialId(socialProvider, socialId);
    }
}

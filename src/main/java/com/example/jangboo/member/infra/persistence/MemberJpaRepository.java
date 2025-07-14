package com.example.jangboo.member.infra.persistence;

import com.example.jangboo.member.domain.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {
    /**
 * Checks if a member exists with the given social provider and social ID.
 *
 * @param socialProvider the social authentication provider to check
 * @param socialId the unique identifier from the social provider
 * @return true if a member with the specified social provider and social ID exists, false otherwise
 */
boolean existsBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
}

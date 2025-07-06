package com.example.jangboo.member.infra.persistence;

import com.example.jangboo.member.domain.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {
    boolean existsBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
}

package com.bongjangboo.member.infra.persistence;

import com.bongjangboo.member.domain.vo.Email;
import com.bongjangboo.member.domain.vo.PhoneNumber;
import com.bongjangboo.shared.identity.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {
    boolean existsBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
    boolean existsByEmail(Email email);
    boolean existsByPhoneNumber(PhoneNumber phoneNumber);
    boolean existsByEmailAndIdNot(Email email, Long id);
    boolean existsByPhoneNumberAndIdNot(PhoneNumber phoneNumber, Long id);
    Optional<MemberJpaEntity> findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
}

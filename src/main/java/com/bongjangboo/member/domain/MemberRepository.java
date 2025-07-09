package com.bongjangboo.member.domain;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    boolean existsBySocial(SocialProvider socialProvider, String socialId);
}

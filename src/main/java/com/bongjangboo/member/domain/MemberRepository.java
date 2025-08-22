package com.bongjangboo.member.domain;

import com.bongjangboo.member.domain.vo.Email;
import com.bongjangboo.member.domain.vo.PhoneNumber;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    boolean existsBySocial(SocialProvider socialProvider, String socialId);
    boolean existsByEmail(Email email);
    boolean existsByPhoneNumber(PhoneNumber phoneNumber);
    boolean existsByEmailAndIdNot(Email email, Long id);
    boolean existsByPhoneNumberAndIdNot(PhoneNumber phoneNumber, Long id);
    Optional<Member> findBySocial(SocialProvider socialProvider, String socialId);
}

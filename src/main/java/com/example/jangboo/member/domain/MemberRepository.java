package com.example.jangboo.member.domain;

import java.util.Optional;

public interface MemberRepository {
    /**
 * Persists the given member and returns the saved instance.
 *
 * @param member the member to be saved
 * @return the persisted member
 */
Member save(Member member);
    /**
 * Retrieves a member by their unique identifier.
 *
 * @param id the unique identifier of the member
 * @return an {@code Optional} containing the member if found, or empty if not present
 */
Optional<Member> findById(Long id);
    /**
 * Checks whether a member exists with the specified social provider and social ID.
 *
 * @param socialProvider the social authentication provider to check
 * @param socialId the unique identifier assigned by the social provider
 * @return true if a member with the given social credentials exists, false otherwise
 */
boolean existsBySocial(SocialProvider socialProvider, String socialId);
}

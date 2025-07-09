package com.bongjangboo.legacy.oauth.token.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
	Token findByOwnerId(Long ownerId);
}

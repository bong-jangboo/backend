package com.bongjangboo.auth.infra.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity}")
    private long accessTokenValiditySeconds;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValiditySeconds;


}

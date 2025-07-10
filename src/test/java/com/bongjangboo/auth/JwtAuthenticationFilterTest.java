package com.bongjangboo.auth;

import com.bongjangboo.auth.application.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationFilterTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @TestConfiguration
    static class TestSecurityConfig {
        @RestController
        static class TestController {
            @GetMapping("/api/v1/test/secure")
            public String secureEndpoint() {
                return "OK";
            }
        }

        @Bean
        public UserDetailsService userDetailsService() {
            // 테스트용 Mock UserDetailsService
            return username -> {
                if ("test-user".equals(username)) {
                    return new User("test-user", "", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
                }
                throw new UsernameNotFoundException("User not found: " + username);
            };
        }
    }

    @Test
    @DisplayName("유효한 JWT 토큰으로 요청 시, 200 OK를 반환한다")
    void withValidToken_shouldReturnOk() throws Exception {
        // given
        UserDetails userDetails = new User("test-user", "", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);

        // when & then
        mvc.perform(get("/api/v1/test/secure")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    @DisplayName("JWT 토큰 없이 요청 시, 403 Forbidden을 반환한다")
    void withoutToken_shouldReturnForbidden() throws Exception {
        // when & then
        mvc.perform(get("/api/v1/test/secure"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("유효하지 않은 JWT 토큰으로 요청 시, 403 Forbidden을 반환한다")
    void withInvalidToken_shouldReturnForbidden() throws Exception {
        // when & then
        mvc.perform(get("/api/v1/test/secure")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isForbidden());
    }
}

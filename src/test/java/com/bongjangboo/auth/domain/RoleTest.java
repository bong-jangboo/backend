package com.bongjangboo.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Role 도메인 테스트")
class RoleTest {

    @Test
    @DisplayName("모든 Role 값이 정의되어 있다")
    void allRoleValuesAreDefined() {
        // When & Then
        assertThat(Role.values()).hasSize(3);
        assertThat(Role.values()).containsExactly(Role.GUEST, Role.USER, Role.ADMIN);
    }

    @ParameterizedTest
    @EnumSource(Role.class)
    @DisplayName("모든 Role에 대해 Spring Security 형식의 권한을 반환한다")
    void returnSpringSecurityAuthorityFormat(Role role) {
        // When
        String authority = role.getAuthority();

        // Then
        assertThat(authority).startsWith("ROLE_");
        assertThat(authority).isEqualTo("ROLE_" + role.name());
    }

    @Test
    @DisplayName("GUEST 권한은 ROLE_GUEST를 반환한다")
    void guestRoleReturnsRoleGuest() {
        // When
        String authority = Role.GUEST.getAuthority();

        // Then
        assertThat(authority).isEqualTo("ROLE_GUEST");
    }

    @Test
    @DisplayName("USER 권한은 ROLE_USER를 반환한다")
    void userRoleReturnsRoleUser() {
        // When
        String authority = Role.USER.getAuthority();

        // Then
        assertThat(authority).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("ADMIN 권한은 ROLE_ADMIN을 반환한다")
    void adminRoleReturnsRoleAdmin() {
        // When
        String authority = Role.ADMIN.getAuthority();

        // Then
        assertThat(authority).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Role enum의 순서가 권한 레벨을 나타낸다")
    void roleOrderRepresentsAuthorityLevel() {
        // When & Then
        assertThat(Role.GUEST.ordinal()).isLessThan(Role.USER.ordinal());
        assertThat(Role.USER.ordinal()).isLessThan(Role.ADMIN.ordinal());
    }

    @Test
    @DisplayName("Role을 문자열로 변환할 수 있다")
    void canConvertRoleToString() {
        // When & Then
        assertThat(Role.GUEST.toString()).isEqualTo("GUEST");
        assertThat(Role.USER.toString()).isEqualTo("USER");
        assertThat(Role.ADMIN.toString()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("문자열로부터 Role을 생성할 수 있다")
    void canCreateRoleFromString() {
        // When & Then
        assertThat(Role.valueOf("GUEST")).isEqualTo(Role.GUEST);
        assertThat(Role.valueOf("USER")).isEqualTo(Role.USER);
        assertThat(Role.valueOf("ADMIN")).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("존재하지 않는 Role 문자열로 IllegalArgumentException을 발생시킨다")
    void throwIllegalArgumentExceptionForInvalidRoleString() {
        // When & Then
        assertThatThrownBy(() -> Role.valueOf("INVALID_ROLE"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
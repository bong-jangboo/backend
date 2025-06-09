package com.example.jangboo.v2.role.domain;

import com.example.jangboo.v2.organization.domain.UserOrganization;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 역할
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // 유저 - 조직
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_organization_id")
    private UserOrganization userOrganization;

    // 역할 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "role_status")
    private RoleStatus status;

    // 임기 관리
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public UserRole(UserOrganization userOrganization,
                    RoleType roleType,
                    RoleStatus status,
                    LocalDate startDate,
                    LocalDate endDate) {
        this.userOrganization = userOrganization;
        this.roleType = roleType;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public boolean isActive() {
        return status == RoleStatus.ACTIVE;
    }

    public boolean isExpired() {
        return endDate != null && endDate.isBefore(LocalDate.now());
    }

    public void expire(LocalDate date) {
        this.status = RoleStatus.EXPIRED;
        this.endDate = date;
    }




}

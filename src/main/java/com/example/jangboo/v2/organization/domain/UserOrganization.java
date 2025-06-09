package com.example.jangboo.v2.organization.domain;

import com.example.jangboo.users.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_organization",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "organization_id"}))

public class UserOrganization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_organization_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public UserOrganization(Organization organization, User user) {
        this.organization = organization;
        this.user = user;
    }

}

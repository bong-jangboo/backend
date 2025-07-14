package com.example.jangboo.member.infra.persistence;

import com.example.jangboo.member.domain.Member;
import com.example.jangboo.member.domain.MemberStatus;
import com.example.jangboo.member.domain.SocialProvider;
import com.example.jangboo.member.domain.vo.Email;
import com.example.jangboo.member.domain.vo.PhoneNumber;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private Email email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "phone_number"))
    private PhoneNumber phoneNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialProvider socialProvider;

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Sets the creation and update timestamps to the current time before the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the {@code updatedAt} timestamp to the current time before the entity is updated in the database.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Creates a MemberJpaEntity from a domain Member object by copying all relevant fields.
     *
     * @param member the domain Member object to convert
     * @return a new MemberJpaEntity representing the given Member
     */
    public static MemberJpaEntity from(Member member) {
        return new MemberJpaEntity(
                member.getId(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getName(),
                member.getNickname(),
                member.getStatus(),
                member.getSocialProvider(),
                member.getSocialId(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }

    /**
     * Converts this JPA entity into a corresponding domain {@code Member} object.
     *
     * @return a {@code Member} instance with values copied from this entity
     */
    public Member toDomain() {
        return Member.builder()
                .id(id)
                .email(email)
                .phoneNumber(phoneNumber)
                .name(name)
                .nickname(nickname)
                .status(status)
                .socialProvider(socialProvider)
                .socialId(socialId)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

}

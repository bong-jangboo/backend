package com.bongjangboo.member.infra.persistence;

import com.bongjangboo.member.domain.Member;
import com.bongjangboo.member.domain.MemberStatus;
import com.bongjangboo.auth.domain.oauth.SocialProvider;
import com.bongjangboo.member.domain.vo.Email;
import com.bongjangboo.member.domain.vo.PhoneNumber;
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

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Member (도메인) → Entity 변환
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
     * Entity → Member (도메인) 변환
     * builder() 대신 of 사용
     */
    public Member toDomain() {
        return Member.reconstruct(
                id,
                email,
                phoneNumber,
                name,
                nickname,
                status,
                socialProvider,
                socialId,
                createdAt,
                updatedAt
        );
    }

}

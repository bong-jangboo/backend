package com.example.jangboo.member.domain;

import com.example.jangboo.member.domain.vo.Email;
import com.example.jangboo.member.domain.vo.PhoneNumber;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = "id")
public class Member {

    private Long id;
    private Email email;
    private PhoneNumber phoneNumber;
    private String name;
    private String nickname;
    private MemberStatus status;
    private final SocialProvider socialProvider;
    private final String socialId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Member Aggregate
     *
     * <p>주의: Builder는 테스트/fixture 용도로만 사용하세요.
     * 실제 회원 생성은 createNewMember() 팩토리 메서드를 통해 진행.</p>
     */
    @Builder
    public Member(
            Long id,
            Email email,
            PhoneNumber phoneNumber,
            String name,
            String nickname,
            MemberStatus status,
            SocialProvider socialProvider,
            String socialId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.nickname = nickname;
        this.status = status;
        this.socialProvider = socialProvider;
        this.socialId = socialId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 회원가입용 도메인 팩토리 메서드
     */
    public static Member createNewMember(
            String name,
            String nickname,
            SocialProvider socialProvider,
            String socialId
    ) {
        return Member.builder()
                .id(null) // 생성 시에는 null
                .email(null) // 생성 시에는 null
                .phoneNumber(null) // 생성 시에는 null
                .name(name)
                .nickname(nickname)
                .status(MemberStatus.ACTIVE) // 가입 시 ACTIVE
                .socialProvider(socialProvider)
                .socialId(socialId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }



    /**
     * 프로필 수정
     */
    public void updateProfile(String nickname) {
        if (!isActive()) {
            throw new IllegalStateException("DEACTIVATED 상태에서는 프로필 수정이 불가합니다.");
        }
        this.nickname = nickname;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 이메일 등록 (소셜가입을 통해 최초 가입 후, 선택 사항)
     */
    public void registerEmail(Email email) {
        if (this.email != null) {
            throw new IllegalStateException("이미 이메일이 등록된 회원입니다.");
        }
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 핸드폰 번호 등록 (소셜가입을 통해 최초 가입 후, 선택 사항)
     */
    public void registerPhoneNumber(PhoneNumber phoneNumber) {
        if (this.phoneNumber != null) {
            throw new IllegalStateException("이미 전화번호가 등록된 회원입니다.");
        }
        this.phoneNumber = phoneNumber;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 휴면 전환
     */
    public void sleep() {
        if (!isActive()) {
            throw new IllegalStateException("휴면 전환은 ACTIVE 상태에서만 가능합니다.");
        }
        this.status = MemberStatus.SLEEP;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 탈퇴
     */
    public void deactivate() {
        this.status = MemberStatus.DEACTIVATED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 활성 여부
     */
    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }

}

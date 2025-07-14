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
     * Constructs a Member aggregate with the specified attributes.
     *
     * <p>Note: The builder is intended for testing or fixture purposes only.
     * Use the {@link #createNewMember(String, String, SocialProvider, String)} factory method for actual member creation.</p>
     *
     * @param id the unique identifier of the member
     * @param email the member's email address, or null if not registered
     * @param phoneNumber the member's phone number, or null if not registered
     * @param name the member's name
     * @param nickname the member's nickname
     * @param status the current status of the member
     * @param socialProvider the social provider associated with the member
     * @param socialId the unique identifier from the social provider
     * @param createdAt the timestamp when the member was created
     * @param updatedAt the timestamp when the member was last updated
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
     * Creates a new Member instance for registration with default ACTIVE status and no contact information.
     *
     * @param name the member's name
     * @param nickname the member's nickname
     * @param socialProvider the social authentication provider
     * @param socialId the unique identifier from the social provider
     * @return a new Member with null id, email, and phone number, and current timestamps
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
     * Updates the member's nickname if the member is active.
     *
     * @param nickname the new nickname to set
     * @throws IllegalStateException if the member is not in ACTIVE status
     */
    public void updateProfile(String nickname) {
        if (!isActive()) {
            throw new IllegalStateException("DEACTIVATED 상태에서는 프로필 수정이 불가합니다.");
        }
        this.nickname = nickname;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Registers an email address for the member if none is currently set.
     *
     * @param email the email address to register
     * @throws IllegalStateException if an email is already registered for this member
     */
    public void registerEmail(Email email) {
        if (this.email != null) {
            throw new IllegalStateException("이미 이메일이 등록된 회원입니다.");
        }
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Registers a phone number for the member if none is already set.
     *
     * @param phoneNumber the phone number to register
     * @throws IllegalStateException if a phone number is already registered for this member
     */
    public void registerPhoneNumber(PhoneNumber phoneNumber) {
        if (this.phoneNumber != null) {
            throw new IllegalStateException("이미 전화번호가 등록된 회원입니다.");
        }
        this.phoneNumber = phoneNumber;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Transitions the member's status to SLEEP if currently ACTIVE.
     *
     * @throws IllegalStateException if the member is not in ACTIVE status.
     */
    public void sleep() {
        if (!isActive()) {
            throw new IllegalStateException("휴면 전환은 ACTIVE 상태에서만 가능합니다.");
        }
        this.status = MemberStatus.SLEEP;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Deactivates the member and updates the last modified timestamp.
     *
     * Changes the member's status to {@code DEACTIVATED} and sets {@code updatedAt} to the current time.
     */
    public void deactivate() {
        this.status = MemberStatus.DEACTIVATED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if the member is currently active.
     *
     * @return {@code true} if the member's status is ACTIVE; {@code false} otherwise.
     */
    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }

}

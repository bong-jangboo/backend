package com.bongjangboo.member.domain;

import com.bongjangboo.member.domain.vo.Email;
import com.bongjangboo.member.domain.vo.PhoneNumber;
import com.bongjangboo.member.exception.MemberErrorCode;
import com.bongjangboo.shared.exception.BusinessException;
import lombok.*;

import java.time.LocalDateTime;

@Getter
//@EqualsAndHashCode(of = "id")
// 영속화 이전에 id 는 null 이고 컬렉션 사용 등 문제가 있음
// 또한 대부분의 경우 참조 동일성으로 충분 함
// 필요할 경우 도메인 메서드로 동등성을 비교
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 생성자 막고 정적 팩토리 메서드로만 Member 생성 허용
@Builder // 프로덕션 코드에서는 builder()로 객체 생성 x -> 정적 팩토리 메서드를 통해서만 생성
public class Member {

    private Long id;
    private Email email;
    private PhoneNumber phoneNumber;
    private String name;
    // TODO nickname VO로 변경
    private String nickname;
    private MemberStatus status;
    private final SocialProvider socialProvider;
    private final String socialId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 모든 필드를 지정하여 Member 객체 생성 (테스트, 마이그레이션, 복원 등 특수 목적용)
     *
     * @param id
     * @param email
     * @param phoneNumber
     * @param name
     * @param nickname
     * @param status
     * @param socialProvider
     * @param socialId
     * @param createdAt
     * @param updatedAt
     * @return
     */
    public static Member reconstruct(
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

    /**
     * 회원가입용 도메인 팩토리 메서드
     * 팩토리 메서드에서 또다른 생성패턴인 빌더를 쓰는게 이상함
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
     * 이메일 등록 (소셜가입을 통해 최초 가입 후, social provider가 정보 제공, 선택 사항)
     */
    public void registerEmail(Email email) {
        checkIfActive();
        if (this.email != null) {
            throw new BusinessException(MemberErrorCode.EMAIL_ALREADY_REGISTERED);
        }
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 이메일 업데이트 ( 가입 이후 이메일 업데이트를 원할 때)
     */
    public void updateEmail(Email email) {
        checkIfActive();
        if (this.email == null) {
            throw new BusinessException(MemberErrorCode.MEMBER_EMAIL_NOT_REGISTERED);
        }
        if (this.email.equals(email)) {
            return; // 같은 이메일인 경우 업데이트 하지 않음
        }
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }



    /**
     * 핸드폰 번호 등록 (소셜가입을 통해 최초 가입 후, social provider가 정보 제공, 선택 사항)
     */
    public void registerPhoneNumber(PhoneNumber phoneNumber) {
        checkIfActive();
        if (this.phoneNumber != null) {
            throw new BusinessException(MemberErrorCode.PHONE_ALREADY_REGISTERED);
        }
        this.phoneNumber = phoneNumber;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 핸드폰 번호 업데이트
     */
    public void updatePhoneNumber(PhoneNumber phoneNumber) {
        checkIfActive();
        if (this.phoneNumber == null) {
            throw new BusinessException(MemberErrorCode.MEMBER_PHONE_NOT_REGISTERED);
        }
        if (this.phoneNumber.equals(phoneNumber)) {
            return;
        }
        this.phoneNumber = phoneNumber;
        this.updatedAt = LocalDateTime.now();
    }


    /**
     * 닉네임 등록
     */
    public void registerNickname(String nickname) {
        checkIfActive();

        this.nickname = nickname;
        this.updatedAt = LocalDateTime.now();

    }

    /**
     * 닉네임 업데이트
     * 25.7.29 pr review
     * 현재 nickname의 타입이 String 이라 nickname에 대한 정책이나 검증을 위한 검증코드가 도메인 클래스에 포함되어야함
     * nickname을 vo로 만들고 캡슐화
     */
    public void updateNickname(String nickname) {
        checkIfActive();
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new BusinessException(MemberErrorCode.NICKNAME_INVALID_FORMAT);
        }

        if (this.nickname.equals(nickname)) {
            return;
        }
        this.nickname = nickname;
        this.updatedAt = LocalDateTime.now();
    }


    /**
     * 휴면 전환
     */
    public void sleep() {
        if (!isActive()) {
            throw new BusinessException(MemberErrorCode.SLEEP_ONLY_ACTIVE);
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


    private void checkIfActive() {
        if(!isActive()) {
            throw new BusinessException(MemberErrorCode.CANNOT_UPDATE_DEACTIVATED);
        }
    }

}

package com.bongjangboo.member.application.service;

import com.bongjangboo.member.application.command.*;
import com.bongjangboo.member.domain.Member;
import com.bongjangboo.member.domain.MemberRepository;
import com.bongjangboo.shared.identity.SocialProvider;
import com.bongjangboo.member.domain.event.MemberRegisteredEvent;
import com.bongjangboo.member.exception.MemberErrorCode;
import com.bongjangboo.shared.event.DomainEventPublisher;
import com.bongjangboo.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberApplicationService {

    private final MemberRepository memberRepository;
    private final DomainEventPublisher eventPublisher;

    /**
     * 회원 등록
     * kakao 소셜 로그인 구현 예정
     */
    @Transactional
    public Member registerMember(RegisterMemberCommand command) {
        if(memberRepository.existsBySocial(command.getSocialProvider(), command.getSocialId())){
            throw new BusinessException(MemberErrorCode.DUPLICATE_SOCIAL_ID);
        }

        Member member = Member.createNewMember(
                command.getName(),
                command.getNickname(),
                command.getSocialProvider(),
                command.getSocialId()
        );

        Member saved = memberRepository.save(member);

        eventPublisher.publish(
                MemberRegisteredEvent.builder()
                        .memberId(member.getId())
                        .socialProvider(member.getSocialProvider())
                        .socialId(member.getSocialId())
                        .email(member.getEmail())
                        .registeredAt(member.getCreatedAt())
                        .build()
        );

        return saved;
    }

    /**
     * 프로필 수정 (닉네임, 이메일, 전화번호)
     * 닉네임 (현재는 중복 허용 -> 추가 정책 도입시 변경 예정)
     * 이메일, 전화번호는 중복 불가능
     */
    @Transactional
    public Member updateProfile(UpdateProfileCommand command) {
        Member member = findMemberById(command.getMemberId());
        // TODO : 다중 필드 에러 집계 반환 구현 후 도입
        // null 체크 후 선택적 업데이트 (PATCH 방식)
        if (command.getNickname() != null) {
            member.updateNickname(command.getNickname());
        }
        if (command.getEmail() != null) {
            if(memberRepository.existsByEmailAndIdNot(command.getEmail(), command.getMemberId())) {
                throw new BusinessException(MemberErrorCode.EMAIL_ALREADY_IN_USE);
            }
            member.updateEmail(command.getEmail());
        }
        if (command.getPhoneNumber() != null) {
            if(memberRepository.existsByPhoneNumberAndIdNot(command.getPhoneNumber(), command.getMemberId())) {
                throw new BusinessException(MemberErrorCode.PHONE_ALREADY_IN_USE);
            }
            member.updatePhoneNumber(command.getPhoneNumber());
        }

        return memberRepository.save(member);  // 수정된 Member 반환
    }


    /**
     * 이메일 등록
     */
    @Transactional
    public void registerEmail(RegisterEmailCommand command) {
        Member member = findMemberById(command.getMemberId());

        if(memberRepository.existsByEmail(command.getEmail())){
            throw new BusinessException(MemberErrorCode.EMAIL_ALREADY_IN_USE);
        }
        member.registerEmail(command.getEmail());
        memberRepository.save(member);
    }

    /**
     * 전화번호 등록
     */
    @Transactional
    public void registerPhoneNumber(RegisterPhoneNumberCommand command) {
        Member member = findMemberById(command.getMemberId());

        if(memberRepository.existsByPhoneNumber(command.getPhoneNumber())){
            throw new BusinessException(MemberErrorCode.PHONE_ALREADY_IN_USE);
        }
        member.registerPhoneNumber(command.getPhoneNumber());
        memberRepository.save(member);
    }

    /**
     * 탈퇴
     */
    @Transactional
    public void deactivateMember(DeactivateMemberCommand command) {
        Member member = findMemberById(command.getMemberId());
        member.deactivate();
        memberRepository.save(member);
    }

    /**
     * 휴면 처리
     */
    @Transactional
    public void sleepMember(SleepMemberCommand command) {
        Member member = findMemberById(command.getMemberId());
        member.sleep();
        memberRepository.save(member);
    }

    /**
     * 멤버 조회
     */
    @Transactional(readOnly = true)
    public Member getMemberProfile(Long memberId) {
        return findMemberById(memberId);
    }

    /**
     * social 정보로 멤버 조회
     * @param provider
     * @param socialId
     * @return
     */
    @Transactional(readOnly = true)
    public Member getMemberBySocial(SocialProvider provider, String socialId) {
        return memberRepository.findBySocial(provider, socialId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
    }


    /**
     * 내부 헬퍼 메서드
     * @param memberId
     * @return
     */
    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}

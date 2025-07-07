package com.example.jangboo.member.application.service;

import com.example.jangboo.member.application.command.*;
import com.example.jangboo.member.domain.Member;
import com.example.jangboo.member.domain.MemberRepository;
import com.example.jangboo.member.domain.event.MemberRegisteredEvent;
import com.example.jangboo.member.exception.MemberErrorCode;
import com.example.jangboo.shared.event.DomainEventPublisher;
import com.example.jangboo.shared.exception.BusinessException;
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
     */
    @Transactional
    public Long registerMember(RegisterMemberCommand command) {
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

        return saved.getId();
    }

    /**
     * 프로필 수정 (현재는 수정사항에 닉네임 정도만)
     */
    @Transactional
    public void updateProfile(UpdateProfileCommand command) {
        Member member = memberRepository.findById(command.getMemberId())
                .orElseThrow(()->new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.updateProfile(command.getNickName());
        memberRepository.save(member);
    }

    /**
     * 이메일 등록
     */
    @Transactional
    public void registerEmail(RegisterEmailCommand command) {
        Member member = memberRepository.findById(command.getMemberId())
                .orElseThrow(()->new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.registerEmail(command.getEmail());
        memberRepository.save(member);
    }

    /**
     * 전화번호 등록
     */
    @Transactional
    public void registerPhoneNumber(RegisterPhoneNumberCommand command) {
        Member member = memberRepository.findById(command.getMemberId())
                .orElseThrow(()->new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.registerPhoneNumber(command.getPhoneNumber());
        memberRepository.save(member);
    }

    /**
     * 탈퇴
     */
    @Transactional
    public void deactivateMember(DeactivateMemberCommand command) {
        Member member = memberRepository.findById(command.getMemberId())
                .orElseThrow(()->new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.deactivate();
        memberRepository.save(member);
    }


}

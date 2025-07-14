package com.example.jangboo.member.application.service;

import com.example.jangboo.member.application.command.*;
import com.example.jangboo.member.domain.Member;
import com.example.jangboo.member.domain.MemberRepository;
import com.example.jangboo.member.domain.event.MemberRegisteredEvent;
import com.example.jangboo.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberApplicationService {

    private final MemberRepository memberRepository;
    private final DomainEventPublisher eventPublisher;

    /**
     * Registers a new member using social credentials.
     *
     * Checks for an existing member with the same social provider and social ID, throwing an {@code IllegalStateException} if found. If not, creates and saves a new member, publishes a {@code MemberRegisteredEvent}, and returns the new member's ID.
     *
     * @param command the registration details including name, nickname, social provider, and social ID
     * @return the ID of the newly registered member
     * @throws IllegalStateException if a member with the given social credentials already exists
     */
    @Transactional
    public Long registerMember(RegisterMemberCommand command) {
        if(memberRepository.existsBySocial(command.getSocialProvider(), command.getSocialId())){
            throw new IllegalStateException("이미 가입된 소셜 계정입니다.");
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
     * Updates a member's profile information, currently limited to changing the nickname.
     *
     * @param command the command containing the member ID and new nickname
     * @throws IllegalArgumentException if the member does not exist
     */
    @Transactional
    public void updateProfile(UpdateProfileCommand command) {
        Member member = memberRepository.findById(command.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("회원이 존재하지 않습니다."));

        member.updateProfile(command.getNickName());
        memberRepository.save(member);
    }

    /**
     * Registers or updates the email address for a member.
     *
     * Throws an IllegalArgumentException if the member does not exist.
     */
    @Transactional
    public void registerEmail(RegisterEmailCommand command) {
        Member member = memberRepository.findById(command.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("회원이 존재하지 않습니다."));
        member.registerEmail(command.getEmail());
        memberRepository.save(member);
    }

    /**
     * Registers or updates the phone number for a member.
     *
     * Updates the phone number of the member identified by the provided command. Throws an IllegalArgumentException if the member does not exist.
     */
    @Transactional
    public void registerPhoneNumber(RegisterPhoneNumberCommand command) {
        Member member = memberRepository.findById(command.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("회원이 존재하지 않습니다."));
        member.registerPhoneNumber(command.getPhoneNumber());
        memberRepository.save(member);
    }

    /**
     * Deactivates a member account by marking the member as deactivated.
     *
     * Throws an IllegalArgumentException if the member does not exist.
     */
    @Transactional
    public void deactivateMember(DeactivateMemberCommand command) {
        Member member = memberRepository.findById(command.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("회원이 존재하지 않습니다."));
        member.deactivate();
        memberRepository.save(member);
    }


}

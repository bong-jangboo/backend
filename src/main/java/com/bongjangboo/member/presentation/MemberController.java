package com.bongjangboo.member.presentation;

import com.bongjangboo.member.application.command.RegisterMemberCommand;
import com.bongjangboo.member.application.command.UpdateProfileCommand;
import com.bongjangboo.member.application.service.MemberApplicationService;
import com.bongjangboo.member.domain.Member;
import com.bongjangboo.member.presentation.dto.request.RegisterMemberRequest;
import com.bongjangboo.member.presentation.dto.request.UpdateProfileRequest;
import com.bongjangboo.member.presentation.dto.response.MemberProfileResponse;
import com.bongjangboo.member.presentation.dto.response.RegisterMemberResponse;
import com.bongjangboo.shared.response.ApiResponse;
import com.bongjangboo.shared.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Validated
public class MemberController {
    private final MemberApplicationService memberApplicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<RegisterMemberResponse>> registerMember(
            // TODO: 나중에 @AuthenticationPrincipal 추가
            @Valid @RequestBody RegisterMemberRequest request
    ) {
        RegisterMemberCommand command = request.toCommand();
        Member member = memberApplicationService.registerMember(command);
        RegisterMemberResponse response = RegisterMemberResponse.from(member);
        return ResponseUtil.success(response);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<MemberProfileResponse>> getMemberProfile(
            @PathVariable Long id
    ) {
        Member member = memberApplicationService.getMemberProfile(id);
        return ResponseUtil.success(MemberProfileResponse.from(member));
    }

    @PatchMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<MemberProfileResponse>> updateMemberProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        UpdateProfileCommand command = request.toCommand(id);
        Member updatedMember = memberApplicationService.updateProfile(command);
        MemberProfileResponse response = MemberProfileResponse.from(updatedMember);
        return ResponseUtil.success(response);
    }
}

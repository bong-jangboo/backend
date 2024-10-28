package com.example.jangboo.accountBook.presentation;

import com.example.jangboo.accountBook.application.AccountBookService;
import com.example.jangboo.accountBook.domain.AccountBookStatus;
import com.example.jangboo.accountBook.dto.in.ApproveAccountBookListRequestDto;
import com.example.jangboo.accountBook.dto.in.ApproveAccountBookRequestDto;
import com.example.jangboo.accountBook.dto.in.CreateAccountBookRequestDto;
import com.example.jangboo.accountBook.dto.out.AccountBookDetailResponseDto;
import com.example.jangboo.accountBook.dto.out.AccountBookResponseDto;
import com.example.jangboo.accountBook.dto.out.ApproveAccountBookListResponseDto;
import com.example.jangboo.accountBook.vo.in.CreateAccountBookRequestVo;
import com.example.jangboo.accountBook.vo.out.AccountBookDetailResponseVo;
import com.example.jangboo.accountBook.vo.out.AccountBookResponseVo;
import com.example.jangboo.accountBook.vo.out.ApproveAccountBookListResponseVo;
import com.example.jangboo.auth.controller.dto.Info.CurrentUserInfo;
import com.example.jangboo.global.dto.ResultDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account-books")
public class AccountBookController {

    private final AccountBookService accountBookService;

    //장부 등록하기
    @Operation(summary = "장부 등록하기", tags = {"장부"})
    @PostMapping
    public ResponseEntity<ResultDto<String>> createAccountBook(@RequestBody CreateAccountBookRequestVo createAccountBookRequestVo,
                                                               @AuthenticationPrincipal CurrentUserInfo info) {
        CreateAccountBookRequestDto createAccountBookRequestDto = CreateAccountBookRequestDto.builder()
                .userId(info.userId())
                .receiptId(createAccountBookRequestVo.getReceiptId())
                .deptId(info.deptId())
                .transactionId(createAccountBookRequestVo.getTransactionId())
                .docNum(createAccountBookRequestVo.getDocNum())
                .createdAt(createAccountBookRequestVo.getCreatedAt())
                .title(createAccountBookRequestVo.getTitle())
                .content(createAccountBookRequestVo.getContent())
                .amount(createAccountBookRequestVo.getAmount())
                .build();

        accountBookService.createAccountBook(createAccountBookRequestDto);

        return ResponseEntity.ok(ResultDto.of(200, "장부 등록 성공", null));
    }

    //장부 리스트 조회
    @Operation(summary = "장부 리스트 조회", tags = {"장부"})
    @GetMapping
    public ResponseEntity<ResultDto<Page<AccountBookResponseVo>>> getAccountBookList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        PageRequest pageable = PageRequest.of(page - 1, pageSize);

        Page<AccountBookResponseDto> accountBooks = accountBookService.getAccountBookList(AccountBookStatus.valueOf(status), fromDate, toDate, pageable);

        Page<AccountBookResponseVo> accountBookResponseVos = accountBooks.map(AccountBookResponseDto::toVo);

        return ResponseEntity.ok(ResultDto.of(200, "장부 조회 성공", accountBookResponseVos));
    }


    // 장부 상세 조회
    @Operation(summary = "장부 상세 조회", tags = {"장부"})
    @GetMapping("/{accountBookId}")
    public ResponseEntity<ResultDto<AccountBookDetailResponseVo>> getAccountBook(
            @PathVariable Long accountBookId) {

        AccountBookDetailResponseDto accountBook = accountBookService.getAccountBook(accountBookId);

        return ResponseEntity.ok(ResultDto.of(200, "장부 조회 성공", accountBook.toVo()));
    }

    // 승인 해야할 장부 조회
    @Operation(summary = "승인 해야할 장부 조회", tags = {"장부"})
    @GetMapping("/approve")
    public ResponseEntity<ResultDto<List<ApproveAccountBookListResponseVo>>> getApproveAccountBookList(@AuthenticationPrincipal CurrentUserInfo info) {

        ApproveAccountBookListRequestDto approveAccountBookListRequestDto = ApproveAccountBookListRequestDto.builder()
                .userId(info.userId())
                .deptId(info.deptId())
                .build();

        List<ApproveAccountBookListResponseDto> accountBooks = accountBookService.getApproveAccountBookList(approveAccountBookListRequestDto);

        List<ApproveAccountBookListResponseVo> accountBookResponseVos = accountBooks.stream()
                .map(ApproveAccountBookListResponseDto::toVo)
                .toList();

        return ResponseEntity.ok(ResultDto.of(200, "승인 해야할 장부 조회 성공", accountBookResponseVos));
    }

    // 장부 승인 및 반려하기
    @Operation(summary = "장부 승인 및 반려하기", tags = {"장부"})
    @PostMapping("/approve/{accountBookId}")
    public ResponseEntity<ResultDto<String>> approveAccountBook(@PathVariable Long accountBookId,
                                                                @RequestParam boolean approval,
                                                                @AuthenticationPrincipal CurrentUserInfo info) {

        ApproveAccountBookRequestDto approveAccountBookRequestDto = ApproveAccountBookRequestDto.builder()
                .accountBookId(accountBookId)
                .approval(approval)
                .userId(info.userId())
                .build();

        accountBookService.approveAccountBook(approveAccountBookRequestDto);

        return ResponseEntity.ok(ResultDto.of(200, "장부 승인/반려 성공", null));
    }
}

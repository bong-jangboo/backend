package com.example.legacy.receipt.controller;

import com.example.legacy.auth.controller.dto.Info.CurrentUserInfo;
import com.example.legacy.global.dto.ResultDto;
import com.example.legacy.receipt.controller.dto.response.ReceiptResponse;
import com.example.legacy.receipt.service.ReceiptService;
import com.example.legacy.receipt.service.dto.response.ReceiptUrlAndIdResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/receipt")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }


    @GetMapping("")
    public ResponseEntity<ResultDto<ReceiptResponse>> getReceipts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate,desc") String sort,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @AuthenticationPrincipal CurrentUserInfo userInfo) {

        // 유효성 검사: fromDate가 toDate보다 이후일 경우 예외 발생
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이후일 수 없습니다.");
        }

        Long deptId = userInfo.deptId();
        ReceiptResponse response = receiptService.getReceipt(deptId, page, size, sort, fromDate, toDate);
        return ResponseEntity.ok().body(ResultDto.of(200, "영수증 리스트 조회 성공", response));
    }

    @GetMapping("/url")
    public ResponseEntity<ResultDto<ReceiptUrlAndIdResponse>> getReceipts(
        @AuthenticationPrincipal CurrentUserInfo userInfo,
        @RequestParam Long receiptId) {
        return ResponseEntity.ok().body(ResultDto.of(200, "영수증 리스트 조회 성공", receiptService.getReceiptUrlAndId(receiptId)));
    }
}

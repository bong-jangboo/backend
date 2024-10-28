package com.example.jangboo.accountBook.vo.out;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AccountBookDetailResponseVo {
    private Long id;
    private Long accountBookSignId;
    private Long receiptId;
    private Long deptId;
    private Long transactionId;
    private Long docNum;
    private LocalDateTime createdAt;
    private String title;
    private String content;
    private Long amount;
    private String status;

}
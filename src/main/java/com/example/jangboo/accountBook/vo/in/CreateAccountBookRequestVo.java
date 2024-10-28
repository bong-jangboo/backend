package com.example.jangboo.accountBook.vo.in;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CreateAccountBookRequestVo {
    private Long receiptId;
    private Long transactionId;
    private Long docNum;
    private LocalDateTime createdAt;
    private String title;
    private String content;
    private Long amount;
}

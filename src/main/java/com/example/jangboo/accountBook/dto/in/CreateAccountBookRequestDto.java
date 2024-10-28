package com.example.jangboo.accountBook.dto.in;

import com.example.jangboo.accountBook.domain.AccountBookStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class CreateAccountBookRequestDto {
    private Long userId;
    private Long receiptId;
    private Long transactionId;
    private Long deptId;
    private Long docNum;
    private LocalDateTime createdAt;
    private String title;
    private String content;
    private Long amount;
    private AccountBookStatus status;
}

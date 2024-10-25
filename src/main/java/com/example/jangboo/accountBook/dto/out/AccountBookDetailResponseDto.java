package com.example.jangboo.accountBook.dto.out;

import com.example.jangboo.accountBook.domain.AccountBook;
import com.example.jangboo.accountBook.vo.out.AccountBookDetailResponseVo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AccountBookDetailResponseDto {
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

    public static AccountBookDetailResponseDto fromEntity(AccountBook accountBook) {
        return AccountBookDetailResponseDto.builder()
                .id(accountBook.getId())
                .accountBookSignId(accountBook.getAccountBookSign().getId())
                .receiptId(accountBook.getReceiptId())
                .deptId(accountBook.getDeptId())
                .transactionId(accountBook.getTransactionId())
                .docNum(accountBook.getDocNum())
                .createdAt(accountBook.getCreatedAt())
                .title(accountBook.getTitle())
                .content(accountBook.getContent())
                .amount(accountBook.getAmount())
                .status(accountBook.getStatus().name())
                .build();
    }

    public AccountBookDetailResponseVo toVo() {
        return AccountBookDetailResponseVo.builder()
                .id(id)
                .accountBookSignId(accountBookSignId)
                .receiptId(receiptId)
                .deptId(deptId)
                .transactionId(transactionId)
                .docNum(docNum)
                .createdAt(createdAt)
                .title(title)
                .content(content)
                .amount(amount)
                .status(status)
                .build();
    }
}

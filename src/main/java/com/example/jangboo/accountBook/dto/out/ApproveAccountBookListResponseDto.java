package com.example.jangboo.accountBook.dto.out;

import com.example.jangboo.accountBook.domain.AccountBook;
import com.example.jangboo.accountBook.domain.AccountBookSign;
import com.example.jangboo.accountBook.domain.AccountBookStatus;
import com.example.jangboo.accountBook.vo.out.ApproveAccountBookListResponseVo;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApproveAccountBookListResponseDto {
    private Long id;
    private AccountBookSign accountBookSign;
    private Long docNum;
    private String title;
    private Long amount;
    private AccountBookStatus status;

    public static ApproveAccountBookListResponseDto fromEntity(AccountBook accountBook) {
        return ApproveAccountBookListResponseDto.builder()
                .id(accountBook.getId())
                .accountBookSign(accountBook.getAccountBookSign())
                .docNum(accountBook.getDocNum())
                .title(accountBook.getTitle())
                .amount(accountBook.getAmount())
                .status(accountBook.getStatus())
                .build();
    }

    public ApproveAccountBookListResponseVo toVo() {
        return ApproveAccountBookListResponseVo.builder()
                .id(id)
                .accountBookSign(accountBookSign)
                .docNum(docNum)
                .title(title)
                .amount(amount)
                .status(status)
                .build();
    }
}

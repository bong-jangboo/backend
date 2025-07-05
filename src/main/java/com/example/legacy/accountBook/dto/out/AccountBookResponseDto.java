package com.example.legacy.accountBook.dto.out;

import com.example.legacy.accountBook.domain.AccountBook;
import com.example.legacy.accountBook.vo.out.AccountBookResponseVo;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountBookResponseDto {
    private Long id;
    private Long docNum;
    private String title;
    private Long amount;

    public static AccountBookResponseDto fromEntity(AccountBook accountBook) {
        return AccountBookResponseDto.builder()
                .id(accountBook.getId())
                .docNum(accountBook.getDocNum())
                .title(accountBook.getTitle())
                .amount(accountBook.getAmount())
                .build();
    }

    public AccountBookResponseVo toVo() {
        return AccountBookResponseVo.builder()
                .id(id)
                .docNum(docNum)
                .title(title)
                .amount(amount)
                .build();
    }
}

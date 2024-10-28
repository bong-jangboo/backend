package com.example.jangboo.accountBook.vo.out;

import com.example.jangboo.accountBook.domain.AccountBookSign;
import com.example.jangboo.accountBook.domain.AccountBookStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApproveAccountBookListResponseVo {
    private Long id;
    private AccountBookSign accountBookSign;
    private Long docNum;
    private String title;
    private Long amount;
    private AccountBookStatus status;
}

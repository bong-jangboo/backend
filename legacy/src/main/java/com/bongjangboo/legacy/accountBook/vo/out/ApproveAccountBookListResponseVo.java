package com.bongjangboo.legacy.accountBook.vo.out;

import com.bongjangboo.legacy.accountBook.domain.AccountBookSign;
import com.bongjangboo.legacy.accountBook.domain.AccountBookStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApproveAccountBookListResponseVo {
    private Long id;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private AccountBookSign accountBookSign;
    private Long docNum;
    private String title;
    private Long amount;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private AccountBookStatus status;
    private Long deptId;
}

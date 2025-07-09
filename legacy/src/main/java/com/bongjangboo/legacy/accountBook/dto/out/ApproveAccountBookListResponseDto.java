package com.bongjangboo.legacy.accountBook.dto.out;

import com.bongjangboo.legacy.accountBook.domain.AccountBook;
import com.bongjangboo.legacy.accountBook.domain.AccountBookSign;
import com.bongjangboo.legacy.accountBook.domain.AccountBookStatus;
import com.bongjangboo.legacy.accountBook.vo.out.ApproveAccountBookListResponseVo;
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
    private Long deptId;

    public static ApproveAccountBookListResponseDto fromEntity(AccountBook accountBook) {
        return ApproveAccountBookListResponseDto.builder()
                .id(accountBook.getId())
                .accountBookSign(accountBook.getAccountBookSign())
                .docNum(accountBook.getDocNum())
                .title(accountBook.getTitle())
                .amount(accountBook.getAmount())
                .status(accountBook.getStatus())
                .deptId(accountBook.getDeptId())
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
                .deptId(deptId)
                .build();
    }
}

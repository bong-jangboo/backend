package com.bongjangboo.legacy.accountBook.dto.in;

import com.bongjangboo.legacy.accountBook.domain.AccountBookStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApproveAccountBookListRequestDto {

    private AccountBookStatus status;
    private Long userId;
    private Long deptId;

    public ApproveAccountBookListRequestDto(AccountBookStatus status, Long userId, Long deptId) {
        this.status = status;
        this.userId = userId;
        this.deptId = deptId;
    }
}

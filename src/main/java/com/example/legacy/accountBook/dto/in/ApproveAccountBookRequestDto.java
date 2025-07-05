package com.example.legacy.accountBook.dto.in;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApproveAccountBookRequestDto {
    private Long accountBookId;
    private boolean approval;
    private Long userId;
}

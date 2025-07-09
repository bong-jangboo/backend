package com.bongjangboo.legacy.accountBook.dto.in;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateAccountBookRequestDto {
    private Long docNum;
    private LocalDateTime createdAt;
    private String title;
    private String content;
    private Long amount;
}

package com.bongjangboo.legacy.accountBook.vo.in;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateAccountBookRequestVo {
    private Long docNum;
    private LocalDateTime createdAt;
    private String title;
    private String content;
    private Long amount;
}

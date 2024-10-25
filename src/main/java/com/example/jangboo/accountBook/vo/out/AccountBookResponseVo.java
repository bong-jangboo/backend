package com.example.jangboo.accountBook.vo.out;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountBookResponseVo {
    private Long id;
    private Long docNum;
    private String title;
    private Long amount;
}

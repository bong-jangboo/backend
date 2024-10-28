package com.example.jangboo.accountBook.application;

import com.example.jangboo.accountBook.domain.AccountBookStatus;
import com.example.jangboo.accountBook.dto.in.ApproveAccountBookListRequestDto;
import com.example.jangboo.accountBook.dto.in.ApproveAccountBookRequestDto;
import com.example.jangboo.accountBook.dto.in.CreateAccountBookRequestDto;
import com.example.jangboo.accountBook.dto.out.AccountBookDetailResponseDto;
import com.example.jangboo.accountBook.dto.out.AccountBookResponseDto;
import com.example.jangboo.accountBook.dto.out.ApproveAccountBookListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountBookService {

    void createAccountBook(CreateAccountBookRequestDto requestDto);

    Page<AccountBookResponseDto> getAccountBookList(AccountBookStatus status,
                                                    LocalDateTime fromDate,
                                                    LocalDateTime toDate,
                                                    Pageable pageable);

    AccountBookDetailResponseDto getAccountBook(Long accountBookId);

    List<ApproveAccountBookListResponseDto> getApproveAccountBookList(ApproveAccountBookListRequestDto requestDto);

    void approveAccountBook(ApproveAccountBookRequestDto requestDto);

}

package com.bongjangboo.legacy.accountBook.application;

import com.bongjangboo.legacy.accountBook.domain.AccountBookStatus;
import com.bongjangboo.legacy.accountBook.dto.in.ApproveAccountBookListRequestDto;
import com.bongjangboo.legacy.accountBook.dto.in.ApproveAccountBookRequestDto;
import com.bongjangboo.legacy.accountBook.dto.in.CreateAccountBookRequestDto;
import com.bongjangboo.legacy.accountBook.dto.in.UpdateAccountBookRequestDto;
import com.bongjangboo.legacy.accountBook.dto.out.AccountBookDetailResponseDto;
import com.bongjangboo.legacy.accountBook.dto.out.AccountBookResponseDto;
import com.bongjangboo.legacy.accountBook.dto.out.ApproveAccountBookListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountBookService {

    void createAccountBook(CreateAccountBookRequestDto requestDto);

    void updateAccountBook(Long accountBookId, UpdateAccountBookRequestDto requestDto);

    Page<AccountBookResponseDto> getAccountBookList(AccountBookStatus status,
                                                    LocalDateTime fromDate,
                                                    LocalDateTime toDate,
                                                    Pageable pageable,
                                                    Long deptId);

    AccountBookDetailResponseDto getAccountBook(Long accountBookId);

    List<ApproveAccountBookListResponseDto> getApproveAccountBookList(ApproveAccountBookListRequestDto requestDto);

    void approveAccountBook(ApproveAccountBookRequestDto requestDto);
    boolean isTransactionNotExists(Long transactionId);

}

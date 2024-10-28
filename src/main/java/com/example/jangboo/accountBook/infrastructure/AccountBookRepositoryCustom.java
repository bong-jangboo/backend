package com.example.jangboo.accountBook.infrastructure;

import com.example.jangboo.accountBook.domain.AccountBook;
import com.example.jangboo.accountBook.domain.AccountBookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AccountBookRepositoryCustom {
    Page<AccountBook> findAccountBooksByConditions(AccountBookStatus status, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable, Long deptId);
}
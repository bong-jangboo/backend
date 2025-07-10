package com.bongjangboo.legacy.accountBook.infrastructure;

import com.bongjangboo.legacy.accountBook.domain.AccountBook;
import com.bongjangboo.legacy.accountBook.domain.AccountBookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AccountBookRepositoryCustom {
    Page<AccountBook> findAccountBooksByConditions(AccountBookStatus status, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable, Long deptId);
}
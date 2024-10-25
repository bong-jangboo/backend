package com.example.jangboo.accountBook.infrastructure;


import com.example.jangboo.accountBook.domain.AccountBook;
import com.example.jangboo.accountBook.domain.AccountBookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long> {

    @Query("SELECT a FROM AccountBook a WHERE a.status = :status AND a.createdAt BETWEEN :fromDate AND :toDate")
    Page<AccountBook> findByStatusAndDateRange(@Param("status") AccountBookStatus status,
                                               @Param("fromDate") LocalDateTime fromDate,
                                               @Param("toDate") LocalDateTime toDate,
                                               Pageable pageable);

    List<AccountBook> findByStatusAndAccountBookSign_VicePresidentApprovalFalse(AccountBookStatus status);

    List<AccountBook> findByStatusAndAccountBookSign_VicePresidentApprovalTrueAndAccountBookSign_PresidentApprovalFalse(AccountBookStatus status);

    List<AccountBook> findByStatusAndAccountBookSign_PresidentApprovalTrueAndAccountBookSign_VicePresidentApprovalTrueAndAccountBookSign_AuditApprovalFalse(AccountBookStatus status);

}

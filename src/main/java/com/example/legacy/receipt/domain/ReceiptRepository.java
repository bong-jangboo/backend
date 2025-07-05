package com.example.legacy.receipt.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ReceiptRepository extends CrudRepository<Receipt, Long> {
    Page<Receipt> findByDeptId(Long id, Pageable pageable);

    @Query("SELECT r FROM Receipt r JOIN r.receiptDetails d WHERE r.deptId = :deptId AND d.transactionDate BETWEEN :from AND :to")
    Page<Receipt> findByDeptIdAndTransactionDateBetween(
            @Param("deptId") Long deptId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );
}

package com.example.jangboo.transaction.domain.repository;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jangboo.transaction.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	Optional<Transaction> findTopByDeptIdOrderByDateDescTimeDesc(Long deptId);
	List<Transaction> findTop5ByDeptIdOrderByDateDescTimeDesc(Long deptId);
	Page<Transaction> findByDeptIdAndDateBetween(Long deptId, LocalDate startDate, LocalDate endDate, Pageable pageable);
	List<Transaction> findByDescriptionContainingAndLableAndDeptIdAndAmount(String name,String lable,Long deptId,String amount);
	Optional<Transaction> findByAmountAndDateAndTime(String amount, LocalDate date, LocalTime time);
	Page<Transaction> findByDeptIdAndReceiptIdIsNotNull(Long deptId, Pageable pageable);
}

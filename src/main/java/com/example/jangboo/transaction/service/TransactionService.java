package com.example.jangboo.transaction.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.jangboo.transaction.domain.Transaction;
import com.example.jangboo.transaction.domain.TransactionRepository;

@Service
public class TransactionService {
	private final TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public LocalDate findLatestUpdatedTransactionDate(Long userId) {
		return transactionRepository.findTopByAccountOwnerIdOrderByDateDesc(userId)
			.map(Transaction::getDate)
			.orElseGet(this::getFistDayOfYear);
	}

	private LocalDate getFistDayOfYear() {
		return LocalDate.of(LocalDate.now().getYear(), 1, 1);
	}
}

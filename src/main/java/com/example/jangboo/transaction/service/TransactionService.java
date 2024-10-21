package com.example.jangboo.transaction.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jangboo.oauth.client.response.MockTransactionResponse;
import com.example.jangboo.transaction.domain.Transaction;
import com.example.jangboo.transaction.domain.TransactionRepository;

@Service
public class TransactionService {
	private final TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public LocalDateTime findLatestUpdatedTransactionDateTime(Long userId) {
		return transactionRepository
			.findTopByAccountOwnerIdOrderByDateDescTimeDesc(userId)
			.map(transaction -> LocalDateTime.of(transaction.getDate(), transaction.getTime()))
			.orElseGet(this::getFirstDayOfYear);
	}

	private LocalDateTime getFirstDayOfYear() {
		return LocalDateTime.of(LocalDate.now().withDayOfYear(1), LocalTime.MIDNIGHT); // 1월 1일 00:00:00
	}

	@Transactional
	public MockTransactionResponse saveTransactions (MockTransactionResponse transaction,Long userId){
		List<Transaction> transactions = transaction.transactions().stream()
			.map(t -> Transaction.builder()
				.transactionType(t.transactionType())
				.amount(t.amount())
				.date((LocalDate)parseDateTime(t.date(),"yyyy-MM-dd"))
				.time((LocalTime)parseDateTime(t.time(),"HH:mm:ss"))
				.description(t.description())
				.accountOwnerId(userId)
				.build()
			)
			.collect(Collectors.toList());

		transactionRepository.saveAll(transactions);

		return transaction;
	}

	public Object parseDateTime(String input, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

		if (pattern.equals("yyyy-MM-dd")) {
			return LocalDate.parse(input, formatter);
		} else if (pattern.equals("HH:mm:ss")) {
			return LocalTime.parse(input, formatter);
		} else if (pattern.equals("yyyy-MM-dd HH:mm:ss")) {
			return LocalDateTime.parse(input, formatter);
		} else {
			throw new IllegalArgumentException("지원하지 않는 포맷입니다.");
		}
	}
}

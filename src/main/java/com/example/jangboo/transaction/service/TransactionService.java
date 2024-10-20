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

	public LocalDate findLatestUpdatedTransactionDate(Long userId) {
		return transactionRepository.findTopByAccountOwnerIdOrderByDateDesc(userId)
			.map(Transaction::getDate)
			.orElseGet(this::getFistDayOfYear);
	}

	private LocalDate getFistDayOfYear() {
		return LocalDate.of(LocalDate.now().getYear(), 1, 1);
	}

	@Transactional
	public MockTransactionResponse saveTransactions (MockTransactionResponse transaction,Long userId){
		// Transaction 엔티티 리스트 생성 및 저장
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

		// 저장된 모든 거래내역을 repository에 저장
		transactionRepository.saveAll(transactions);

		// 필요한 응답 객체 반환
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

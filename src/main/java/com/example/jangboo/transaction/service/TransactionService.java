package com.example.jangboo.transaction.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jangboo.oauth.client.response.MockTransactionResponse;
import com.example.jangboo.transaction.controller.dto.response.Info.TransactionInfo;
import com.example.jangboo.transaction.controller.dto.response.TransactionsResponse;
import com.example.jangboo.transaction.domain.Transaction;
import com.example.jangboo.transaction.domain.TransactionRepository;

@Service
public class TransactionService {
	private final TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public LocalDateTime findLatestUpdatedTransactionDateTime(Long deptId) {
		return transactionRepository
			.findTopByDeptIdOrderByDateDescTimeDesc(deptId)
			.map(transaction -> LocalDateTime.of(transaction.getDate(), transaction.getTime()))
			.orElseGet(this::getFirstDayOfYear);
	}

	private LocalDateTime getFirstDayOfYear() {
		return LocalDateTime.of(LocalDate.now().withDayOfYear(1), LocalTime.MIDNIGHT); // 1월 1일 00:00:00
	}

	@Transactional
	public void saveTransactions (MockTransactionResponse transaction,Long userId,Long deptId){
		System.out.println(transaction.transactions().get(0).transaction_type());
		List<Transaction> transactions = transaction.transactions().stream()
			.map(t -> Transaction.builder()
				.transactionType(t.transaction_type())
				.amount(t.amount())
				.date((LocalDate)parseDateTime(t.date(),"yyyy-MM-dd"))
				.time((LocalTime)parseDateTime(t.time(),"HH:mm:ss"))
				.description(t.description())
				.balance(t.balance())
				.lable(t.lable())
				.accountOwnerId(userId)
				.deptId(deptId)
				.build()
			)
			.collect(Collectors.toList());

		transactionRepository.saveAll(transactions);
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

	@Transactional(readOnly = true)
	public String getLatestBalance(Long DeptId) {
		return transactionRepository
			.findTopByDeptIdOrderByDateDescTimeDesc(DeptId)
			.orElseThrow(() -> new IllegalStateException("잔액을 가져오지 못했습니다."))
			.getBalance();
	}

	@Transactional(readOnly = true)
	public TransactionsResponse getTop5Transactions(Long accountOwnerId) {
		List<Transaction> transactions = transactionRepository.findTop5ByDeptIdOrderByDateDescTimeDesc(accountOwnerId);

		return new TransactionsResponse(
			transactions.stream().map(
				transaction -> TransactionInfo.builder()
					.transaction_type(transaction.getTransactionType())
					.lable(transaction.getLable())
					.date(transaction.getDate().toString())
					.amount(transaction.getAmount())
					.description(transaction.getDescription())
					.balance(transaction.getBalance())
					.time(transaction.getTime().toString())
					.build()
			).toList()
		);
	}
}

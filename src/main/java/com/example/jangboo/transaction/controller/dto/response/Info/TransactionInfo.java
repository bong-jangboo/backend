package com.example.jangboo.transaction.controller.dto.response.Info;

import com.example.jangboo.transaction.domain.Transaction;

import lombok.Builder;

@Builder
public record TransactionInfo(
	String lable,
	String amount,
	String transaction_type,
	String balance,
	String date,
	String time,
	String description
) {
	public static TransactionInfo from(Transaction transaction) {
		return TransactionInfo.builder()
			.lable(transaction.getLable())
			.amount(transaction.getAmount())
			.transaction_type(transaction.getTransactionType())
			.balance(transaction.getBalance())
			.date(transaction.getDate().toString())
			.time(transaction.getTime().toString())
			.description(transaction.getDescription()).build();
	}
}

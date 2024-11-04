package com.example.jangboo.transaction.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "transaction",indexes = {
	@Index(name = "idx_transaction_date", columnList = "date")}
)
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private Long id;

	@Column(name="account_owner_id")
	private Long accountOwnerId;

	@Column(name="lable")
	private String lable;

	@Column(name="amount")
	private String amount;

	@Column(name="transactionType")
	private String transactionType;

	@Column(name="balance")
	private String balance;

	@Getter
	@Column(name="date")
	private LocalDate date;

	@Getter
	@Column(name="time")
	private LocalTime time;

	@Column(name="description")
	private String description;

	@Builder
	public Transaction(Long accountOwnerId, String lable, String amount, String transactionType, String balance, LocalDate date, LocalTime time, String description) {
		this.accountOwnerId = accountOwnerId;
		this.lable = lable;
		this.amount = amount;
		this.transactionType = transactionType;
		this.balance = balance;
		this.date = date;
		this.time = time;
		this.description = description;
	}
}

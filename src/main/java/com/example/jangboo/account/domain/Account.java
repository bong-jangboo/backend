package com.example.jangboo.account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="account")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="account_id")
	private Long id;

	@Column(name="fintech_use_num")
	private String fintechUseNum;

	@Column(name="owner_id")
	private Long ownerId;

	@Builder
	public Account(String fintechUseNum, Long ownerId) {
		this.fintechUseNum = fintechUseNum;
		this.ownerId = ownerId;
	}
}

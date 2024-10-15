package com.example.jangboo.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jangboo.account.domain.Account;
import com.example.jangboo.account.domain.AccountRepository;

@Service
public class AccountService {
	@Autowired
	private final AccountRepository accountRepository;

	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Transactional
	public void registerAccount(Long userId, String fintechUseNum) {
		accountRepository.save(
			Account.builder()
				.fintechUseNum(fintechUseNum)
				.ownerId(userId)
				.build()
		);
	}
}

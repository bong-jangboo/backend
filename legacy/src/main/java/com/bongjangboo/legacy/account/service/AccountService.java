package com.bongjangboo.legacy.account.service;

import java.util.NoSuchElementException;

import com.bongjangboo.legacy.account.domain.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bongjangboo.legacy.account.domain.Account;

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

	public String getFintechUseNum(Long userId) {
		return accountRepository.findByOwnerId(userId)
			.orElseThrow(()-> new NoSuchElementException("존재하지 않는 계정아이디 입니다."))
			.getFintechUseNum();
	}

	public boolean isNotExist(Long userId) {
		return accountRepository.findByOwnerId(userId).isEmpty();
	}
}

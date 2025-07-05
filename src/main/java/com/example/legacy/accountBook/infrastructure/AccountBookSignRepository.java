package com.example.legacy.accountBook.infrastructure;

import com.example.legacy.accountBook.domain.AccountBookSign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBookSignRepository extends JpaRepository<AccountBookSign, Long> {

}

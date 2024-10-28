package com.example.jangboo.accountBook.infrastructure;

import com.example.jangboo.accountBook.domain.AccountBookSign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBookSignRepository extends JpaRepository<AccountBookSign, Long> {

}

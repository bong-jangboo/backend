package com.bongjangboo.legacy.accountBook.infrastructure;

import com.bongjangboo.legacy.accountBook.domain.AccountBookSign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBookSignRepository extends JpaRepository<AccountBookSign, Long> {

}

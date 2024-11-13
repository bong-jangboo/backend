package com.example.jangboo.accountBook.infrastructure;


import com.example.jangboo.accountBook.domain.AccountBook;
import com.example.jangboo.accountBook.domain.AccountBookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long>, AccountBookRepositoryCustom {

    List<AccountBook> findByStatusAndAccountBookSign_VicePresidentApprovalFalse(AccountBookStatus status);

    List<AccountBook> findByStatusAndAccountBookSign_VicePresidentApprovalTrueAndAccountBookSign_PresidentApprovalFalse(AccountBookStatus status);

    List<AccountBook> findByStatusAndAccountBookSign_PresidentApprovalTrueAndAccountBookSign_VicePresidentApprovalTrueAndAccountBookSign_AuditApprovalFalse(AccountBookStatus status);

}

package com.example.legacy.accountBook.infrastructure;

import com.example.legacy.accountBook.domain.AccountBook;
import com.example.legacy.accountBook.domain.AccountBookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long>, AccountBookRepositoryCustom {

    List<AccountBook> findByStatusAndDeptIdAndAccountBookSign_VicePresidentApprovalFalse(AccountBookStatus status, Long deptId);

    List<AccountBook> findByStatusAndDeptIdAndAccountBookSign_VicePresidentApprovalTrueAndAccountBookSign_PresidentApprovalFalse(AccountBookStatus status, Long deptId);

    List<AccountBook> findByStatusAndDeptIdInAndAccountBookSign_PresidentApprovalTrueAndAccountBookSign_VicePresidentApprovalTrueAndAccountBookSign_AuditApprovalFalse(AccountBookStatus status, List<Long> deptIds);

    Optional<AccountBook> findByTransactionId(Long transactionId);
}

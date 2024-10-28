package com.example.jangboo.accountBook.application;

import com.example.jangboo.accountBook.domain.AccountBook;
import com.example.jangboo.accountBook.domain.AccountBookSign;
import com.example.jangboo.accountBook.domain.AccountBookStatus;
import com.example.jangboo.accountBook.dto.in.ApproveAccountBookListRequestDto;
import com.example.jangboo.accountBook.dto.in.ApproveAccountBookRequestDto;
import com.example.jangboo.accountBook.dto.in.CreateAccountBookRequestDto;
import com.example.jangboo.accountBook.dto.out.AccountBookDetailResponseDto;
import com.example.jangboo.accountBook.dto.out.AccountBookResponseDto;
import com.example.jangboo.accountBook.dto.out.ApproveAccountBookListResponseDto;
import com.example.jangboo.accountBook.infrastructure.AccountBookRepository;
import com.example.jangboo.accountBook.infrastructure.AccountBookSignRepository;
import com.example.jangboo.role.domain.Role;
import com.example.jangboo.role.domain.RoleRepository;
import com.example.jangboo.role.domain.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookServiceImpl implements AccountBookService {

    private final AccountBookRepository accountBookRepository;
    private final AccountBookSignRepository accountBookSignRepository;
    private final RoleRepository roleRepository;

    //장부 등록하기
    @Override
    @Transactional
    public void createAccountBook(CreateAccountBookRequestDto requestDto) {

        // Role 엔티티에서 userId에 해당하는 Role을 가져옴
        Role userRole = roleRepository.findByStudentId(requestDto.getUserId()).stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("역할을 찾을 수 없습니다."));

        // RoleType이 AUDITOR가 아닐 경우 예외 처리
        if (userRole.getRole() != RoleType.AUDITOR) {
            throw new IllegalArgumentException("장부 등록은 감사(AUDITOR)만 가능합니다.");
        }

        AccountBook accountBook = AccountBook.builder()
                .receiptId(requestDto.getReceiptId())
                .deptId(requestDto.getDeptId())
                .transactionId(requestDto.getTransactionId())
                .docNum(requestDto.getDocNum())
                .createdAt(requestDto.getCreatedAt())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .amount(requestDto.getAmount())
                .status(AccountBookStatus.UNAUDITED)
                .build();

        AccountBookSign accountBookSign = AccountBookSign.builder()
                .presidentApproval(false)
                .vicePresidentApproval(false)
                .auditApproval(false)
                .build();

        accountBook.setAccountBookSign(accountBookSign);

        accountBookRepository.save(accountBook);
        accountBookSignRepository.save(accountBookSign);
    }

    //장부 리스트 조회
    @Override
    @Transactional(readOnly = true)
    public Page<AccountBookResponseDto> getAccountBookList(AccountBookStatus status,
                                                           LocalDateTime fromDate,
                                                           LocalDateTime toDate,
                                                           Pageable pageable) {

        Page<AccountBook> accountBooks = accountBookRepository.findByStatusAndDateRange(status,
                fromDate,
                toDate,
                pageable);

        return accountBooks.map(AccountBookResponseDto::fromEntity);
    }

    //장부 상세 조회
    @Override
    @Transactional(readOnly = true)
    public AccountBookDetailResponseDto getAccountBook(Long accountBookId) {

        AccountBook accountBook = accountBookRepository.findById(accountBookId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장부가 존재하지 않습니다."));

        return AccountBookDetailResponseDto.fromEntity(accountBook);
    }

    //승인 해야할 장부 조회
    @Override
    @Transactional(readOnly = true)
    public List<ApproveAccountBookListResponseDto> getApproveAccountBookList(ApproveAccountBookListRequestDto requestDto) {

        List<Role> roles = roleRepository.findByStudentId(requestDto.getUserId());

        RoleType roleType = roles.stream()
                .map(Role::getRole)
                .findFirst()  // RoleType은 여러 개? 최상위 하나만?
                .orElseThrow(() -> new IllegalArgumentException("역할이 존재하지 않음"));

        List<AccountBook> accountBooks;

        if (roleType == RoleType.VICE_PRESIDENT) {
            //장부 상태가 미감사인 것 중 부회장 승인이 안 된 것
            accountBooks = accountBookRepository.findByStatusAndAccountBookSign_VicePresidentApprovalFalse(AccountBookStatus.UNAUDITED);
        } else if (roleType == RoleType.PRESIDENT) {
            //장부 상태가 미감사인 것 중 부회장 승인은 됐고 회장 승인은 안 된 것
            accountBooks = accountBookRepository.findByStatusAndAccountBookSign_VicePresidentApprovalTrueAndAccountBookSign_PresidentApprovalFalse(AccountBookStatus.UNAUDITED);
        } else if (roleType == RoleType.AUDITOR) {
            //장부 상태가 미감사인 것 중 부회장, 회장 승인은 됐고 감사 승인은 안 된 것
            accountBooks = accountBookRepository.findByStatusAndAccountBookSign_PresidentApprovalTrueAndAccountBookSign_VicePresidentApprovalTrueAndAccountBookSign_AuditApprovalFalse(AccountBookStatus.UNAUDITED);
        } else {
            throw new IllegalArgumentException("승인 권한이 없는 역할입니다.");
        }

        return accountBooks.stream()
                .map(ApproveAccountBookListResponseDto::fromEntity)
                .toList();
    }

    //장부 승인 및 반려하기
    @Override
    @Transactional
    public void approveAccountBook(ApproveAccountBookRequestDto requestDto) {

        // AccountBook 찾기
        AccountBook accountBook = accountBookRepository.findById(requestDto.getAccountBookId())
                .orElseThrow(() -> new IllegalArgumentException("해당 장부가 존재하지 않습니다."));

        // AccountBookSign 찾기
        AccountBookSign accountBookSign = accountBook.getAccountBookSign();

        // 역할 찾기
        RoleType roleType = roleRepository.findByStudentId(requestDto.getUserId())
                .stream()
                .map(Role::getRole)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("역할이 존재하지 않음"));

        // 승인 여부 업데이트
        // 상위 직급의 승인 여부는 이미 승인 해야할 장부 조회 API에서 걸러졌기 때문에 예외 처리를 안 함
        if (roleType == RoleType.VICE_PRESIDENT) {
            accountBookSign.setVicePresidentApproval(requestDto.isApproval());
        } else if (roleType == RoleType.PRESIDENT) {
            accountBookSign.setPresidentApproval(requestDto.isApproval());
        } else if (roleType == RoleType.AUDITOR) {
            accountBookSign.setAuditApproval(requestDto.isApproval());
        } else {
            throw new IllegalArgumentException("승인 권한이 없는 역할입니다.");
        }

        // 승인 여부가 하나라도 false일 경우 처리
        if (!requestDto.isApproval()) {
            // AccountBook 상태를 UNAPPROVED로 변경
            accountBook.setStatus(AccountBookStatus.UNAPPROVED);

            // AccountBookSign의 모든 승인 값들을 false로 되돌림
            accountBookSign.setPresidentApproval(false);
            accountBookSign.setVicePresidentApproval(false);
            accountBookSign.setAuditApproval(false);
        }

        // 변경사항 저장
        accountBookSignRepository.save(accountBookSign);
        accountBookRepository.save(accountBook);
    }
}

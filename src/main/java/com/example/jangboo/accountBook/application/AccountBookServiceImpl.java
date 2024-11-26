package com.example.jangboo.accountBook.application;

import com.example.jangboo.accountBook.domain.AccountBook;
import com.example.jangboo.accountBook.domain.AccountBookSign;
import com.example.jangboo.accountBook.domain.AccountBookStatus;
import com.example.jangboo.accountBook.dto.in.ApproveAccountBookListRequestDto;
import com.example.jangboo.accountBook.dto.in.ApproveAccountBookRequestDto;
import com.example.jangboo.accountBook.dto.in.CreateAccountBookRequestDto;
import com.example.jangboo.accountBook.dto.in.UpdateAccountBookRequestDto;
import com.example.jangboo.accountBook.dto.out.AccountBookDetailResponseDto;
import com.example.jangboo.accountBook.dto.out.AccountBookResponseDto;
import com.example.jangboo.accountBook.dto.out.ApproveAccountBookListResponseDto;
import com.example.jangboo.accountBook.infrastructure.AccountBookRepository;
import com.example.jangboo.accountBook.infrastructure.AccountBookSignRepository;
import com.example.jangboo.role.domain.Role;
import com.example.jangboo.role.domain.RoleRepository;
import com.example.jangboo.role.domain.RoleType;
import com.example.jangboo.role.service.RoleService;
import com.example.jangboo.univ.domain.Univ;
import com.example.jangboo.univ.domain.UnivRepository;
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
    private final UnivRepository univRepository;
    private final RoleService roleService;

    //장부 등록하기
    @Override
    @Transactional
    public void createAccountBook(CreateAccountBookRequestDto requestDto) {


        RoleType userRole = roleService.getCurrentRole(requestDto.getUserId());
        // RoleType이 MANAGER가 아닐 경우 예외 처리
        if (userRole != RoleType.MANAGER) {
            throw new IllegalArgumentException("장부 등록은 총무(MANAGER)만 가능합니다.");
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

    @Override
    @Transactional
    public void updateAccountBook(Long accountBookId, UpdateAccountBookRequestDto updateRequestDto) {

        // AccountBook 찾기
        AccountBook accountBook = accountBookRepository.findById(accountBookId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장부가 존재하지 않습니다."));

        // 필드 업데이트
        accountBook.updateAccountBook(updateRequestDto.getDocNum(),
                updateRequestDto.getCreatedAt(),
                updateRequestDto.getTitle(),
                updateRequestDto.getContent(),
                updateRequestDto.getAmount());

        // 상태를 UNAUDITED로 변경
        accountBook.setStatus(AccountBookStatus.UNAUDITED);

        // AccountBookSign 상태 초기화
        AccountBookSign accountBookSign = accountBook.getAccountBookSign();
        accountBookSign.setPresidentApproval(false);
        accountBookSign.setVicePresidentApproval(false);
        accountBookSign.setAuditApproval(false);

        accountBookRepository.save(accountBook);
        accountBookSignRepository.save(accountBookSign);
    }


    //장부 리스트 조회
    @Override
    @Transactional(readOnly = true)
    public Page<AccountBookResponseDto> getAccountBookList(AccountBookStatus status,
                                                           LocalDateTime fromDate,
                                                           LocalDateTime toDate,
                                                           Pageable pageable,
                                                           Long deptId) {
        Page<AccountBook> accountBooks = accountBookRepository.findAccountBooksByConditions(status,
                fromDate,
                toDate,
                pageable,
                deptId);
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

    // 승인해야할 장부 조회
    @Override
    @Transactional(readOnly = true)
    public List<ApproveAccountBookListResponseDto> getApproveAccountBookList(
            ApproveAccountBookListRequestDto requestDto) {

        RoleType roleType = roleService.getCurrentRole(requestDto.getUserId());

        List<AccountBook> accountBooks;

        if (roleType == RoleType.VICE_PRESIDENT) {
            // 부회장: 학과에 맞는 미감사 장부 중 부회장 승인이 안 된 것
            accountBooks = accountBookRepository.findByStatusAndDeptIdAndAccountBookSign_VicePresidentApprovalFalse(
                    AccountBookStatus.UNAUDITED, requestDto.getDeptId());
        } else if (roleType == RoleType.PRESIDENT) {
            // 회장: 학과에 맞는 미감사 장부 중 부회장 승인 됐고 회장 승인이 안 된 것
            accountBooks = accountBookRepository.findByStatusAndDeptIdAndAccountBookSign_VicePresidentApprovalTrueAndAccountBookSign_PresidentApprovalFalse(
                    AccountBookStatus.UNAUDITED, requestDto.getDeptId());
        } else if (roleType == RoleType.AUDITOR) {
            // 감사: 단과대 소속 모든 학과의 미감사 장부 중 부회장, 회장 승인 됐고 감사 승인이 안 된 것
            List<Long> childDeptIds = univRepository.findByParentId(requestDto.getDeptId()).stream()
                    .map(Univ::getId)
                    .toList();
            accountBooks = accountBookRepository.findByStatusAndDeptIdInAndAccountBookSign_PresidentApprovalTrueAndAccountBookSign_VicePresidentApprovalTrueAndAccountBookSign_AuditApprovalFalse(
                    AccountBookStatus.UNAUDITED, childDeptIds);
        } else {
            throw new IllegalArgumentException("승인 권한이 없는 역할입니다.");
        }

        return accountBooks.stream()
                .map(ApproveAccountBookListResponseDto::fromEntity)
                .toList();
    }


    // 장부 승인 및 반려하기
    @Override
    @Transactional
    public void approveAccountBook(ApproveAccountBookRequestDto requestDto) {

        // AccountBook 찾기
        AccountBook accountBook = accountBookRepository.findById(requestDto.getAccountBookId())
                .orElseThrow(() -> new IllegalArgumentException("해당 장부가 존재하지 않습니다."));

        // AccountBookSign 찾기
        AccountBookSign accountBookSign = accountBook.getAccountBookSign();

        // 역할 찾기
        RoleType roleType = roleService.getCurrentRole(requestDto.getUserId());

        // 승인 여부 업데이트
        if (roleType == RoleType.VICE_PRESIDENT) {
            accountBookSign.setVicePresidentApproval(requestDto.isApproval());
        } else if (roleType == RoleType.PRESIDENT) {
            accountBookSign.setPresidentApproval(requestDto.isApproval());
        } else if (roleType == RoleType.AUDITOR) {
            accountBookSign.setAuditApproval(requestDto.isApproval());
            // AUDITOR가 승인할 경우, 모든 승인 상태가 true라면 AccountBook의 상태를 PUBLIC으로 변경
            if (requestDto.isApproval()
                    && accountBookSign.getPresidentApproval()
                    && accountBookSign.getVicePresidentApproval()) {
                accountBook.setStatus(AccountBookStatus.PUBLIC);
            }
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

    @Override
    public boolean isTransactionNotExists(Long transactionId){
        return accountBookRepository.findByTransactionId(transactionId).isEmpty();
    }
}

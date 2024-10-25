package com.example.jangboo.accountBook.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class AccountBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_book_id")
    private Long id;

    @Comment("장부 서명 id")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_book_sign_id")
    private AccountBookSign accountBookSign;

    @Comment("영수증 id")
    @Column(nullable = false, name = "receipt_id")
    private Long receiptId;

    @Comment("학과 id")
    @Column(nullable = false, name = "dept_id")
    private Long deptId;

    @Comment("거래내역 id")
    @Column(nullable = false, name = "transaction_id")
    private Long transactionId;

    @Comment("문서 번호")
    @Column(nullable = false, name = "doc_num")
    private Long docNum;

    @Comment("거래일시")
    @Column(nullable = false, name = "transaction_date")
    private LocalDateTime createdAt;

    @Comment("제목")
    @Column(nullable = false, length = 100, name = "title")
    private String title;

    @Comment("내용")
    @Column(nullable = false, length = 100, name = "content")
    private String content;

    @Comment("총 비용")
    @Column(nullable = false, name = "amount")
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private AccountBookStatus status;

    @Builder
    public AccountBook(Long receiptId, Long deptId, Long transactionId, Long docNum, LocalDateTime createdAt, String title, String content, Long amount, AccountBookStatus status) {
        this.receiptId = receiptId;
        this.deptId = deptId;
        this.transactionId = transactionId;
        this.docNum = docNum;
        this.createdAt = createdAt;
        this.title = title;
        this.content = content;
        this.amount = amount;
        this.status = status;
    }

    public void setAccountBookSign(AccountBookSign accountBookSign) {
        this.accountBookSign = accountBookSign;
    }

    public void setStatus(AccountBookStatus status) {
        this.status = status;
    }
}

package com.example.jangboo.accountBook.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class AccountBookSign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_book_sign_id")
    private Long id;

    @Comment("회장 승인 여부")
    @Column(nullable = false, name = "president_approval")
    private Boolean presidentApproval;

    @Comment("부회장 승인 여부")
    @Column(nullable = false, name = "vice_president_approval")
    private Boolean vicePresidentApproval;

    @Comment("감사 승인 여부")
    @Column(nullable = false, name = "audit_approval")
    private Boolean auditApproval;

    @Builder
    public AccountBookSign(Boolean presidentApproval, Boolean vicePresidentApproval, Boolean auditApproval) {
        this.presidentApproval = presidentApproval != null ? presidentApproval : false;
        this.vicePresidentApproval = vicePresidentApproval != null ? vicePresidentApproval : false;
        this.auditApproval = auditApproval != null ? auditApproval : false;
    }

    public void setPresidentApproval(Boolean approval) {
        this.presidentApproval = approval;
    }

    public void setVicePresidentApproval(Boolean approval) {
        this.vicePresidentApproval = approval;
    }

    public void setAuditApproval(Boolean approval) {
        this.auditApproval = approval;
    }
}

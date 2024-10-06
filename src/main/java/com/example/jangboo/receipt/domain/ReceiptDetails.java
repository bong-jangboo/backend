package com.example.jangboo.receipt.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ReceiptDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "details_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    @Column(name = "appcode")
    private String appcode;

    @Column(name = "store")
    private String store;

    @Column(name = "ammount")
    private int amount;

    @Column(name = "transaction_date_time")
    private LocalDateTime transactionDate;

    @Builder
    public ReceiptDetails(String appcode, String store, int amount, LocalDateTime transactionDate) {
        this.appcode = appcode;
        this.store = store;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }
}

package com.example.jangboo.receipt.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name="receipt")
public class Receipt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Long id;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "appcode")
    private String appcode;

    @Column(name = "store")
    private String store;

    @Column(name = "ammount")
    private int amount;

    @Column(name = "transaction_date_time")
    private LocalDateTime transactionDate;

    @Column(name = "img_url")
    private String receiptImgUrl;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceiptItem> receiptItems = new ArrayList<>();

    @Builder
    public Receipt(Long deptId, String appcode, String store, int amount, LocalDateTime transactionDate, String receiptImgUrl) {
        this.deptId = deptId;
        this.appcode = appcode;
        this.store = store;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.receiptImgUrl = null;
    }

    public void addReceiptItem(ReceiptItem item) {
        receiptItems.add(item);
        if (item.getReceipt() != this) {
            item.linkReceipt(this);
        }
    }

}

package com.example.jangboo.receipt.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ReceiptItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;

    @Column(name = "name")
    private String name;

    @Column(name = "count")
    private int count;

    @Column(name = "price")
    private int price;


    public void linkReceipt(Receipt receipt) {
        this.receipt = receipt;
        if(!receipt.getReceiptItems().contains(this)) {
            receipt.addReceiptItem(this);
        }
    }

}

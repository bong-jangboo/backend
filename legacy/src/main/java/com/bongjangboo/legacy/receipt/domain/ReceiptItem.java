package com.bongjangboo.legacy.receipt.domain;

import com.bongjangboo.legacy.receipt.controller.dto.ocr.OcrResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public static ReceiptItem of(OcrResult.PurchasedItem item) {
        ReceiptItem receiptItem = new ReceiptItem();
        receiptItem.name = item.getName();
        receiptItem.count = Integer.parseInt(item.getCount());
        receiptItem.price = Integer.parseInt(item.getPrice());
        return receiptItem;
    }

}

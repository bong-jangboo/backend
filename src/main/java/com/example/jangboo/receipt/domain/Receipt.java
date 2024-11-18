package com.example.jangboo.receipt.domain;

import com.example.jangboo.receipt.controller.dto.ocr.OcrRes;
import com.example.jangboo.receipt.controller.dto.ocr.OcrResult;
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

    @Column(name = "img_url", length = 1024)
    private String receiptImgUrl;

    @OneToOne(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private ReceiptDetails receiptDetails;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceiptItem> receiptItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReceiptStatus receiptStatus;


    @Builder
    public Receipt(Long deptId, String receiptImgUrl) {
        this.deptId = deptId;
        this.receiptImgUrl = receiptImgUrl;
        this.receiptStatus = ReceiptStatus.PEDING;
    }

    public void addReceiptItem(ReceiptItem item) {
        receiptItems.add(item);
        if (item.getReceipt() != this) {
            item.linkReceipt(this);
        }
    }

    @Builder
    public static Receipt of(Long deptId, String imgUrl, OcrRes.OcrResponse ocrResponse){
            Receipt receipt = new Receipt(deptId, imgUrl);
            ReceiptDetails receiptDetails = ReceiptDetails.of(ocrResponse);
            receipt.receiptDetails = receiptDetails;
            receiptDetails.linkReceipt(receipt);

            List<OcrResult.PurchasedItem> purchasedItems = ocrResponse.getItems();
            for (OcrResult.PurchasedItem item : purchasedItems) {
                ReceiptItem receiptItem = ReceiptItem.of(item);
                receipt.addReceiptItem(receiptItem);
            }
            return receipt;
    }

    public void updateReceiptDetails(String appcode, String store, int amount, LocalDateTime transactionDate){
        this.receiptDetails = new ReceiptDetails(appcode, store, amount, transactionDate);
        this.receiptStatus = ReceiptStatus.PROCESSED;
    }

    public void markAsProcessing() {
        this.receiptStatus = ReceiptStatus.PROCESSING;
    }

    public void markAsFailed() {
        this.receiptStatus = ReceiptStatus.FAILED;
    }

}

package com.example.jangboo.receipt.domain;

import com.example.jangboo.receipt.controller.dto.ocr.OcrRes;
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

    @OneToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    @Column(name = "appcode")
    private String appcode;

    @Column(name = "store")
    private String store;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "transaction_date_time")
    private LocalDateTime transactionDate;

    @Builder
    public ReceiptDetails(String appcode, String store, int amount, LocalDateTime transactionDate) {
        this.appcode = appcode;
        this.store = store;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public static ReceiptDetails of(OcrRes.OcrResponse ocrResponse){
        return new ReceiptDetails(
                ocrResponse.getPaymentInfo().getConfirmNum(),
                ocrResponse.getStoreInfo().getStoreName(),
                Integer.parseInt(ocrResponse.getTotalPrice()),
                ocrResponse.getPaymentInfo().getTransactionDateTime()
        );
    }

    // ReceiptDetails 검증 로직
    public boolean isComplete() {
        return amount != null && transactionDate != null;
    }

    public void linkReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
}

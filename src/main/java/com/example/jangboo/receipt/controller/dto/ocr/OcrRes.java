package com.example.jangboo.receipt.controller.dto.ocr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class OcrRes {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OcrResponse {
        private OcrResult.StoreInfoRes storeInfo;
        private OcrResult.PaymentInfoRes paymentInfo;
        private List<OcrResult.PurchasedItem> items;
        private String totalPrice;

        public static OcrResponse of(OcrJsonRes.Result result) {


            return new OcrRes.OcrResponse(
                    OcrResult.StoreInfoRes.of(result.getStoreInfo()),
                    OcrResult.PaymentInfoRes.of(result.getPaymentInfo()),
                    OcrResult.PurchasedItem.of(result.getSubResults()),
                    result.getTotalPrice().getPrice().getText().replaceAll(",", "")
            );
        }
    }
}

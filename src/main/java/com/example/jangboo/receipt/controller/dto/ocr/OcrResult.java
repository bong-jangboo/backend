package com.example.jangboo.receipt.controller.dto.ocr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class OcrResult {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StoreInfoRes {
        private String storeName;

        public static StoreInfoRes of(OcrJsonRes.StoreInfo storeInfo) {
            return new StoreInfoRes(storeInfo.getName().getText());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentInfoRes {
        private String date;
        private String time;
        private String confirmNum;

        public static PaymentInfoRes of(OcrJsonRes.PaymentInfo paymentInfo) {
            return new PaymentInfoRes(
                    paymentInfo.getDate().getText(),
                    paymentInfo.getTime().getText(),
                    paymentInfo.getConfirmNum().getText()
            );
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PurchasedItem {
        private String name;
        private String count;
        private String price;

        public static PurchasedItem of(OcrJsonRes.Item item) {
            return new PurchasedItem(item.getName().getText() != null ? item.getName().getText() : "",
                    item.getCount() != null && item.getCount().getText() != null ? item.getCount().getText() : "",
                    item.getPrice().getPrice().getText() != null ? item.getPrice().getPrice().getText().replaceAll(",", "") : "");
        }
        public static List<PurchasedItem> of(List<OcrJsonRes.SubResult> subResults) {
            return subResults.stream().flatMap(s -> s.getItems().stream()).map(PurchasedItem::of).toList();
        }
    }
}

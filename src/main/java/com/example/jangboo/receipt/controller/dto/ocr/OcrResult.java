package com.example.jangboo.receipt.controller.dto.ocr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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

        public LocalDateTime getTransactionDateTime() {
            String cleanedDate = date.replaceAll("\\(.*\\)", ""); // (일) 같은 요일 제거
            String cleanedTime = time.replaceAll("\\s+", ""); // 모든 공백 제거
            String dateTimeString = cleanedDate + " " + cleanedTime;
            // 포맷 지정
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            try {
                return LocalDateTime.parse(dateTimeString, formatter);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Failed to parse date and time: " + dateTimeString, e);
            }
        }

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

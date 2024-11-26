package com.example.jangboo.receipt.controller.dto.ocr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OcrResult {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StoreInfoRes {
        private String storeName;

        public static StoreInfoRes of(OcrJsonRes.StoreInfo storeInfo) {
            return new StoreInfoRes(
                    Optional.ofNullable(storeInfo)
                            .map(OcrJsonRes.StoreInfo::getName)
                            .map(OcrJsonRes.Name::getText)
                            .orElse(null) // null 허용
            );
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
            if (date == null || time == null) {
                return null;
            }

            String cleanedDate = date.replaceAll("[\\[\\(].*?[\\]\\)]", "").replaceAll("/", "-");
            String cleanedTime = time.replaceAll("\\s+", ""); // 모든 공백 제거
            String dateTimeString = cleanedDate + " " + cleanedTime;

            // 두 가지 포맷 시도: 초가 있는 경우와 없는 경우
            List<DateTimeFormatter> formatters = List.of(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), // 초 있는 포맷
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")    // 초 없는 포맷
            );

            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDateTime.parse(dateTimeString, formatter);
                } catch (DateTimeParseException ignored) {
                    // 포맷이 맞지 않으면 다음 포맷 시도
                }
            }

            // 모든 포맷 시도 실패 시 예외 발생
            throw new IllegalArgumentException("Failed to parse date and time: " + dateTimeString);
        }

        public static PaymentInfoRes of(OcrJsonRes.PaymentInfo paymentInfo) {
            return new PaymentInfoRes(
                    Optional.ofNullable(paymentInfo.getDate())
                                    .map(OcrJsonRes.Date::getText)
                                            .orElse(null),
                    Optional.ofNullable(paymentInfo.getTime())
                            .map(OcrJsonRes.Time::getText)
                            .orElse(null),
                    Optional.ofNullable(paymentInfo.getConfirmNum())
                            .map(OcrJsonRes.ConfirmNum::getText)
                            .orElse(null) // null 허용
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

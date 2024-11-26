package com.example.jangboo.receipt.service;


import com.example.jangboo.receipt.controller.dto.ocr.OcrRes;
import com.example.jangboo.receipt.controller.dto.response.ReceiptDto;
import com.example.jangboo.receipt.controller.dto.response.ReceiptResponse;
import com.example.jangboo.receipt.domain.Receipt;
import com.example.jangboo.receipt.domain.ReceiptDetails;
import com.example.jangboo.receipt.domain.ReceiptRepository;
import com.example.jangboo.receipt.domain.ReceiptStatus;
import com.example.jangboo.receipt.event.ReceiptDetailsSavedEvent;
import com.example.jangboo.receipt.service.dto.response.ReceiptInfoResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReceiptResponse getReceipt(Long deptId, int page, int size, String sort, LocalDate fromDate, LocalDate toDate) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by((sort.split(","))));
        Page<Receipt> receiptPage;

        if(fromDate != null && toDate != null) {

            LocalDateTime from = fromDate.atStartOfDay();
            LocalDateTime to = toDate.plusDays(1).atStartOfDay().minusNanos(1);
            receiptPage = receiptRepository.findByDeptIdAndTransactionDateBetween(deptId, from, to, pageable);
        } else {
            receiptPage = receiptRepository.findByDeptId(deptId, pageable);
        }

        List<ReceiptDto> receiptDtoList = receiptPage.getContent().stream()
                .map(ReceiptDto::from)
                .collect(Collectors.toList());

        return ReceiptResponse.builder()
                .currentPage(receiptPage.getNumber()+1)
                .pageSize(receiptPage.getSize())
                .totalPages(receiptPage.getTotalPages())
                .totalReceipts((int)receiptPage.getTotalElements())
                .receipts(receiptDtoList)
                .build();
    }

    @Transactional
    public void saveOcrReceipt(Long deptId, String imgUrl,OcrRes.OcrResponse ocrResponse) {
        Receipt receipt = Receipt.of(deptId,imgUrl,ocrResponse);
        Receipt savedReceipt = receiptRepository.save(receipt);

        if(receipt.getReceiptDetails().isComplete()){
            savedReceipt.markAsStatus(ReceiptStatus.COMPLETE);
            eventPublisher.publishEvent(new ReceiptDetailsSavedEvent(savedReceipt.getId()));
        } else {
            savedReceipt.markAsStatus(ReceiptStatus.INCOMPLETE);
        }
    }

    public ReceiptInfoResponse getReceiptInfo(Long receiptId){
        Receipt receipt = receiptRepository.findById(receiptId).orElseThrow(() -> new IllegalStateException("영수증을 가져오지 못했습니다."));
        ReceiptDetails receiptDetails = receipt.getReceiptDetails();

        return new ReceiptInfoResponse(receiptId,receiptDetails.getAmount(),receiptDetails.getTransactionDate());
    }
}

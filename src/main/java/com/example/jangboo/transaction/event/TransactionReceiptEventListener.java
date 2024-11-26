package com.example.jangboo.transaction.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.jangboo.file.event.ReceiptUploadedEvent;
import com.example.jangboo.receipt.controller.dto.ocr.OcrRes;
import com.example.jangboo.receipt.event.ReceiptDetailsSavedEvent;
import com.example.jangboo.receipt.service.ReceiptService;
import com.example.jangboo.receipt.service.dto.response.ReceiptInfoResponse;
import com.example.jangboo.transaction.service.TransactionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionReceiptEventListener {
	private final TransactionService transactionService;
	private final ReceiptService receiptService;

	@EventListener
	@Async
	public void handleReceiptDetailSavedEvent(ReceiptDetailsSavedEvent event){
		System.out.println("receiptUploadedEvent:"+event.getReceiptId().toString());
		ReceiptInfoResponse response = receiptService.getReceiptInfo(event.getReceiptId());
		transactionService.updateReceiptId(response);
	}
}

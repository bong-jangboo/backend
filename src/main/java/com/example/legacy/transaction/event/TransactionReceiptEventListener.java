package com.example.legacy.transaction.event;

import com.example.legacy.transaction.service.TransactionService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.legacy.receipt.event.ReceiptDetailsSavedEvent;
import com.example.legacy.receipt.service.ReceiptService;
import com.example.legacy.receipt.service.dto.response.ReceiptInfoResponse;

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

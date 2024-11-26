package com.example.jangboo.receipt.event;

import lombok.Getter;

public class ReceiptDetailsSavedEvent {
	@Getter
	private final Long receiptId;
	
	public ReceiptDetailsSavedEvent(Long receiptId) {
		this.receiptId = receiptId;
	}
}

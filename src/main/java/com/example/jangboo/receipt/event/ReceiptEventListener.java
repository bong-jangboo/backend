package com.example.jangboo.receipt.event;

import com.example.jangboo.file.event.ReceiptUploadedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ReceiptEventListener {

    @EventListener
    public void handleReceiptUploadedEvent(ReceiptUploadedEvent event){

    }
}

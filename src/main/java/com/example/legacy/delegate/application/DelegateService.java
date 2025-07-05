package com.example.legacy.delegate.application;

import com.example.legacy.delegate.dto.DelegateRequestDto;

public interface DelegateService {
	void mandateRole(DelegateRequestDto requestDto, Long currentUserId);
}

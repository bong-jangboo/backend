package com.example.jangboo.delegate.application;

import com.example.jangboo.delegate.dto.DelegateRequestDto;

public interface DelegateService {
	void mandateRole(DelegateRequestDto requestDto, Long currentUserId);
}

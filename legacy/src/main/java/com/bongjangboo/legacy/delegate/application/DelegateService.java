package com.bongjangboo.legacy.delegate.application;

import com.bongjangboo.legacy.delegate.dto.DelegateRequestDto;

public interface DelegateService {
	void mandateRole(DelegateRequestDto requestDto, Long currentUserId);
}

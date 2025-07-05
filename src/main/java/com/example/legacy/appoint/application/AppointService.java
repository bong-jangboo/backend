package com.example.legacy.appoint.application;

import com.example.legacy.appoint.dto.AppointRequestDto;

public interface AppointService {

	void appointVicePresidentOrManager(AppointRequestDto requestDto, Long userId);
}

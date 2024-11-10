package com.example.jangboo.appoint.application;

import com.example.jangboo.appoint.dto.AppointRequestDto;

public interface AppointService {

	void appointVicePresidentOrManager(AppointRequestDto requestDto, Long userId);
}

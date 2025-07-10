package com.bongjangboo.legacy.appoint.application;

import com.bongjangboo.legacy.appoint.dto.AppointRequestDto;

public interface AppointService {

	void appointVicePresidentOrManager(AppointRequestDto requestDto, Long userId);
}

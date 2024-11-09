package com.example.jangboo.global.dto;

import com.example.jangboo.global.enums.ErrorCode;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class ResultDto<D>{
	private final Integer code;
	private final String message;
	private final D data;



	// 실패 응답을 생성하는 메서드
	public static ResultDto<String> fail(ErrorCode err) {
		return new ResultDto<>(err.getHttpStatus().value(), err.getMessage(), err.getErrorCode());
	}
}

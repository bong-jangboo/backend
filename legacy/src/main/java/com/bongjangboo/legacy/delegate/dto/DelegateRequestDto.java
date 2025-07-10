package com.bongjangboo.legacy.delegate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DelegateRequestDto {

	private String name;

	private String number;
}

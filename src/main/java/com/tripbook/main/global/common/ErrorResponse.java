package com.tripbook.main.global.common;

import java.util.ArrayList;
import java.util.List;

import com.tripbook.main.global.enums.ErrorCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
	private int status;
	private List<String> message = new ArrayList<>();
	private String code;

	public ErrorResponse(ErrorCode errorCode) {
		this.status = errorCode.getStatus();
		this.message.add(errorCode.getMessage());
		this.code = errorCode.getErrorCode();
	}
}
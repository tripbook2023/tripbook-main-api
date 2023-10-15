package com.tripbook.main.global.common;

import java.util.ArrayList;
import java.util.List;

import com.tripbook.main.global.enums.ErrorCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "실패에 대한 응답 값")
public class ErrorResponse {
	@Schema(title = "HTTP 상태값")
	private int status;
	@Schema(title = "String 배열형태의 에러메시지들입니다.")
	private List<String> message = new ArrayList<>();
	@Schema(title = "HTTP 상태코드")
	private String code;

	public ErrorResponse(ErrorCode errorCode) {
		this.status = errorCode.getStatus();
		this.message.add(errorCode.getMessage());
		this.code = errorCode.getErrorCode();
	}

	public ErrorResponse(ErrorCode errorCode, String detailMessage) {
		this.status = errorCode.getStatus();
		this.message.add(errorCode.getMessage());
		this.message.add(detailMessage);
		this.code = errorCode.getErrorCode();
	}
}
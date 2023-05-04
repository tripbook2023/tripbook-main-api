package com.tripbook.main.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	NOT_FOUND(404, "COMMON-ERR-404", "PAGE_NOTFOUND"),
	INTER_SERVER_ERROR(500, "COMMON-ERR-500", "INTER_SERVER_ERROR"),
	EMAIL_DUPLICATION(400, "MEMBER-ERR-400", "EMAIL_DUPLICATED"),
	TOKEN_Unauthorized(401, "TOKEN-ERR-401", "INVALID_TOKEN");

	private int status;
	private String errorCode;
	private String message;
}
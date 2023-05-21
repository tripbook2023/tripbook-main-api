package com.tripbook.main.token.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

public class ResponseToken {
	@Builder
	@Getter
	public static class resultInfo {
		private HttpStatus status;
		private List<String> message;
	}
}

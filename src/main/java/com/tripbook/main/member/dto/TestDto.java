package com.tripbook.main.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TestDto {
	@NotEmpty(message = "테스트 빈 값")
	private String test;
}

package com.tripbook.main.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbook.main.auth.dto.ResponseAuth;
import com.tripbook.main.global.common.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Swagger3.0 맵핑을 위한 TempController.
 * 실제 동작하지 않습니다.
 */
@RestController
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

	@Operation(security = {
		@SecurityRequirement(name = "OAUTH")}, summary = "간편로그인 토큰인증", description = "Auth0에서 발급받은 토큰을 Header에 전달한다", responses = {
		@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseAuth.ResultInfo.class))),
		@ApiResponse(responseCode = "400", description = "이메일 중복", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "유효하지 않은 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "CSRF 또는 권한문제로 관리자 문의 필요", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("/login/oauth2/")
	public String tmpAuthReq(
		@Parameter(description =
			"1.iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson-> 모바일 웹 \n\n"
				+ "2.IOS_APP|ANDROID_APP->모바일 앱\n\n"
				+ "3.웹"
			, required = true, name = "User-Agent", in = ParameterIn.HEADER)
		String userAgent) {
		return "Test";
	}

}

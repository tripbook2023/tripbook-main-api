package com.tripbook.main.token.controller;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbook.main.global.common.ErrorResponse;
import com.tripbook.main.global.util.CheckDevice;
import com.tripbook.main.global.util.CustomTokenUtil;
import com.tripbook.main.token.dto.ResponseToken;
import com.tripbook.main.token.dto.TokenInfo;
import com.tripbook.main.token.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/token")
@Tag(name = "JWT", description = "JWT API")
@RequiredArgsConstructor
public class JwtController {
	private final JwtService jwtService;

	@Operation(security = {
		@SecurityRequirement(name = "JWT")}, summary = "토큰 재발급", description = "Header(Authorization) 액세스토큰 재발급을 위한 리프레시토큰 입력", responses = {
		@ApiResponse(responseCode = "200", description = "성공시에 액세스토큰, 리프레시토큰 재발급", content = @Content(schema = @Schema(implementation = TokenInfo.class))),
		@ApiResponse(responseCode = "400", description = "토큰을 찾을 수 없거나, 유효하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PostMapping("/issue")
	public ResponseEntity<Object> tokenReissue(HttpServletRequest request,
		@Parameter(description =
			"1.iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson-> 모바일 웹 \n\n"
				+ "2.IOS_APP|ANDROID_APP->모바일 앱\n\n"
				+ "3.웹"
			, required = true, name = "User-Agent", in = ParameterIn.HEADER)
		String userAgent) {
		//토큰 재발급 진행
		String token = CustomTokenUtil.resolveToken(request);
		if (token == null) {
			ResponseToken.resultInfo result = ResponseToken.resultInfo.builder()
				.status(HttpStatus.BAD_REQUEST)
				.message(Arrays.asList("INVALID_TOKEN")).build();
			return ResponseEntity.badRequest().body(result);
		}
		TokenInfo tokenInfo = jwtService.tokenIssue(token, CheckDevice.checkDevice(request));

		return ResponseEntity.status(HttpStatus.OK).body(tokenInfo);

	}
}

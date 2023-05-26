package com.tripbook.main.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbook.main.auth.dto.AuthResponse;
import com.tripbook.main.auth.service.AuthService;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.global.util.CustomTokenUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Swagger3.0 맵핑을 위한 TempController.
 * 실제 동작하지 않습니다.
 */
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

	private final AuthService authService;

	@GetMapping("/login/oauth2/temp")
	public ResponseEntity<AuthResponse> login(HttpServletRequest request) {
		final String accessToken = CustomTokenUtil.resolveToken(request);

		if (accessToken == null) {
			throw new CustomException.InvalidTokenException(ErrorCode.TOKEN_UNAUTHORIZED.getMessage(),
				ErrorCode.TOKEN_UNAUTHORIZED);
		}

		return ResponseEntity.ok(
			AuthDtoMapper.of(authService.login(accessToken))
		);
	}

}

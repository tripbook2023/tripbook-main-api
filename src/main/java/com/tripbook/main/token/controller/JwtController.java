package com.tripbook.main.token.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbook.main.global.util.CheckDevice;
import com.tripbook.main.global.util.CustomTokenUtil;
import com.tripbook.main.token.dto.TokenInfo;
import com.tripbook.main.token.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/token")
@RequiredArgsConstructor
public class JwtController {
	private final JwtService jwtService;

	@PostMapping("/issue")
	public ResponseEntity<Object> tokenReissue(HttpServletRequest request) {
		//토큰 재발급 진행
		String token = CustomTokenUtil.resolveToken(request);
		if (token == null) {
			return ResponseEntity.badRequest().build();
		}
		TokenInfo tokenInfo = jwtService.tokenIssue(token, CheckDevice.checkDevice(request));

		return ResponseEntity.status(HttpStatus.OK).body(tokenInfo);

	}
}

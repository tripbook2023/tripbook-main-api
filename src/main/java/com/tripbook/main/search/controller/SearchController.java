package com.tripbook.main.search.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbook.main.global.common.ErrorResponse;
import com.tripbook.main.member.dto.ResponseMember;
import com.tripbook.main.member.service.MemberService;
import com.tripbook.main.token.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/search")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Search", description = "Search API")
public class SearchController {
	/**
	 * @TODO 여행소식 검색개발
	 * 1. 인기검색
	 * 2. 연관검색
	 * @TODO 사용자 검색개발
	 * 1. 사용자 검색
	 */
	@Qualifier("mailJwtService")
	private final JwtService mailJwtService;
	private final MemberService memberService;

	@Operation(security = {
		@SecurityRequirement(name = "JWT")},
		summary = "멤버검색", description = "사용자 검색.\n\n birth:yyyy-mm-dd", responses = {
		@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseMember.MemberInfo.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("/member")
	public ResponseEntity<Object> member(Authentication authentication) {
		return null;
	}
}

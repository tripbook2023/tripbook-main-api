package com.tripbook.main.member.controller;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbook.main.member.dto.RequestMember;
import com.tripbook.main.member.dto.ResponseMember;
import com.tripbook.main.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	//회원 등급, 설문조사, MemberDTO
	@PostMapping("/signup")
	public ResponseEntity<Object> memberJoin(@RequestBody @Validated RequestMember.SignupMember requestMember,
		OAuth2AuthenticationToken authentication) {
		memberService.memberCertification(requestMember, authentication.getPrincipal().getAttribute("email"));
		ResponseMember.resultInfo result = ResponseMember.resultInfo.builder().status(HttpStatus.OK)
			.message(Arrays.asList("success"))
			.build();
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	//닉네임 유효성검사
	@PostMapping("/nickname/validate")
	public ResponseEntity<Object> memberNameCheck(
		@RequestBody @Validated RequestMember.SignupNameValidator requestMember) {
		memberService.memberNameValidation(requestMember);
		ResponseMember.resultInfo result = ResponseMember.resultInfo.builder().status(HttpStatus.OK)
			.message(Arrays.asList("success"))
			.build();
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}

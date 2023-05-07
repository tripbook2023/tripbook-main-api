package com.tripbook.main.member.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbook.main.member.dto.RequestMember;
import com.tripbook.main.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	//@TODO Response JWT 전달 필요
	//회원 등급, 설문조사, MemberDTO
	@PostMapping("/signup")
	public String memberJoin(@RequestBody @Validated RequestMember requestMember,
		OAuth2AuthenticationToken authentication) {
		memberService.memberCertification(requestMember, authentication.getPrincipal().getAttribute("email"));
		return "test";
	}

	@GetMapping("/signup")
	public String memberGet() {
		log.info("TEST!");
		return "test";
	}

	@PostMapping("/test")
	public String test(@RequestBody @Validated RequestMember signupMember,
		OAuth2AuthenticationToken authentication) {
		log.info("TesT");
		return "test";
	}

}

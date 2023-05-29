package com.tripbook.main.member.controller;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbook.main.global.common.ErrorResponse;
import com.tripbook.main.member.dto.RequestMember;
import com.tripbook.main.member.dto.ResponseMember;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.enums.MemberStatus;
import com.tripbook.main.member.service.MemberService;
import com.tripbook.main.member.vo.MemberVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/member")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Members", description = "Member API")
public class MemberController {
	private final MemberService memberService;

	@Operation(
		summary = "회원가입", description = "프로필 정보를 입력한다.\n\n birth:yyyy-mm-dd", responses = {
		@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseMember.Info.class))),
		@ApiResponse(responseCode = "400", description = "이미 인증되었거나, 유저를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping("/signup")
	public ResponseEntity<Object> memberJoin(HttpServletRequest request,
		@RequestBody @Validated RequestMember.SignupMember requestMember) {
		MemberVO memberVO = MemberVO.builder()
			.birth(requestMember.getBirth())
			.profile(requestMember.getProfile())
			.isMarketing(requestMember.getIsMarketing())
			.email(requestMember.getEmail())
			.name(requestMember.getName())
			.gender(requestMember.getGender())
			.role(MemberRole.ROLE_MEMBER)
			.status(MemberStatus.STATUS_NORMAL)
			.build();
		ResponseMember.Info info = memberService.memberSave(memberVO, request);
		return ResponseEntity.status(HttpStatus.OK).body(info);
	}

	@Operation(responses = {
		@ApiResponse(responseCode = "200", description = "성공시 success 메시지 출력", content = @Content(schema = @Schema(implementation = ResponseMember.ResultInfo.class))),
		@ApiResponse(responseCode = "400", description = "중복된 닉네임이거나, 유효성검사 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PostMapping("/nickname/validate")
	public ResponseEntity<Object> memberNameCheck(
		@RequestBody @Validated RequestMember.SignupNameValidator requestMember) {
		memberService.memberNameValidation(MemberVO.builder().name(requestMember.getName()).build());
		ResponseMember.ResultInfo result = ResponseMember.ResultInfo.builder().status(HttpStatus.OK)
			.message(Arrays.asList("success"))
			.build();
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}

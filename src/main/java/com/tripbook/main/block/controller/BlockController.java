package com.tripbook.main.block.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbook.main.block.dto.RequestBlock;
import com.tripbook.main.block.dto.ResponseBlock;
import com.tripbook.main.block.service.BlockService;
import com.tripbook.main.global.common.ErrorResponse;
import com.tripbook.main.member.dto.PrincipalMemberDto;
import com.tripbook.main.member.dto.ResponseMember;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/blocks")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Blocks", description = "block API")
public class BlockController {
	private final BlockService blockService;
	private final MemberService memberService;

	@Operation(security = {
		@SecurityRequirement(name = "JWT")},
		summary = "차단사용자 조회", description = "JWT를 사용하여 차단사용자조회.\n\n birth:yyyy-mm-dd", responses = {
		@ApiResponse(responseCode = "200", description = "차단사용자 목록", content = @Content(schema = @Schema(implementation = ResponseMember.MemberInfo.class))),
		@ApiResponse(responseCode = "500", description = "관리자문의", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("")
	public ResponseEntity<Object> selectBlockList(Authentication authentication) {
		Member member = getMember(authentication);

		return ResponseEntity.status(HttpStatus.OK)
			.body(blockService.selectBlocks(RequestBlock.builder().member(member).build()));
	}

	@Operation(security = {
		@SecurityRequirement(name = "JWT")},
		summary = "차단사용자 저장", description = "차단사용자저장.\n\n birth:yyyy-mm-dd", responses = {
		@ApiResponse(responseCode = "200", description = "완료", content = @Content(schema = @Schema(implementation = ResponseBlock.ResultInfo.class))),
		@ApiResponse(responseCode = "500", description = "관리자문의", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping("")
	public ResponseEntity<Object> insertBlockList(Authentication authentication, RequestBlock requestBlock) {
		Member member = getMember(authentication);
		requestBlock.setMember(member);
		return ResponseEntity.status(HttpStatus.OK).body(blockService.insertBlocks(requestBlock));
	}

	@Operation(security = {
		@SecurityRequirement(name = "JWT")},
		summary = "차단사용자 삭제", description = "차단사용자 삭제.\n\n birth:yyyy-mm-dd", responses = {
		@ApiResponse(responseCode = "200", description = "완료", content = @Content(schema = @Schema(implementation = ResponseBlock.ResultInfo.class))),
		@ApiResponse(responseCode = "500", description = "관리자문의", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@DeleteMapping("")
	public ResponseEntity<Object> deleteBlockList(Authentication authentication, RequestBlock requestBlock) {
		Member member = getMember(authentication);
		requestBlock.setMember(member);
		return ResponseEntity.status(HttpStatus.OK).body(blockService.deleteBlocks(requestBlock));
	}

	private Member getMember(Authentication authentication) {
		OAuth2User authUser = (OAuth2User)authentication.getPrincipal();
		PrincipalMemberDto principalMemberDto = PrincipalMemberDto.builder().email(authUser.getAttribute("email"))
			.role(new SimpleGrantedAuthority(authentication.getAuthorities().toArray()[0].toString()))
			.build();
		return memberService.getLoginMemberByEmail(principalMemberDto.getEmail());
	}

}

package com.tripbook.main.member.controller;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.article.entity.Article;
import com.tripbook.main.global.common.ErrorResponse;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.global.util.CheckDevice;
import com.tripbook.main.member.dto.PrincipalMemberDto;
import com.tripbook.main.member.dto.RequestMember;
import com.tripbook.main.member.dto.ResponseMember;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.enums.MemberStatus;
import com.tripbook.main.member.service.MemberService;
import com.tripbook.main.member.vo.MemberVO;
import com.tripbook.main.token.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
	@Qualifier("mailJwtService")
	private final JwtService mailJwtService;

	@Operation(security = {
		@SecurityRequirement(name = "JWT")},
		summary = "멤버조회", description = "JWT를 사용하여 멤버조회.\n\n birth:yyyy-mm-dd", responses = {
		@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseMember.MemberInfo.class))),
		@ApiResponse(responseCode = "400", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("/select")
	public ResponseEntity<Object> member(Authentication authentication) {
		OAuth2User authUser = (OAuth2User)authentication.getPrincipal();
		PrincipalMemberDto principalMemberDto = PrincipalMemberDto.builder().email(authUser.getAttribute("email"))
			.role(new SimpleGrantedAuthority(authentication.getAuthorities().toArray()[0].toString()))
			.build();
		ResponseMember.MemberInfo memberInfo = memberService.memberSelect(principalMemberDto);
		return ResponseEntity.status(HttpStatus.OK).body(memberInfo);
	}

	@Operation(security = {
		@SecurityRequirement(name = "JWT")},
		summary = "멤버_임시저장리스트조회", description = "JWT를 사용하여 임시저장 리스트 조회.", responses = {
		@ApiResponse(responseCode = "200", description = "성공", content = @Content(
			array = @ArraySchema(schema = @Schema(implementation = ArticleResponseDto.ArticleResponse.class)))
		),
		@ApiResponse(responseCode = "400", description = "유저를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/select/articles/temp")
	public ResponseEntity<Object> selectTempArticles(Authentication authentication) {
		OAuth2User authUser = (OAuth2User)authentication.getPrincipal();
		String email = (String)authUser.getAttribute("email");
		if (email == null || email.isEmpty()) {
			throw new CustomException.MemberNotFound(ErrorCode.MEMBER_NOTFOUND.getMessage(), ErrorCode.MEMBER_NOTFOUND);
		}
		List<ArticleResponseDto.ArticleResponse> resultList = memberService.memberTempArticleList(email);
		return ResponseEntity.status(HttpStatus.OK).body(resultList);
	}

	@Operation(
		summary = "회원가입", description = "프로필 정보를 입력한다.\n\n birth:yyyy-mm-dd", responses = {
		@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseMember.Info.class))),
		@ApiResponse(responseCode = "400", description = "이미 인증되었거나, 유저를 찾을 수 없음, 프로필 업로드 용량을 초과함(5MB)", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping(value = "/signup", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Object> memberJoin(HttpServletRequest request,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Validated RequestMember.MemberReqInfo requestMember) {
		if (!requestMember.getImageAccept()) {
			log.error("Image Not Accepted::{}", requestMember.getImageFile().getOriginalFilename());
			throw new CustomException.UnsupportedImageFileException(requestMember.getImageFile().getOriginalFilename(),
				ErrorCode.FILE_UNSUPPORTED_ERROR);
		}
		MemberVO memberVO = bindMemberVo(requestMember);
		String deviceValue = CheckDevice.checkDevice(request);
		ResponseMember.Info info = memberService.memberSave(memberVO, deviceValue);
		log.info("Email::" + memberVO.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(info);
	}

	/**
	 * 멤버 수정 로직
	 *
	 * @param updateMember
	 * @return 결과 값
	 */
	@Operation(security = {
		@SecurityRequirement(name = "JWT")}, summary = "멤버 업데이트", description = "Member 수정을 위한 JWT 토큰 입력",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공시 success 메시지 출력", content = @Content(schema = @Schema(implementation = ResponseMember.ResultInfo.class))),
			@ApiResponse(responseCode = "400", description = "Nickname 중복 \n\n 이메일 중복 "
				+ "\n\n 유효하지 않는 유저이메일", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		})
	@PostMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Object> memberUpdate(@Validated RequestMember.UpdateProfile updateMember,
		Authentication authentication) {
		OAuth2User authUser = (OAuth2User)authentication.getPrincipal();
		// notNull 필드 가져오기
		memberService.memberUpdate(bindMemberVo(updateMember, authUser));
		ResponseMember.ResultInfo result = ResponseMember.ResultInfo.builder().status(HttpStatus.OK)
			.message(Arrays.asList("success"))
			.build();
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@Operation(
		security = {
			@SecurityRequirement(name = "JWT")},
		responses = {
			@ApiResponse(responseCode = "200", description = "성공시 success 메시지 출력", content = @Content(schema = @Schema(implementation = ResponseMember.ResultInfo.class))),
			@ApiResponse(responseCode = "401", description = "토큰정보와 사용자 이메일정보가 다름", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))}
		, summary = "멤버 삭제", description = "Member 삭제API")
	@PostMapping("/delete")
	public ResponseEntity<Object> memberDelete(@RequestParam String email, Authentication authentication) {
		OAuth2User authUser = (OAuth2User)authentication.getPrincipal();
		if (!email.equals(authUser.getAttribute("email"))) {
			throw new CustomException.MemberNameAlreadyException(ErrorCode.MEMBER_NOT_PERMITTED.getMessage(),
				ErrorCode.MEMBER_NOT_PERMITTED);
		}
		memberService.memberDelete(MemberVO.builder().email(email).build());
		ResponseMember.ResultInfo result = ResponseMember.ResultInfo.builder().status(HttpStatus.OK)
			.message(Arrays.asList("success"))
			.build();
		return ResponseEntity.status(HttpStatus.OK).body(result);

	}

	@Operation(responses = {
		@ApiResponse(responseCode = "200", description = "성공시 success 메시지 출력", content = @Content(schema = @Schema(implementation = ResponseMember.ResultInfo.class))),
		@ApiResponse(responseCode = "400", description = "중복된 닉네임이거나, 유효성검사 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/nickname/validate")
	public ResponseEntity<Object> memberNameCheck(
		@Validated RequestMember.SignupNameValidator requestMember) {

		boolean isDuplicated = memberService.memberNameValidation(
			MemberVO.builder().name(requestMember.getName()).build());

		if (isDuplicated) {
			throw new CustomException.MemberNameAlreadyException(ErrorCode.MEMBER_NAME_ERROR.getMessage(),
				ErrorCode.MEMBER_NAME_ERROR);
		}

		ResponseMember.ResultInfo result = ResponseMember.ResultInfo.builder().status(HttpStatus.OK)
			.message(Arrays.asList("success"))
			.build();
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	private static MemberVO bindMemberVo(RequestMember.MemberReqInfo requestMember) {
		MemberVO memberVO = MemberVO.builder()
			.birth(requestMember.getBirth())
			.imageFile(requestMember.getImageFile())
			.termsOfService(requestMember.getTermsOfService())
			.termsOfPrivacy(requestMember.getTermsOfPrivacy())
			.termsOfLocation(requestMember.getTermsOfLocation())
			.marketingContent(requestMember.getMarketingConsent())
			.email(requestMember.getEmail())
			.name(requestMember.getName())
			.gender(requestMember.getGender())
			.role(MemberRole.ROLE_MEMBER)
			.status(MemberStatus.STATUS_NORMAL)
			.build();
		return memberVO;
	}

	private static MemberVO bindMemberVo(RequestMember.UpdateProfile requestMember, OAuth2User authUser) {
		MemberVO memberVO = MemberVO.builder()
			.birth(requestMember.getBirth())
			.imageFile(requestMember.getImageFile())
			.profile(requestMember.getProfile())
			.termsOfService(requestMember.getTermsOfService())
			.termsOfPrivacy(requestMember.getTermsOfPrivacy())
			.termsOfLocation(requestMember.getTermsOfLocation())
			.marketingContent(requestMember.getMarketingConsent())
			.email(authUser.getAttribute("email"))
			.name(requestMember.getName())
			.gender(requestMember.getGender())
			.build();
		return memberVO;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(MultipartFile.class, new PropertyEditorSupport() {

			@Override
			public void setAsText(String text) {
				log.debug("initBinder MultipartFile.class: {}; set null;", text);
				setValue(null);
			}

		});
	}
}

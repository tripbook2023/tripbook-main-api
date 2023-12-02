package com.tripbook.main.global.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.global.common.ErrorResponse;
import com.tripbook.main.global.dto.ResponseImage;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.enums.ImageCategory;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.global.service.UploadService;
import com.tripbook.main.member.dto.PrincipalMemberDto;
import com.tripbook.main.member.dto.ResponseMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/common/image")
@Tag(name = "Commons", description = "Common API")
public class UploadController {
	@Resource
	UploadService uploadService;

	@Operation(security = {
		@SecurityRequirement(name = "JWT")},
		summary = "S3이미지업로드", description = "S3를 이용한 이미지 업로드\n Category : 여행소식(BOARD_A) ", responses = {
		@ApiResponse(responseCode = "200", description = "성공_URL정보", content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청, 파라미터 값 확인", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "권한없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버에러, 관리자 문의요망", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Object> imageS3Upload(Authentication authentication,@RequestParam String category,@RequestParam
		MultipartFile image) {
		if(authentication==null){
			//토큰없음
			throw new CustomException.CommonNotPermittedException(ErrorCode.COMMON_NOT_PERMITTED.getMessage(),ErrorCode.COMMON_NOT_PERMITTED);
		}
		return ResponseEntity.status(HttpStatus.OK).body(uploadService.imageUpload(image,category));
	}

}

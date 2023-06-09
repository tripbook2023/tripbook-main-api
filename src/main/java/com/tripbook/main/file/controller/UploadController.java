package com.tripbook.main.file.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbook.main.file.dto.RequestImageDto;
import com.tripbook.main.file.dto.ResponseImageDto;
import com.tripbook.main.file.service.UploadService;
import com.tripbook.main.global.common.ErrorResponse;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author : 이석운
 * @Description : OCI FileServer 업로드 기능 구현
 * @Return : Image URL
 */
@RestController
@RequestMapping("/file")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "File", description = "File API")
public class UploadController {
	private final UploadService uploadService;

	@Operation(
		summary = "이미지파일업로드", description = "이미지파일을 업로드한다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseImageDto.ImageInfo.class))),
		@ApiResponse(responseCode = "500", description = "지원하지 않는 확장자 또는 파일 용량", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping(value = "/image",
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> imageUpload(
		@ModelAttribute RequestImageDto.ImageDto imageFile) {
		if (imageFile == null) {
			throw new CustomException.UnsupportedImageFileException(ErrorCode.FILE_UNSUPPORTED_ERROR.getMessage(),
				ErrorCode.FILE_UNSUPPORTED_ERROR);
		}
		String resultUrl = uploadService.imageUpload(imageFile);
		ResponseImageDto.ImageInfo result = ResponseImageDto.ImageInfo.builder()
			.fileUrl(resultUrl)
			.status(HttpStatus.OK)
			.build();
		log.info("ImageUploadSuccess!");
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

}

package com.tripbook.main.global.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tripbook.main.global.dto.ResponseImage;
import com.tripbook.main.global.entity.Image;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.enums.ImageCategory;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.global.repository.ImageRepository;
import com.tripbook.main.global.util.BasicUploader;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {
	@Qualifier("s3Uploader")
	private final BasicUploader s3Uploader;
	@Resource
	private final ImageRepository imageRepository;
	@Value("${file.upload_path.signup}")
	private String memberPath;
	@Value("${file.upload_path.article}")
	private String articlePath;
	@Override
	@SneakyThrows
	public ResponseImage.ImageInfo imageUpload(MultipartFile file,String category) {
		String s3Url="";
		if(ImageCategory.BOARD_A.toString().equals(category.toUpperCase())){
			try {
				s3Url=s3Uploader.uploadFile(file,articlePath);
			} catch (IOException e) {
				throw new CustomException.CommonRuntimeException(ErrorCode.COMMON_RUNTIME_ERROR.getMessage(),ErrorCode.COMMON_RUNTIME_ERROR);
			}
		}else if(ImageCategory.MEMBER.toString().equals(category.toUpperCase())){
			try {
				s3Url=s3Uploader.uploadFile(file,memberPath);
			} catch (IOException e) {
				throw new CustomException.CommonRuntimeException(ErrorCode.COMMON_RUNTIME_ERROR.getMessage(),ErrorCode.COMMON_RUNTIME_ERROR);
			}
		}
		if(!s3Url.isEmpty()){
			Image image = Image.builder()
				.url(s3Url)
				.name(file.getName())
				.refType(category)
				.build();
			imageRepository.save(image);
			return new ResponseImage.ImageInfo(image);
		}else{
			throw new CustomException.CommonUnSupportedException(ErrorCode.COMMON_UNSUPPORTED_ERROR.getMessage(),ErrorCode.COMMON_UNSUPPORTED_ERROR);
		}
	}
}

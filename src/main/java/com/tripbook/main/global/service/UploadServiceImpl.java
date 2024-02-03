package com.tripbook.main.global.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
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
	public ResponseImage.ImageInfo imageUpload(MultipartFile file, String category) {
		// String s3Url = "";
		List<String> s3ResultList = null;
		if (ImageCategory.BOARD_A.toString().equals(category.toUpperCase())) {
			try {
				s3ResultList = s3Uploader.uploadFile(file, articlePath);
			} catch (IOException e) {
				throw new CustomException.CommonRuntimeException(ErrorCode.COMMON_RUNTIME_ERROR.getMessage(),
					ErrorCode.COMMON_RUNTIME_ERROR);
			}
		} else if (ImageCategory.MEMBER.toString().equals(category.toUpperCase())) {
			try {
				s3ResultList = s3Uploader.uploadFile(file, memberPath);
			} catch (IOException e) {
				throw new CustomException.CommonRuntimeException(ErrorCode.COMMON_RUNTIME_ERROR.getMessage(),
					ErrorCode.COMMON_RUNTIME_ERROR);
			}
		}
		if (s3ResultList != null && !s3ResultList.isEmpty()) {
			Image image = Image.builder()
				.url(s3ResultList.get(1))
				.keyName(s3ResultList.get(0))
				.name(file.getOriginalFilename())
				.refType(category)
				.build();
			Image saveImage = imageRepository.save(image);
			return new ResponseImage.ImageInfo(saveImage);
		} else {
			throw new CustomException.CommonUnSupportedException(ErrorCode.COMMON_UNSUPPORTED_ERROR.getMessage(),
				ErrorCode.COMMON_UNSUPPORTED_ERROR);
		}
	}

	@Override
	@SneakyThrows
	public void imageDelete(Long refId, String refType) {
		Optional<List<Image>> targetImage = imageRepository.findByRefIdAndRefType(refId, refType);
		targetImage.ifPresent(item -> {
			item.forEach(deleteItem -> {
				log.info("ImageDelete_keyName:::{}", deleteItem.getKeyName());
				s3Uploader.deleteFile(deleteItem.getKeyName());
			});
			imageRepository.deleteAll(targetImage.get());
		});

	}
}

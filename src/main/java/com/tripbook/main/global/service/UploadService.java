package com.tripbook.main.global.service;

import org.springframework.web.multipart.MultipartFile;

import com.tripbook.main.global.dto.ResponseImage;

public interface UploadService {
	public ResponseImage.ImageInfo imageUpload(MultipartFile file, String path);
}

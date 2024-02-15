package com.tripbook.main.global.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tripbook.main.global.dto.ResponseImage;

public interface UploadService {
	public ResponseImage.ImageInfo imageUpload(MultipartFile file, String path);

	public void imageDelete(Long refId, String refType);
	public void imageDelete(List<Long> fileIds);
}

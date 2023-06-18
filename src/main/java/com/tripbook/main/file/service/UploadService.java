package com.tripbook.main.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
	public String imageUpload(MultipartFile file, String path);
}

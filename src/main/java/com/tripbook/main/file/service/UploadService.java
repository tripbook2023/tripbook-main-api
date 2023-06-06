package com.tripbook.main.file.service;

import com.tripbook.main.file.dto.RequestImageDto;

public interface UploadService {
	public String imageUpload(RequestImageDto.ImageDto file);
}

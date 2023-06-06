package com.tripbook.main.file.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tripbook.main.file.dto.RequestImageDto;
import com.tripbook.main.global.util.OciUploader;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {
	private final OciUploader ociUploader;
	@Value("${file.upload_path.signup}")
	private String path;

	@Override
	@SneakyThrows
	public String imageUpload(RequestImageDto.ImageDto file) {
		return ociUploader.uploadFile(file.getImageFile(), path);
	}
}

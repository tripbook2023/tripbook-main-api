package com.tripbook.main.file.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tripbook.main.global.util.BasicUploader;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {
	@Qualifier("s3Uploader")
	private final BasicUploader s3Uploader;

	@Override
	@SneakyThrows
	public String imageUpload(MultipartFile file, String path) {
		return s3Uploader.uploadFile(file, path);
	}
}

package com.tripbook.main.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tripbook.main.global.util.OciUploader;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {
	private final OciUploader ociUploader;

	@Override
	@SneakyThrows
	public String imageUpload(MultipartFile file, String path) {
		return ociUploader.uploadFile(file, path);
	}
}

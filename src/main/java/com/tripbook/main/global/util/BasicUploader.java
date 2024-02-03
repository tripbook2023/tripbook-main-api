package com.tripbook.main.global.util;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface BasicUploader {
	public List<String> uploadFile(MultipartFile multipartFile, String dirName) throws IOException;

	public void deleteFile(String key);

}

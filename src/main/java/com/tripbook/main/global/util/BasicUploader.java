package com.tripbook.main.global.util;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface BasicUploader {
	public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException;

}

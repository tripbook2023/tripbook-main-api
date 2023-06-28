package com.tripbook.main.global.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Service("s3Uploader")
@RequiredArgsConstructor
public class S3Uploader implements BasicUploader {
	private final AmazonS3Client amazonS3Client;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException {
		String fileName = dirName + "/" + UUID.randomUUID() + multipartFile.getOriginalFilename();   // S3에 저장된 파일 이름
		return putS3(multipartFile, fileName);
	}

	// S3로 업로드
	private String putS3(MultipartFile file, String fileName) throws IOException {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
		metadata.setContentLength(file.getSize());
		amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata).withCannedAcl(
			CannedAccessControlList.PublicRead));
		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

}

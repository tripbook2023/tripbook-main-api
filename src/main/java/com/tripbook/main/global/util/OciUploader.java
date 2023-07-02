package com.tripbook.main.global.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;

import lombok.RequiredArgsConstructor;

@Service("ociUploader")
@RequiredArgsConstructor
public class OciUploader implements BasicUploader {
	private final ObjectStorageClient objectStorageClient;
	@Value("${cloud.oci.bucket}")
	private String bucket;
	@Value("${cloud.oci.namespace}")
	private String namespace;
	@Value("${cloud.oci.preAuthenticatedUrl}")
	private String authenticatedUrl;

	public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException {

		InputStream objectBody = multipartFile.getInputStream();
		String contentType = multipartFile.getContentType();
		String fileName = dirName + "/" + UUID.randomUUID() + multipartFile.getName();

		return putObjectStorage(objectBody, contentType, fileName);
	}

	private String putObjectStorage(InputStream objectBody, String contentType, String fileName) {
		PutObjectRequest request = PutObjectRequest.builder()
			.namespaceName(namespace)
			.bucketName(bucket)
			.objectName(fileName)
			.contentType(contentType)
			.putObjectBody(objectBody)
			.build();

		PutObjectResponse response = objectStorageClient.putObject(request);
		assert response.get__httpStatusCode__() == 200 : "error: putObject()";

		return authenticatedUrl + fileName.replaceAll("/", "%2F");
	}

}

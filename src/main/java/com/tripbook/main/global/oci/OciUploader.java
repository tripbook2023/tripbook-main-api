package com.tripbook.main.global.oci;

import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OciUploader {
    private final ObjectStorageClient objectStorageClient;
    @Value("${cloud.oci.bucket}")
    private String bucket;
    @Value("${cloud.oci.namespace}")
    private String namespace;
    @Value("${cloud.oci.preAuthenticatedUrl}")
    private String authenticatedUrl;

    public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        InputStream objectBody = new FileInputStream(uploadFile);
        String contentType = multipartFile.getContentType();
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();

        String url = putObjectStorage(objectBody, contentType, fileName);

        removeNewFile(uploadFile);

        return url;
    }

    private String putObjectStorage(InputStream objectBody, String contentType, String fileName) throws IOException {

        PutObjectRequest request = PutObjectRequest.builder()
                .namespaceName(namespace)
                .bucketName(bucket)
                .objectName(fileName)
                .contentType(contentType)
                .putObjectBody(objectBody)
                .build();

        PutObjectResponse response = objectStorageClient.putObject(request);
        assert response.get__httpStatusCode__() == 200 : "error: putObject()";

        // 수정필요
        return authenticatedUrl + fileName;
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            System.out.println("File delete success");
            return;
        }
        System.out.println("File delete fail");
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }


}

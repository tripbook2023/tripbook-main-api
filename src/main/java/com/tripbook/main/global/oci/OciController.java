package com.tripbook.main.global.oci;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/oci/upload")
@RequiredArgsConstructor
@Slf4j
public class OciController {
    private final OciUploader ociUploader;

    @PostMapping()
    public ResponseEntity<?> upload(@RequestPart(value = "file")MultipartFile multipartFile) {
        String res = null;
        try {
            res = ociUploader.uploadFiles(multipartFile, "test");
        } catch (Exception e) {
            log.error("-----------------------------");
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(res);
    }

}

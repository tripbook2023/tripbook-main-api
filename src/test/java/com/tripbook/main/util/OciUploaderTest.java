package com.tripbook.main.util;

import com.tripbook.main.global.util.OciUploader;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles(profiles = "local")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class OciUploaderTest {

    @Autowired
    private OciUploader ociUploader;
    @Test
    void upload_file_to_OCI() throws Exception {
        //given
        String dirName = "test";
        File inputFile = new File("C:\\Users\\구본주\\Desktop\\aaaa.png");
        MultipartFile multipartFile = new MockMultipartFile("aaaa.png","aaaa.png", "image/png", new FileInputStream(inputFile));

        //when
        String resUrl = ociUploader.uploadFile(multipartFile, dirName);

        URL url = new URL(resUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        int resCode = conn.getResponseCode();

        //then
        assertThat(resCode).isEqualTo(200);

    }

}

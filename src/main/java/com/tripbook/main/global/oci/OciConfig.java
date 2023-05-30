package com.tripbook.main.global.oci;

import com.oracle.bmc.ClientConfiguration;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AbstractRequestingAuthenticationDetailsProvider;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.internal.FederationClient;
import com.oracle.bmc.http.client.HttpProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.retrier.DefaultRetryCondition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class OciConfig {
    @Value("${cloud.oci.region}")
    private String region;
    @Value("${cloud.oci.bucket}")
    private String bucket;

    @Bean
    public ObjectStorageClient OciClient() throws IOException {

        // 인증절차 수정 필요
        AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(bucket);

        return ObjectStorageClient.builder()
                .region(Region.valueOf(region))
                .configuration(ClientConfiguration.builder().build())
                .build(provider);
    }

}

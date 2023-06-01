package com.tripbook.main.global.config;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.ConfigFileReader.ConfigFile;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class OciConfig {

    @Value("${cloud.oci.configFilePath}")
    private String configFilePath;

    @Bean
    public ObjectStorageClient ObjectStorageClient() throws IOException {
        ConfigFile config = ConfigFileReader.parse(configFilePath, "DEFAULT");
        AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);

        return ObjectStorageClient.builder()
                .build(provider);
    }

}

package com.tripbook.main.global.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.ConfigFileReader.ConfigFile;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;

@Configuration
public class OciConfig {

	@Value("${cloud.oci.configFilePath}")
	private String configFilePath;
	@Autowired
	private ResourceLoader resourceLoader;

	@Bean
	public ObjectStorageClient ObjectStorageClient() throws IOException {
		Resource resource = resourceLoader.getResource(configFilePath);
		String absolutePath = ResourceUtils.getFile(resource.getURL()).getAbsolutePath();
		ConfigFile config = ConfigFileReader.parse(absolutePath, "DEFAULT");
		AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);

		return ObjectStorageClient.builder()
			.build(provider);
	}

}

package com.tripbook.main.swagger.config;

import java.util.Arrays;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@OpenAPIDefinition(
	info = @Info(title = "트립북  API 명세서",
		description = "알맞는 명세서를 선택하세요.",
		version = "v1"))
@Configuration
public class SwaggerConfig {
	@Value("${domain.name}")
	private String domainName;

	@Bean
	public GroupedOpenApi MemberGroup() {
		return GroupedOpenApi.builder()
			.group("멤버API")
			.packagesToScan("com.tripbook.main.member.controller", "com.tripbook.main.auth.controller",
				"com.tripbook.main.token.controller")
			.build();
	}

	@Bean
	public GroupedOpenApi BlockGroup() {
		return GroupedOpenApi.builder()
			.group("멤버차단API")
			.packagesToScan("com.tripbook.main.block.controller")
			.build();
	}

	@Bean
	public GroupedOpenApi ArticleGroup() {
		return GroupedOpenApi.builder()
			.group("여행소식API")
			.packagesToScan("com.tripbook.main.article.controller")
			.build();
	}

	@Bean
	public GroupedOpenApi CommonGroup() {
		return GroupedOpenApi.builder()
			.group("공통API")
			.packagesToScan("com.tripbook.main.global.controller")
			.build();
	}

	@Bean
	public OpenAPI JwtOpenAPI() {
		return new OpenAPI()
			.servers(Arrays.asList(
				new Server().url(domainName).description("HTTPS Server"))
			)
			.components(new Components()
				.addSecuritySchemes("JWT",
					new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT"))
				.addSecuritySchemes("OAUTH",
					new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")));
	}

}
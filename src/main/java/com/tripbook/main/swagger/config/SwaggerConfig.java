package com.tripbook.main.swagger.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition(
	info = @Info(title = "트립북  API 명세서",
		description = "알맞는 명세서를 선택하세요.",
		version = "v1"))
@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi MemberGroup() {
		return GroupedOpenApi.builder()
			.group("멤버API")
			.packagesToScan("com.tripbook.main.member.controller", "com.tripbook.main.auth.controller",
				"com.tripbook.main.token.controller")
			.build();
	}

	// @Bean
	// public Docket api() {
	// 	return new Docket(DocumentationType.OAS_30)
	// 		.groupName("01.멤버관련 명세서")
	// 		.select()
	// 		.apis(RequestHandlerSelectors.any())
	// 		.paths(PathSelectors.any()) // AntPathRequestMatcher에 사용한 패턴 지정
	// 		.build();
	// }

	@Bean
	public OpenAPI JwtOpenAPI() {
		return new OpenAPI()
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
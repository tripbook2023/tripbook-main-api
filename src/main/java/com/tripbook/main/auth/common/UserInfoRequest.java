package com.tripbook.main.auth.common;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.enums.MemberStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserInfoRequest {
	static ParameterizedTypeReference<Map<String, Object>> RESPONSE_TYPE = new ParameterizedTypeReference<>() {
	};
	protected final RestTemplate restTemplate = new RestTemplate();
	@Value("${spring.security.oauth2.client.provider.auth0.token-uri}")
	private String issuerUri;

	public Member getSocialEmail(String accessToken) {

		HttpHeaders headers = createHeaders(accessToken);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		try {
			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(issuerUri, HttpMethod.GET, request,
				RESPONSE_TYPE);
			Map<String, Object> response2 = (Map<String, Object>)response.getBody();
			return Member.builder()
				.email(response2.get("email").toString())
				.name(response2.get("name").toString())
				.role(MemberRole.ROLE_MEMBER)
				.status(MemberStatus.ADDITIONAL_AUTHENTICATION)
				.isMarketing(false)
				.build();
		} catch (HttpClientErrorException.Unauthorized e) {
			log.error("TOKEN_UNAUTHORIZED ERRROR", e);
			throw new CustomException.InvalidTokenException(ErrorCode.TOKEN_UNAUTHORIZED.getMessage(),
				ErrorCode.TOKEN_UNAUTHORIZED);
		}
	}

	public HttpHeaders createHeaders(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return headers;
	}
}

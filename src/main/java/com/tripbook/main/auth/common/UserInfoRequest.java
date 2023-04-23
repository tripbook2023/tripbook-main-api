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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.MemberStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserInfoRequest {
	static ParameterizedTypeReference<Map<String, Object>> RESPONSE_TYPE  =  new ParameterizedTypeReference<>(){};

	protected static final RestTemplate restTemplate = new RestTemplate();
	@Value("${spring.security.oauth2.client.provider.auth0.token-uri}")
	private static String issuerUri="https://dev-z2b4bazfo6o536tj.us.auth0.com/userinfo/";
	public static Member getSocialEmail(String accessToken) {
		HttpHeaders headers = new HttpHeaders();

		setHeaders(accessToken, headers);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();


		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		try {

			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(issuerUri,
				HttpMethod.GET,
				request,
				RESPONSE_TYPE);
			Map<String , Object> response2 = ( Map<String , Object>)response.getBody();
			return Member.builder()
				.email(response2.get("email").toString())
				.name(response2.get("name").toString())
				.status(MemberStatus.STATUS_NORMAL)
				.build();
		} catch (Exception e) {
			log.error("AccessToken을 사용하여 유저정보를 받아오던 중 예외가 발생했습니다 {}" ,e.getMessage() );
			throw e;
		}
	}




	public static void setHeaders(String accessToken, HttpHeaders headers) {
		headers.set("Authorization", "Bearer " + accessToken);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	}

}

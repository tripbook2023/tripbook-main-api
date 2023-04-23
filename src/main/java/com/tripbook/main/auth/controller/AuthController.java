package com.tripbook.main.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@GetMapping("/")
	public String home(Model model, @AuthenticationPrincipal OidcUser principal) {
		if (principal != null) {
			model.addAttribute("profile", principal.getClaims());
		}
		return "index";
	}

	//액세스토큰 생성을 위한 테스트용입니다.
	@GetMapping("/login/oauth2/code/auth0")
	public String authTest(OAuth2AuthenticationToken authentication){
		OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
			authentication.getAuthorizedClientRegistrationId(), authentication.getName());

		String accessToken = authorizedClient.getAccessToken().getTokenValue();

		return "Access Token: " + accessToken;
	}
	@GetMapping("/hello")
	public String test(){
		return "index";
	}
}

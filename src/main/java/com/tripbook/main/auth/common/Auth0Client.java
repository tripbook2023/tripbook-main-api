package com.tripbook.main.auth.common;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth0Client", url = "https://dev-z2b4bazfo6o536tj.us.auth0.com")
public interface Auth0Client {

	@GetMapping(value = "/userinfo")
	Auth0UserInfo getUserInfoFromAuth0Token(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);
}

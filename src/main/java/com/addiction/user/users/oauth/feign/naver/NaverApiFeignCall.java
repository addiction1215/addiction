package com.addiction.user.users.oauth.feign.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.addiction.user.users.oauth.feign.naver.response.NaverUserInfoResponse;

@Component
@FeignClient(name = "${oauth.naver.api.name}", url = "${oauth.naver.api.url}", configuration = FeignClientsConfiguration.class)
public interface NaverApiFeignCall {

	@GetMapping(value = "/v1/nid/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	NaverUserInfoResponse getUserInfo(@RequestHeader("Authorization") String auth);

}

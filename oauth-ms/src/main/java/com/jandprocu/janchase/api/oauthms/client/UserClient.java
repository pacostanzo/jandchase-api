package com.jandprocu.janchase.api.oauthms.client;

import com.jandprocu.janchase.api.oauthms.rest.UserGetOAuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-ms")
public interface UserClient {
    @GetMapping(path = "/getByUserName/{userName}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<UserGetOAuthResponse> getUserOAuth(@PathVariable("userName") String userName);
}

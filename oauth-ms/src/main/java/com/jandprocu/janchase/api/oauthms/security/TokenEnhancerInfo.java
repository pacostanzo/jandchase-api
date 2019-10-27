package com.jandprocu.janchase.api.oauthms.security;

import com.jandprocu.janchase.api.oauthms.rest.UserGetOAuthResponse;
import com.jandprocu.janchase.api.oauthms.service.IUserService;
import com.jandprocu.janchase.api.oauthms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenEnhancerInfo implements TokenEnhancer {
    @Autowired
    private IUserService userService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> moreInfo = new HashMap<>();

        UserGetOAuthResponse user = userService.findByUsername(authentication.getName());
        moreInfo.put("firstName", user.getFirstName());
        moreInfo.put("lastName", user.getLastName());
        moreInfo.put("email", user.getEmail());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(moreInfo);

        return accessToken;
    }

}

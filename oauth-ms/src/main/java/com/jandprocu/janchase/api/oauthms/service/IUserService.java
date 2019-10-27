package com.jandprocu.janchase.api.oauthms.service;

import com.jandprocu.janchase.api.oauthms.rest.UserGetOAuthResponse;

public interface IUserService {

    public UserGetOAuthResponse findByUsername(String username);

}

package com.jandprocu.jandchase.api.usersms.service;

import com.jandprocu.jandchase.api.usersms.rest.UserRest;
import com.jandprocu.jandchase.api.usersms.rest.response.UserCreateResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetOAuthResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetResponse;

public interface IUserService {
    UserCreateResponse createUser(UserRest userRequest);

    UserGetResponse getUserByUserId(String userId);

    UserGetOAuthResponse getUserByUserName(String userName);

    UserGetResponse deleteUserByUserId(String userId);

}

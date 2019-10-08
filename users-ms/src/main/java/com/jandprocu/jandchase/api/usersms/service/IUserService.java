package com.jandprocu.jandchase.api.usersms.service;

import com.jandprocu.jandchase.api.usersms.rest.UserRest;
import com.jandprocu.jandchase.api.usersms.rest.request.UserUpdateRequest;
import com.jandprocu.jandchase.api.usersms.rest.response.UserCreateResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetOAuthResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserUpdateResponse;

public interface IUserService {
    UserCreateResponse createUser(UserRest userRequest);

    UserGetResponse getUserByUserId(String userId);

    UserUpdateResponse updateUserByUserId(String userId, UserRest updateRequest);

    UserGetResponse deleteUserByUserId(String userId);

    UserGetResponse getUserByUserName(String userName);

    UserGetOAuthResponse getUserByUserOAuthName(String userName);
}

package com.jandprocu.jandchase.api.usersms.service;

import com.jandprocu.jandchase.api.usersms.rest.UserRest;
import com.jandprocu.jandchase.api.usersms.rest.response.UserCreateResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetOAuthResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserResponse;

import java.util.List;

public interface IUserService {
    UserCreateResponse createUser(UserRest userRequest);

    UserGetResponse getUserByUserId(String userId);

    UserGetOAuthResponse getUserByUserName(String userName);

    UserGetResponse updateUserByUserId(String userId, UserRest updateRequest);

    UserGetResponse deleteUserByUserId(String userId);

    UserResponse addRolesByUserId(String userId, List<String> rolesName);

    UserResponse removeRolesByUserId(String userId, List<String> rolesNames);
}

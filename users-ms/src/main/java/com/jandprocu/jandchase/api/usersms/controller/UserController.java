package com.jandprocu.jandchase.api.usersms.controller;

import com.jandprocu.jandchase.api.usersms.rest.request.UserCreateRequest;
import com.jandprocu.jandchase.api.usersms.rest.request.UserUpdateRequest;
import com.jandprocu.jandchase.api.usersms.rest.response.UserCreateResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetOAuthResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetResponse;
import com.jandprocu.jandchase.api.usersms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class UserController {


    @Autowired
    private IUserService userService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserCreateResponse> createUser(@Valid @RequestBody UserCreateRequest createRequest) {
        UserCreateResponse createdUser = userService.createUser(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping(path = "/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserGetResponse> getUser(@PathVariable String userId) {
        UserGetResponse userResponse = userService.getUserByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping(path = "/getByUserName/{userName}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserGetOAuthResponse> getUserOAuth(@PathVariable String userName) {
        UserGetOAuthResponse userResponse = userService.getUserByUserName(userName);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @PutMapping(path = "/{userId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserGetResponse> updateUser(@PathVariable String userId,
                                                      @Valid @RequestBody UserUpdateRequest updateRequest) {
        UserGetResponse updateResponse = userService.updateUserByUserId(userId, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateResponse);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<UserGetResponse> deleteUser(@PathVariable String userId) {
        UserGetResponse deleteUser = userService.deleteUserByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(deleteUser);
    }


    @PostMapping(path = "/{userId}/addRoles",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserGetResponse> addRolesToUser(@PathVariable String userId,
                                                      @Valid @RequestBody List<String> rolesNames) {
        UserGetResponse updateResponse = (UserGetResponse) userService.addRolesByUserId(userId, rolesNames);
        return ResponseEntity.status(HttpStatus.OK).body(updateResponse);
    }


    @DeleteMapping(path = "/{userId}/removeRoles",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserGetResponse> removeRolesToUser(@PathVariable String userId,
                                                             @Valid @RequestBody List<String> rolesNames) {
        UserGetResponse updateResponse = (UserGetResponse) userService.removeRolesByUserId(userId, rolesNames);
        return ResponseEntity.status(HttpStatus.OK).body(updateResponse);
    }
}

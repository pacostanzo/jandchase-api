package com.jandprocu.jandchase.api.usersms.service;

import com.jandprocu.jandchase.api.usersms.exception.UserNotCreatedException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotFoundException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotUpdatedException;
import com.jandprocu.jandchase.api.usersms.model.Role;
import com.jandprocu.jandchase.api.usersms.repository.RoleRepository;
import com.jandprocu.jandchase.api.usersms.rest.UserRest;
import com.jandprocu.jandchase.api.usersms.rest.request.UserCreateRequest;
import com.jandprocu.jandchase.api.usersms.model.User;
import com.jandprocu.jandchase.api.usersms.rest.request.UserUpdateRequest;
import com.jandprocu.jandchase.api.usersms.rest.request.UserRequest;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserUpdateResponse;
import com.jandprocu.jandchase.api.usersms.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private IUserService userService;

    private UserCreateRequest userRequest;
    private User user;

    @Before
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        userService = new UserService(userRepository, roleRepository);
        userRequest = new UserCreateRequest();
        userRequest.setUserName("costanzopa");
        userRequest.setFirstName("Pablo");
        userRequest.setLastName("Costanzo");
        userRequest.setEmail("costanzopa@gmail.com");
        userRequest.setPassword("12345678");

        user = modelMapper.map(userRequest, User.class);
    }

    @Test
    public void createUser_OK_ReturnsUserInfo() {
        Role role = new Role();
        role.setName("ROLE_USER");
        given(roleRepository.findByName(anyString())).willReturn(role);

        given(userRepository.save(any())).willReturn(new User());

        UserRest userRest = userService.createUser(userRequest);

        assertThat(userRest.getFirstName()).isEqualTo("Pablo");
        assertThat(userRest.getLastName()).isEqualTo("Costanzo");
        assertThat(userRest.getEmail()).isEqualTo("costanzopa@gmail.com");

    }


    @Test(expected = UserNotCreatedException.class)
    public void createUser_WhenUserWasCreated() throws DataAccessException {
        Role role = new Role();
        role.setName("ROLE_USER");
        given(roleRepository.findByName(anyString())).willReturn(role);
        given(userRepository.save(any())).willThrow(new DuplicateKeyException("Test Exception"));
        userService.createUser(userRequest);
    }


    @Test
    public void getUserByUserId_OK_ReturnsUserInfo() {

        given(userRepository.findByUserId(anyString())).willReturn(user);

        UserGetResponse userRest = userService.getUserByUserId("userId");

        assertThat(userRest.getFirstName()).isEqualTo("Pablo");
        assertThat(userRest.getLastName()).isEqualTo("Costanzo");
        assertThat(userRest.getEmail()).isEqualTo("costanzopa@gmail.com");

    }

    @Test(expected = UserNotFoundException.class)
    public void getUserByUserId_WhenUserNotFound() {
        given(userRepository.findByUserId(anyString())).willReturn(null);
        userService.getUserByUserId(anyString());
    }

    @Test
    public void updateUserByUserId_OK_ReturnsUserUpdated() {

        UserRequest userUpdate = new UserUpdateRequest();
        userUpdate.setFirstName("Agustin");
        userUpdate.setLastName("Costanzo");
        userUpdate.setEmail("pacostanzo@gmail.com");

        given(userRepository.findByUserId(anyString())).willReturn(user);

        UserUpdateResponse userRest = userService.updateUserByUserId("userId", userUpdate);

        assertThat(userRest.getFirstName()).isEqualTo("Agustin");
        assertThat(userRest.getLastName()).isEqualTo("Costanzo");
        assertThat(userRest.getEmail()).isEqualTo("pacostanzo@gmail.com");

    }

    @Test(expected = UserNotFoundException.class)
    public void updateUserByUserId_WhenUserNotFound() {
        given(userRepository.findByUserId(anyString())).willReturn(null);
        userService.updateUserByUserId(anyString(), new UserUpdateRequest());
    }

    @Test(expected = UserNotUpdatedException.class)
    public void updateUserByUserId_WhenUserCouldNotUpdate() {

        UserRequest updateRequest = new UserUpdateRequest();
        updateRequest.setLastName("Costanzo");
        updateRequest.setEmail("costanzopha@gmail.com");

        given(userRepository.findByUserId(anyString())).willReturn(user);
        given(userRepository.save(any())).willThrow(new DuplicateKeyException("Test Exception"));
        userService.updateUserByUserId(anyString(), updateRequest);
    }


    @Test
    public void deleteUserByUserId_OK_ReturnsUserDeleted() {

        given(userRepository.findByUserId(anyString())).willReturn(user);

        UserGetResponse userRest = userService.deleteUserByUserId("userId");

        assertThat(userRest.getFirstName()).isEqualTo("Pablo");
        assertThat(userRest.getLastName()).isEqualTo("Costanzo");
        assertThat(userRest.getEmail()).isEqualTo("costanzopa@gmail.com");
    }


    @Test(expected = UserNotFoundException.class)
    public void deleteUserByUserId_WhenUserNotFound() {
        given(userRepository.findByUserId(anyString())).willReturn(null);
        userService.deleteUserByUserId(anyString());
    }

}

package com.jandprocu.jandchase.api.usersms.service;

import com.jandprocu.jandchase.api.usersms.exception.RoleNotFoundException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotCreatedException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotFoundException;
import com.jandprocu.jandchase.api.usersms.model.Role;
import com.jandprocu.jandchase.api.usersms.repository.RoleRepository;
import com.jandprocu.jandchase.api.usersms.rest.UserRest;
import com.jandprocu.jandchase.api.usersms.rest.request.UserCreateRequest;
import com.jandprocu.jandchase.api.usersms.model.User;
import com.jandprocu.jandchase.api.usersms.repository.UserRepository;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetOAuthResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private IUserService userService;

    private UserCreateRequest createRequest;
    private User user;

    @Before
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        userService = new UserService(userRepository, roleRepository, bCryptPasswordEncoder);
        createRequest = new UserCreateRequest();
        createRequest.setUserName("THIRD_USER");
        createRequest.setFirstName("ThirdUser");
        createRequest.setLastName("ThirdUser");
        createRequest.setEmail("third_user@email.test");
        createRequest.setPassword("12345678");

        user = modelMapper.map(createRequest, User.class);
    }

    @Test
    public void createUser_OK_ReturnsUserInfo() {
        Role role = new Role();
        role.setName("ROLE_USER");
        given(roleRepository.findByName(anyString())).willReturn(role);

        given(userRepository.save(any())).willReturn(new User());

        UserRest userRest = userService.createUser(createRequest);

        assertThat(userRest.getFirstName()).isEqualTo("ThirdUser");
        assertThat(userRest.getLastName()).isEqualTo("ThirdUser");
        assertThat(userRest.getEmail()).isEqualTo("third_user@email.test");

    }


    @Test(expected = UserNotCreatedException.class)
    public void createUser_WhenUserWasNotCreated() throws DataAccessException {
        Role role = new Role();
        role.setName("ROLE_USER");
        given(roleRepository.findByName(anyString())).willReturn(role);
        given(userRepository.save(any())).willThrow(new DuplicateKeyException("Test Exception"));
        userService.createUser(createRequest);
    }

    @Test(expected = RoleNotFoundException.class)
    public void createUser_WhenUserWasNotCreatedBecauseRoleNotFound() throws RoleNotFoundException {
        given(roleRepository.findByName(anyString())).willReturn(null);
        userService.createUser(createRequest);
    }

    @Test
    public void getUserByUserId_OK_ReturnsUserInfo() {

        given(userRepository.findByUserId(anyString())).willReturn(user);

        UserGetResponse userRest = userService.getUserByUserId("userId");

        assertThat(userRest.getFirstName()).isEqualTo("ThirdUser");
        assertThat(userRest.getLastName()).isEqualTo("ThirdUser");
        assertThat(userRest.getEmail()).isEqualTo("third_user@email.test");

    }

    @Test(expected = UserNotFoundException.class)
    public void getUserByUserId_WhenUserNotFound() {
        given(userRepository.findByUserId(anyString())).willReturn(null);
        userService.getUserByUserId(anyString());
    }


    @Test
    public void getUserByUserName_OK_ReturnsUserInfo() {

        given(userRepository.findByUserName(anyString())).willReturn(user);

        UserGetOAuthResponse userRest = userService.getUserByUserName("userName");

        assertThat(userRest.getFirstName()).isEqualTo("ThirdUser");
        assertThat(userRest.getLastName()).isEqualTo("ThirdUser");
        assertThat(userRest.getEmail()).isEqualTo("third_user@email.test");
        assertThat(userRest.getPassword()).isEqualTo("12345678");

    }


    @Test(expected = UserNotFoundException.class)
    public void getUserByUserName_WhenUserNotFound() {
        given(userRepository.findByUserName(anyString())).willReturn(null);
        userService.getUserByUserName(anyString());
    }

    @Test
    public void deleteUserByUserId_OK_ReturnsUserDeleted() {

        given(userRepository.findByUserId(anyString())).willReturn(user);

        UserGetResponse userRest = userService.deleteUserByUserId("userId");

        assertThat(userRest.getFirstName()).isEqualTo("ThirdUser");
        assertThat(userRest.getLastName()).isEqualTo("ThirdUser");
        assertThat(userRest.getEmail()).isEqualTo("third_user@email.test");
    }


    @Test(expected = UserNotFoundException.class)
    public void deleteUserByUserId_WhenUserNotFound() {
        given(userRepository.findByUserId(anyString())).willReturn(null);
        userService.deleteUserByUserId(anyString());
    }
}

package com.jandprocu.jandchase.api.usersms.controller;

import com.jandprocu.jandchase.api.usersms.exception.RoleNotFoundException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotCreatedException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotFoundException;
import com.jandprocu.jandchase.api.usersms.rest.request.UserCreateRequest;
import com.jandprocu.jandchase.api.usersms.rest.response.RoleResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserCreateResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetOAuthResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetResponse;
import com.jandprocu.jandchase.api.usersms.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreateRequest createRequest;
    private UserCreateResponse createResponse;
    private UserGetResponse getUserResponse;
    private UserGetOAuthResponse getOAuthResponse;


    @Before
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        createRequest = new UserCreateRequest();
        createRequest.setUserName("THIRD_USER");
        createRequest.setFirstName("ThirdUser");
        createRequest.setLastName("ThirdUser");
        createRequest.setEmail("third_user@email.test");
        createRequest.setPassword("12345678");

        createResponse = modelMapper.map(createRequest, UserCreateResponse.class);
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setName("ROLE_USER");
        roleResponse.setDescription("Role for common users.");
        createResponse.setRoles(Arrays.asList(roleResponse));
        getUserResponse = modelMapper.map(createResponse, UserGetResponse.class);
        getOAuthResponse = modelMapper.map(getUserResponse, UserGetOAuthResponse.class);
        getOAuthResponse.setPassword("12345678");
    }

    @Test
    public void createUser_ShouldReturnCreatedUser() throws Exception {
        //arrange
        Mockito.when(userService.createUser(any())).thenReturn(createResponse);
        //act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated()).andReturn();

        //assert
        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo(objectMapper.writeValueAsString(createResponse));
    }

    @Test
    public void createUser_NotCreatedBecauseConflict() throws Exception {
        given(userService.createUser(any())).willThrow(new UserNotCreatedException());

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isConflict());
    }


    @Test
    public void createUser_NotCreatedBecauseRoleConflict() throws Exception {
        given(userService.createUser(any())).willThrow(new RoleNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isNotFound());
    }


    @Test
    public void getUser_ShouldReturnUser() throws Exception {

        given(userService.getUserByUserId(anyString())).willReturn(getUserResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/THIRD_USER_ID")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("ThirdUser"))
                .andExpect(jsonPath("lastName").value("ThirdUser"))
                .andExpect(jsonPath("email").value("third_user@email.test"));
    }

    @Test
    public void getUser_NotFound() throws Exception {
        given(userService.getUserByUserId(anyString())).willThrow(new UserNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/userName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void getUserByUserName_ShouldReturnUser() throws Exception {

        given(userService.getUserByUserName(anyString())).willReturn(getOAuthResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/getByUserName/THIRD_USER_ID")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("ThirdUser"))
                .andExpect(jsonPath("lastName").value("ThirdUser"))
                .andExpect(jsonPath("email").value("third_user@email.test"))
                .andExpect(jsonPath("password").value("12345678"));
    }

    @Test
    public void getUserByUserName_NotFound() throws Exception {
        given(userService.getUserByUserName(anyString())).willThrow(new UserNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/getByUserName/userName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUser_ShouldDeleteTheUser() throws Exception {
        //arrange
        given(userService.deleteUserByUserId(anyString())).willReturn(new UserGetResponse());
        //act
        mockMvc.perform(MockMvcRequestBuilders.delete("/asdefasfdsdfdscsa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

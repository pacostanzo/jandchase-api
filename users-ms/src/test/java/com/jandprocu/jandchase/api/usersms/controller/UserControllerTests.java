package com.jandprocu.jandchase.api.usersms.controller;

import com.jandprocu.jandchase.api.usersms.exception.UserNotCreatedException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotFoundException;
import com.jandprocu.jandchase.api.usersms.exception.UserNotUpdatedException;
import com.jandprocu.jandchase.api.usersms.rest.request.UserCreateRequest;
import com.jandprocu.jandchase.api.usersms.rest.request.UserUpdateRequest;
import com.jandprocu.jandchase.api.usersms.rest.response.UserCreateResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserUpdateResponse;
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
    private UserUpdateRequest updateRequest;
    private UserCreateResponse createResponse;
    private UserGetResponse getUserResponse;


    @Before
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        createRequest = new UserCreateRequest();
        createRequest.setUserName("costanzopa");
        createRequest.setFirstName("Pablo");
        createRequest.setLastName("Costanzo");
        createRequest.setEmail("costanzopa@gmail.com");
        createRequest.setPassword("12345678");

        createResponse = modelMapper.map(createRequest, UserCreateResponse.class);
        updateRequest = modelMapper.map(createRequest, UserUpdateRequest.class);
        getUserResponse = modelMapper.map(createRequest, UserGetResponse.class);

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
    public void getUser_ShouldReturnUser() throws Exception {

        given(userService.getUserByUserId(anyString())).willReturn(getUserResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/asdefasfdsdfdscsa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("Pablo"))
                .andExpect(jsonPath("lastName").value("Costanzo"))
                .andExpect(jsonPath("email").value("costanzopa@gmail.com"));
    }

    @Test
    public void getUser_NotFound() throws Exception {
        given(userService.getUserByUserId(anyString())).willThrow(new UserNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/pacostanzo@gmail.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void updateUser_ShouldReturnUserUpdated() throws Exception {

        UserUpdateResponse updatedUser = new UserUpdateResponse();
        updatedUser.setFirstName("Agustin");
        updatedUser.setLastName("Costanzo");
        updatedUser.setEmail("pacostanzo@gmail.com");
        given(userService.updateUserByUserId(anyString(), any())).willReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/asdefasfdsdfdscsa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("Agustin"))
                .andExpect(jsonPath("lastName").value("Costanzo"))
                .andExpect(jsonPath("email").value("pacostanzo@gmail.com"));
    }


    @Test
    public void updateUser_NotFound() throws Exception {

        given(userService.updateUserByUserId(anyString(), any())).willThrow(new UserNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.put("/pacostanzo@gmail.com")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateUser_BadRequest() throws Exception {

        given(userService.updateUserByUserId(anyString(), any())).willThrow(new UserNotUpdatedException());

        mockMvc.perform(MockMvcRequestBuilders.put("/pacostanzo@gmail.com")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
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


    @Test
    public void createRequestWithEmptyValues_ShouldReturnAConstraintException() throws Exception {
        //arrange
        Mockito.when(userService.createUser(any())).thenReturn(createResponse);
        //act
        mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserCreateRequest())))
                .andExpect(status().isBadRequest()).andReturn();
    }

}

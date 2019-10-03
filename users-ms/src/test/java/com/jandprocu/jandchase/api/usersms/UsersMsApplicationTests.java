package com.jandprocu.jandchase.api.usersms;

import com.jandprocu.jandchase.api.usersms.rest.request.UserCreateRequest;
import com.jandprocu.jandchase.api.usersms.rest.request.UserRequest;
import com.jandprocu.jandchase.api.usersms.rest.request.UserUpdateRequest;
import com.jandprocu.jandchase.api.usersms.rest.response.UserCreateResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserUpdateResponse;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersMsApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Value("${local.server.url}")
    String localHost;

    private static String userId;
    private UserCreateRequest createRequest;
    private UserRequest updateRequest;
    private MultiValueMap<String, String> headers;


    @Before
    public void setUp() {
        createRequest = new UserCreateRequest();
        createRequest.setUserName("costanzopa");
        createRequest.setFirstName("Pablo");
        createRequest.setLastName("Costanzo");
        createRequest.setEmail("costanzopa@gmail.com");
        createRequest.setPassword("12345678");

        updateRequest = new UserUpdateRequest();
        updateRequest.setUserName("costanzopa");
        updateRequest.setFirstName("Agustin");
        updateRequest.setLastName("Costanzo");
        updateRequest.setEmail("pcostanzo@gmail.com");

        headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
    }

    @Test
    public void a_createUser_OK_ReturnsUserDetails() throws Exception {
        //arrange
        HttpEntity<UserCreateRequest> request = new HttpEntity<>(createRequest);

        //act
        ResponseEntity<UserCreateResponse> response = restTemplate.postForEntity(
                localHost + randomServerPort +
                        "/users/", request, UserCreateResponse.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getUserName()).isEqualTo("costanzopa");
        assertThat(response.getBody().getFirstName()).isEqualTo("Pablo");
        assertThat(response.getBody().getLastName()).isEqualTo("Costanzo");
        assertThat(response.getBody().getEmail()).isEqualTo("costanzopa@gmail.com");
        userId = response.getBody().getUserId();
    }

    @Test
    public void b_getUserByUserId_OK_ReturnsUserDetails() throws Exception {
        //act
        ResponseEntity<UserGetResponse> entity = restTemplate.exchange(
                localHost + randomServerPort + "/users/" + userId,
                HttpMethod.GET, new HttpEntity<>(headers),
                UserGetResponse.class);

        //assert
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().getFirstName()).isEqualTo("Pablo");
        assertThat(entity.getBody().getLastName()).isEqualTo("Costanzo");
        assertThat(entity.getBody().getEmail()).isEqualTo("costanzopa@gmail.com");
    }


    @Test
    public void c_updateUserByUserId_OK_ReturnsUserUpdateDetails() throws Exception {
        //arrange
        HttpEntity<UserRequest> request = new HttpEntity<>(updateRequest, headers);

        //act
        ResponseEntity<UserUpdateResponse> entity = restTemplate.exchange(
                localHost + randomServerPort + "/users/" + userId,
                HttpMethod.PUT, request, UserUpdateResponse.class);

        //assert
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().getFirstName()).isEqualTo("Agustin");
        assertThat(entity.getBody().getLastName()).isEqualTo("Costanzo");
        assertThat(entity.getBody().getEmail()).isEqualTo("pcostanzo@gmail.com");
    }


    @Test
    public void d_deleteUserByUserId_OK() throws Exception {
        //act
        restTemplate.exchange(localHost + randomServerPort + "/users/" + userId,
                HttpMethod.DELETE, new HttpEntity<Object>(headers), String.class)
                .getStatusCode().equals(HttpStatus.OK);
    }

}

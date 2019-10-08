package com.jandprocu.jandchase.api.usersms;

import com.jandprocu.jandchase.api.usersms.rest.request.UserCreateRequest;
import com.jandprocu.jandchase.api.usersms.rest.request.UserRequest;
import com.jandprocu.jandchase.api.usersms.rest.request.UserUpdateRequest;
import com.jandprocu.jandchase.api.usersms.rest.response.UserCreateResponse;
import com.jandprocu.jandchase.api.usersms.rest.response.UserGetOAuthResponse;
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

import java.util.Date;

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

    private UserCreateRequest createRequest;
    private UserRequest updateRequest;
    private MultiValueMap<String, String> headers;


    @Before
    public void setUp() {
        createRequest = new UserCreateRequest();
        createRequest.setUserName("THIRD_USER");
        createRequest.setFirstName("ThirdUser");
        createRequest.setLastName("ThirdUser");
        createRequest.setEmail("third_user@email.test");
        createRequest.setPassword("12345678");

        updateRequest = new UserUpdateRequest();
        updateRequest.setUserName("SECOND_USER");
        updateRequest.setFirstName("UpadateSecondUser");
        updateRequest.setLastName("UpadateSecondUser");
        updateRequest.setEmail("update_second_user@email.test");

        headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
    }

    @Test
    public void createUser_OK_ReturnsUserDetails() {
        //arrange
        HttpEntity<UserCreateRequest> request = new HttpEntity<>(createRequest);

        //act
        ResponseEntity<UserCreateResponse> response = restTemplate.postForEntity(
                localHost + randomServerPort +
                        "/", request, UserCreateResponse.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getUserName()).isEqualTo("THIRD_USER");
        assertThat(response.getBody().getFirstName()).isEqualTo("ThirdUser");
        assertThat(response.getBody().getLastName()).isEqualTo("ThirdUser");
        assertThat(response.getBody().getEmail()).isEqualTo("third_user@email.test");
    }

    @Test
    public void getUserByUserId_OK_ReturnsUserDetails() {
        //act
        ResponseEntity<UserGetResponse> entity = restTemplate.exchange(
                localHost + randomServerPort + "/SECOND_USER_ID",
                HttpMethod.GET, new HttpEntity<>(headers),
                UserGetResponse.class);

        //assert
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().getFirstName()).isEqualTo("SecondUser");
        assertThat(entity.getBody().getLastName()).isEqualTo("SecondUser");
        assertThat(entity.getBody().getEmail()).isEqualTo("second_user@email.test");
    }

    @Test
    public void getUserByUserName_OK_ReturnsUserDetails() {
        //act
        ResponseEntity<UserGetResponse> entity = restTemplate.exchange(
                localHost + randomServerPort + "/getByUserName/SECOND_USER",
                HttpMethod.GET, new HttpEntity<>(headers),
                UserGetResponse.class);

        //assert
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().getFirstName()).isEqualTo("SecondUser");
        assertThat(entity.getBody().getLastName()).isEqualTo("SecondUser");
        assertThat(entity.getBody().getEmail()).isEqualTo("second_user@email.test");
    }

    @Test
    public void getUserByUserNameOAuth_OK_ReturnsUserDetails() {
        //act
        ResponseEntity<UserGetOAuthResponse> entity = restTemplate.exchange(
                localHost + randomServerPort + "/getByUserNameOAuth/SECOND_USER",
                HttpMethod.GET, new HttpEntity<>(headers),
                UserGetOAuthResponse.class);

        //assert
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().getFirstName()).isEqualTo("SecondUser");
        assertThat(entity.getBody().getLastName()).isEqualTo("SecondUser");
        assertThat(entity.getBody().getEmail()).isEqualTo("second_user@email.test");
        assertThat(entity.getBody().getPassword()).isEqualTo("12345678");
        assertThat(entity.getBody().getRoles().isEmpty()).isEqualTo(false);
    }

    @Test
    public void updateUserByUserId_OK_ReturnsUserUpdateDetails() {
        //arrange
        HttpEntity<UserRequest> request = new HttpEntity<>(updateRequest, headers);

        //act
        ResponseEntity<UserUpdateResponse> entity = restTemplate.exchange(
                localHost + randomServerPort + "/SECOND_USER_ID",
                HttpMethod.PUT, request, UserUpdateResponse.class);

        //assert
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().getFirstName()).isEqualTo("UpadateSecondUser");
        assertThat(entity.getBody().getLastName()).isEqualTo("UpadateSecondUser");
        assertThat(entity.getBody().getEmail()).isEqualTo("update_second_user@email.test");
    }


    @Test
    public void f_deleteUserByUserId_OK() {
        //act
        restTemplate.exchange(localHost + randomServerPort + "/FIRST_USER_ID",
                HttpMethod.DELETE, new HttpEntity<>(headers), String.class)
                .getStatusCode().equals(HttpStatus.OK);
    }

}

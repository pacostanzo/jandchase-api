package com.jandprocu.janchase.api.oauthms.service;

import com.jandprocu.janchase.api.oauthms.client.UserClient;
import com.jandprocu.janchase.api.oauthms.rest.UserGetOAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        ResponseEntity<UserGetOAuthResponse> userResponse = userClient.getByUserName(userName);
        if(userResponse.getStatusCode().equals(HttpStatus.OK)) {
            UserGetOAuthResponse userGetResponse = userResponse.getBody();
            List<GrantedAuthority> authorities = userGetResponse.getRoles()
                                                                .stream()
                                                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                                                .collect(Collectors.toList());
            return new User(userGetResponse.getUserName(), userGetResponse.getPassword(), userGetResponse.getEnable(), true,
                            true, true, authorities);
        }
        throw new UsernameNotFoundException("Username: " + userName+" not found");
    }
}

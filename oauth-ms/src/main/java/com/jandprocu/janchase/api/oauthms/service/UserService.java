package com.jandprocu.janchase.api.oauthms.service;

import com.jandprocu.janchase.api.oauthms.client.UserClient;
import com.jandprocu.janchase.api.oauthms.rest.UserGetOAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service("userService")
public class UserService implements UserDetailsService, IUserService {

    private Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Usuario loadUserByUsername: " + username);
        ResponseEntity<UserGetOAuthResponse> userResponse = userClient.getUserOAuth(username);
        log.info("Usuario autenticado: " + userResponse);
        if(userResponse.getStatusCode().equals(HttpStatus.OK)) {
            UserGetOAuthResponse userGetResponse = userResponse.getBody();
            List<GrantedAuthority> authorities = userGetResponse.getRoles()
                                                                .stream()
                                                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                                                .collect(Collectors.toList());
            return new User(userGetResponse.getUserName(), userGetResponse.getPassword(), userGetResponse.getEnable(), true,
                            true, true, authorities);
        }
        log.error("Usuario no autenticado: " + username);
        throw new UsernameNotFoundException("Username: " + username+" not found");
    }

    @Override
    public UserGetOAuthResponse findByUsername(String username) {
        return null;
    }
}

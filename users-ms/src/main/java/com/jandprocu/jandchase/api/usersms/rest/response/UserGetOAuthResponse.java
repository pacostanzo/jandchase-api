package com.jandprocu.jandchase.api.usersms.rest.response;

public class UserGetOAuthResponse extends UserGetResponse {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
